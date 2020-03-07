package com.example.syogibase.domain

import android.util.Log
import com.example.syogibase.data.BoardRepositoryImp
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece
import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.data.local.PieceMove
import com.example.syogibase.util.*

class SyogiLogicUseCaseImp(private val boardRepository: BoardRepositoryImp):SyogiLogicUseCase {

    private var turn: Int = BLACK

    //駒を動かした後～王手判定
    override fun checkGameEnd(): Boolean {
        val turnOpponent = if (turn == BLACK) WHITE else BLACK
        val (kingX: Int, kingY: Int) = boardRepository.findKing(turnOpponent)

        //もし王手&詰み判定
        if (checkJudg(kingX, kingY, turnOpponent) && checkmate()) {
            return true
        }
        turn = if (turn == BLACK) WHITE else BLACK
        return false
    }

    //指定したマスのヒント探す
    override fun setTouchHint(x: Int, y: Int) {
        boardRepository.resetHint()
        searchHint(x, y, turn)
    }

    //ヒント取得
    private fun searchHint(touchX: Int, touchY: Int, turn: Int) {
        val moveList: Array<Array<PieceMove>> = boardRepository.getMove(touchX, touchY)

        for (moveDirection in moveList) {
            for (move in moveDirection) {
                val (newX: Int, newY: Int) =
                    if (turn == BLACK) Pair(touchX + move.x, touchY + move.y)
                    else Pair(touchX - move.x, touchY - move.y)
                //動かした先が盤上で自分の駒以外だったらヒントチェック
                if (newX in 0..8 && newY in 0..8 && boardRepository.getTurn(newX, newY) != turn) {
                    setHint(touchX, touchY, newX, newY, turn)
                }
                if (newX in 0..8 && newY in 0..8 && boardRepository.getTurn(newX, newY) != 0) break
            }
        }
    }

    //ヒントを設定する
    override fun setHint(x:Int, y:Int, newX:Int, newY:Int, turn:Int) {
        boardRepository.setPre(x, y)
        boardRepository.setMove(newX, newY, turn,false)
        val (kingX: Int, kingY: Int) = boardRepository.findKing(turn)
        if (!checkJudg(kingX, kingY, turn)) boardRepository.setHint(newX, newY)
        boardRepository.setBackMove()
    }

    //駒を動かす
    override fun setMove(x: Int, y: Int,evolution:Boolean) {
        boardRepository.setMove(x, y, turn,evolution)
        boardRepository.setHoldPiece()
        boardRepository.resetHint()
    }

    //成り判定
    override  fun evolutionCheck(x:Int, y:Int):Boolean{
        val preY = boardRepository.findLogY()
        if ((preY in 0..8 && boardRepository.findEvolutionBy(x, y)) && ((turn == BLACK && (y <= 2 || preY <= 2)) || (turn == WHITE && (6 <= y || 6 <= preY)))){
            return true
        }
        return false
    }

    //キャンセル
    override fun cancel() {
        boardRepository.resetHint()
    }

    //王手判定
    private fun checkJudg(c: Int, r: Int, turnKing: Int): Boolean {
        //↑
        for (j in 1..8) {
            if (0 <= r - j) {
                val cellTurn = boardRepository.getTurn(c, r-j)
                val cellPiece = boardRepository.getPiece(c, r-j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalUpMovePiece() && turnKing == BLACK) || (cellPiece.equalDownMovePiece() && turnKing == WHITE))) return true
                else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == BLACK && turnKing == WHITE)) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //↓
        for (j in 1..8) {
            if (r + j < 9) {
                val cellTurn = boardRepository.getTurn(c, r+j)
                val cellPiece = boardRepository.getPiece(c, r+j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalDownMovePiece() && turnKing == BLACK) || (cellPiece.equalUpMovePiece() && turnKing == WHITE))) return true
                else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == BLACK && turnKing == WHITE)) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //←
        for (j in 1..8) {
            if (0 <= c - j) {
                val cellTurn = boardRepository.getTurn(c-j, r)
                val cellPiece = boardRepository.getPiece(c -j, r)
                if (cellTurn == turnKing) break
                else if (j == 1 && cellPiece.equalLRMovePiece()) return true
                else if (cellPiece.equalLongLRMovePiece()) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //→
        for (j in 1..8) {
            if (c + j < 9) {
                val cellTurn = boardRepository.getTurn(c + j, r)
                val cellPiece = boardRepository.getPiece(c + j, r)
                if (cellTurn == turnKing) break
                else if (cellPiece.equalLRMovePiece() && j == 1) return true
                else if (cellPiece.equalLongLRMovePiece()) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //↖
        for (j in 1..8) {
            if (0 <= c - j && 0 <= r - j) {
                val cellTurn = boardRepository.getTurn(c-j, r-j)
                val cellPiece = boardRepository.getPiece(c -j, r-j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
                else if (cellPiece == KAKU || cellPiece == UMA) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //↙
        for (j in 1..8) {
            if (0 <= c - j && r + j < 9) {
                val cellTurn = boardRepository.getTurn(c-j, r+j)
                val cellPiece = boardRepository.getPiece(c -j, r+j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
                else if (cellPiece == KAKU || cellPiece == UMA) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //↗
        for (j in 1..8) {
            if (c + j < 9 && 0 <= r - j) {
                val cellTurn = boardRepository.getTurn(c+j, r-j)
                val cellPiece = boardRepository.getPiece(c+j, r-j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
                else if (cellPiece == KAKU || cellPiece == UMA) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }
        //↘
        for (j in 1..8) {
            if (c + j < 9 && r + j < 9) {
                val cellTurn = boardRepository.getTurn(c+j, r+j)
                val cellPiece = boardRepository.getPiece(c+j, r+j)
                if (cellTurn == turnKing) break
                else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
                else if (cellPiece == KAKU || cellPiece == UMA) return true
                else if (cellTurn != 0) break
            } else {
                //盤外
                break
            }
        }

        //桂馬のきき
        if (turn == BLACK && 0 <= r - 2) {
            if (0 <= c - 1 && boardRepository.getPiece(c - 1, r - 2) == KEI && boardRepository.getTurn(c - 1, r - 2) != turnKing) return true
            if (c + 1 < 9 && boardRepository.getPiece(c + 1, r - 2) == KEI && boardRepository.getTurn(c + 1, r - 2) != turnKing) return true
        } else if (r + 2 < 9) {
            if (0 <= c - 1 && boardRepository.getPiece(c - 1, r + 2) == KEI && boardRepository.getTurn(c - 1, r + 2) != turnKing) return true
            if (c + 1 < 9 && boardRepository.getPiece(c + 1, r + 2) == KEI && boardRepository.getTurn(c + 1, r + 2) != turnKing) return true
        }

        //王手がなかったらfalseを返す
        return false
    }



    //逃げる場所判定
    private fun checkmate(): Boolean {
        val turn = if (this.turn == BLACK) WHITE else BLACK

        //逃げる場所 or 防げる駒があるか判定
        for (i in 0..8) for (j in 0..8) if (boardRepository.getTurn(i, j) == turn) {
            searchHint(i, j, turn)
        }

        //もしHint(逃げる場所)がなかったら詰み
        val count = boardRepository.getCountByHint()
        boardRepository.resetHint()
        if (count == 0) {
            return true
        }
        return false
    }

    //成り判定 強制か否か
    override fun compulsionEvolutionCheck():Boolean {
        if (boardRepository.checkForcedevolution()){
            evolutionPiece(true)
            return true
        }
        return false
    }

    //成り
    override fun evolutionPiece(bool: Boolean) {
        if (bool) boardRepository.setEvolution()
    }

    //持ち駒を使う場合
    override fun setHintHoldPiece(x: Int, y: Int) {
        boardRepository.resetHint()
        val (newX, newY) = if (y == 10) Pair(x, y) else Pair(8 - x, -1)
        val piece =
            if (y == 0 && turn == WHITE) boardRepository.findHoldPieceBy(8 - x, turn)
            else if (y == 10 && turn == BLACK) boardRepository.findHoldPieceBy(x, turn)
            else None
        if (piece == None) return

        boardRepository.setPre(newX, newY)
        when (piece) {
            GIN, KIN, HISYA, KAKU ->
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (boardRepository.getTurn(i, j) == 0) {
                            setHint(newX, newY, i, j, turn)
                        }
                    }
                }
            KYO ->
                for (i in 0..8) {
                    for (j in 1..8) {
                        val J = if (turn == BLACK) j else 8 - j
                        if (boardRepository.getTurn(i, J) == 0) {
                            setHint(newX, newY, i, J, turn)
                        }
                    }
                }
            KEI ->
                for (i in 0..8) {
                    for (j in 2..8) {
                        val J = if (turn == BLACK) j else 8 - j
                        if (boardRepository.getTurn(i, J) == 0) {
                            setHint(newX, newY, i, J, turn)
                        }
                    }
                }
            FU ->
                for (i in 0..8) { for (j in 0..8) {
                    if (boardRepository.getTurn(i, j) == turn && boardRepository.getPiece(i, j) == FU) break
                    if (j == 8) {
                        for (k in 1..8) {
                            val K = if (y == 10) k else k - 1
                            if (boardRepository.getTurn(i, K) == 0) {
                                setHint(newX, newY, i, k, turn)
                            }
                        }
                    }
                } }
            else -> Log.e("GameLogicPresenter", "不正な持ち駒を取得しようとしています")
        }
    }

    //(駒の名前,手番,ヒントの表示)を返す
    override fun getCellInformation(x:Int,y:Int):Triple<String,Int,Boolean>{
        val cell = boardRepository.getCellInformation(x,y)

        return Triple(cell.piece.nameJP, cell.turn, cell.hint)
    }

    //(駒の名前,手番,ヒントの表示)を返す
    override fun getCellTrun(x:Int,y:Int):Int{
        val cell = boardRepository.getCellInformation(x,y)
        return if(cell.hint){
            3
        }else if(cell.turn == BLACK){
            BLACK
        }else if(cell.turn == WHITE){
            WHITE
        }else{
            4
        }
    }

    //指定した手番の持ち駒を返す
    override  fun getPieceHand(turn:Int):MutableList<Pair<Piece,Int>>{
        val hold = mutableListOf<Pair<Piece,Int>>()
        var i = 0
        boardRepository.getAllHoldPiece(turn).forEach{piece,count ->
            hold.add(i,Pair(piece,count))
            i++
        }
        return hold
    }

    //現在の手番を返す
    override  fun getTurn():Int{
        return turn
    }
    //手番を設定する
    override fun setTurn(turn:Int){
        this.turn = turn
    }
    //最後のログを取得する
    override fun getLogLast(): GameLog {
        return boardRepository.getLogList()
    }
    //動かす駒の元の位置をセットする
    override fun setPre(x:Int, y:Int){
        boardRepository.setPre(x, y)
    }
}
