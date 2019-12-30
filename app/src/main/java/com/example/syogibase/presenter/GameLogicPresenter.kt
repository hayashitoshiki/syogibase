package com.example.syogibase.presenter


import android.util.Log
import com.example.syogibase.contact.GameViewContact
import com.example.syogibase.model.BoardRepository
import com.example.syogibase.model.data.Piece.*

class GameLogicPresenter(private val view: GameViewContact.View,private val boardRepository:BoardRepository): GameViewContact.Presenter {

    companion object {
        const val BLACK = 1
        const val WHITE = 2
    }

    private var turn:Int = 1


    //タッチ判定　ヒント表示 or Move
    override fun onTouchEvent(x:Int, y:Int) {

        //持ち駒をタップした場合は持ちを待を置ける場所を返す
        if(y == 0 || y == 10){
            useHoldPiece(x,y)
        }
        //盤上
        else if( x in 0..8 && y in 1..9 ){
            //ヒント表示がされているマスををタップした場合動かす
            if(boardRepository.getHint(x,y-1)){
                setMove(x,y-1)
                val turnOpponent = if(turn==BLACK) WHITE else BLACK
                val (kingX: Int, kingY: Int) = boardRepository.findKing(turnOpponent)

                if(checkJudg(kingX, kingY,turnOpponent) && checkmate()){
                    view.gameEnd()
                }
                turn = if(turn == BLACK) WHITE else BLACK
            }
            //盤上の自分の駒をタップした場合ヒントを表示する
            else if(boardRepository.getPiece(x,y-1) != None && boardRepository.getTurn(x,y-1) == turn){
                boardRepository.resetHint()
                getHint(x,y-1,turn)
            } else{
                cancel()
            }
        }else{
            cancel()
        }
    }

    //ヒント取得
    private fun getHint(touchX:Int, touchY:Int,turn:Int){
        val moveList =  boardRepository.getMove(touchX,touchY)

        for(moveDirection in moveList){
            for(move in moveDirection){
                val (newX:Int, newY:Int) =
                    if(turn == BLACK) Pair(touchX+move.x, touchY+move.y)
                    else              Pair(touchX-move.x, touchY-move.y)
                //駒を動かしたとき自分の王様が王手にならなかったらヒントを表示
               if(newX in 0..8 && newY in 0..8 && boardRepository.getTurn(newX,newY) != turn) {
                   boardRepository.setPre(touchX, touchY)
                   boardRepository.setMove(newX, newY, turn)
                   val (kingX: Int, kingY: Int) = boardRepository.findKing(turn)
                   if (!checkJudg(kingX, kingY, turn)) boardRepository.setHint(newX, newY)
                   boardRepository.setBackMove()
               }
                if(newX in 0..8 && newY in 0..8 && boardRepository.getTurn(newX,newY) != 0)break
            }
        }
        boardRepository.getCountByHint()
    }

    //駒を動かす
    private fun setMove(x:Int, y:Int){
        boardRepository.setMove(x, y, turn)
        boardRepository.setHoldPiece()
        //成り判定
        val preY = boardRepository.findLogY()
        if((preY in 0..8 && boardRepository.findEvolutionBy(x,y))&&
            ((turn == BLACK && (y<= 2 || preY<=2)) ||
             (turn == WHITE && (6<=y  || 6<=preY)))) evolutionCheck()

        boardRepository.resetHint()
    }

    //キャンセル
    private fun cancel(){
        boardRepository.resetHint()
    }

    //王手判定
    private fun checkJudg(c:Int, r:Int,turnKing:Int):Boolean{
        //↑
        for(j in 1..8){
            if(0<= r-j){
                if(j == 1 && boardRepository.getTurn(c,r-j) != turnKing &&
                    ((boardRepository.findUpMovePiece(c,r-j) && turnKing == BLACK)||
                    (boardRepository.findDownMovePiece(c,r-j) && turnKing == WHITE))) return true
                else if(((boardRepository.getPiece(c,r-j) == HISYA || boardRepository.getPiece(c,r-j) == RYU) && boardRepository.getTurn(c,r-j) != turnKing) ||
                    (boardRepository.getPiece(c,r-j) == KYO && boardRepository.getTurn(c,r-j) == WHITE && turnKing == BLACK) ) return true
                else if(boardRepository.getTurn(c,r-j) != 0) break
            }else{
                //盤外
                break
            }
        }
        //↓
        for(j in 1..8){
            if(r+j<9){
                if(j == 1 && boardRepository.getTurn(c,r+j) != turnKing &&
                    ((boardRepository.findDownMovePiece(c,r+j) && turnKing == BLACK)||
                     (boardRepository.findUpMovePiece(c,r+j) && turnKing == WHITE))) return true
                else if(((boardRepository.getPiece(c,r+j) == HISYA || boardRepository.getPiece(c,r+j) == RYU) && boardRepository.getTurn(c,r+j) != turnKing) ||
                    (boardRepository.getPiece(c,r+j) == KYO && boardRepository.getTurn(c,r+j) == BLACK && turnKing == WHITE))return true
                else if(boardRepository.getTurn(c,r+j) != 0) break
            }else{
                //盤外
                break
            }
        }
        //←
        for(j in 1..8){
            if(0<=c-j){
                if(j == 1 && boardRepository.getTurn(c-j,r) != turnKing &&
                    boardRepository.findLRMovePiece(c-j,r)) return true
                else if((boardRepository.getPiece(c-j,r) == HISYA || boardRepository.getPiece(c-j,r) == RYU) && boardRepository.getTurn(c-j,r) != turnKing) return true
                else if(boardRepository.getTurn(c-j,r) != 0) break
            }else{
                //盤外
                break
            }
        }
        //→
        for(j in 1..8){
            if(c+j < 9){
                if(boardRepository.getTurn(c+j,r) == turnKing) break
                else if(j == 1 && boardRepository.getTurn(c+j,r) != turnKing &&
                    boardRepository.findLRMovePiece(c+j,r)) return true
                else if((boardRepository.getPiece(c+j,r) == HISYA || boardRepository.getPiece(c+j,r) == RYU) && boardRepository.getTurn(c+j,r) != turnKing) return true
                else if(boardRepository.getTurn(c+j,r) != 0) break
            }else{
                //盤外
                break
            }
        }
        //↖
        for(j in 1..8){
            if(0<=c-j && 0<=r-j){
                if(boardRepository.getTurn(c-j,r-j) == turnKing) break
                else if(j == 1 && boardRepository.getTurn(c-j,r-j) != turnKing &&
                    ((boardRepository.findDiagonalUp(c-j,r-j) && turnKing == BLACK)||
                    (boardRepository.findDiagonalDown(c-j,r-j) && turnKing == WHITE))) return true
                else if((boardRepository.getPiece(c-j,r-j) == KAKU || boardRepository.getPiece(c-j,r-j) == UMA) && boardRepository.getTurn(c-j,r-j) != turnKing) return true
                else if(boardRepository.getTurn(c-j,r-j) != 0) break
            }else{
                //盤外
                break
            }
        }
        //↙
        for(j in 1..8){
            if(0<=c-j && r+j<9){
                if(j == 1 && boardRepository.getTurn(c-j,r+j) != turnKing &&
                    ((boardRepository.findDiagonalDown(c-j,r+j) && turnKing == BLACK)||
                    (boardRepository.findDiagonalUp(c-j,r+j) && turnKing == WHITE))) return true
                else if((boardRepository.getPiece(c-j,r+j) == KAKU || boardRepository.getPiece(c-j,r+j) == UMA)  && boardRepository.getTurn(c-j,r+j) != turnKing) return true
                else if(boardRepository.getTurn(c-j,r+j) != 0) break
            }else{
                //盤外
                break
            }
        }
        //↗
        for(j in 1..8){
            if(c+j<9 && 0<=r-j){
                if(j == 1 && boardRepository.getTurn(c+j,r-j) != turnKing &&
                    ((boardRepository.findDiagonalUp(c+j,r-j) && turnKing == BLACK)||
                     (boardRepository.findDiagonalDown(c+j,r-j) && turnKing == WHITE))) return true
                else if((boardRepository.getPiece(c+j,r-j) == KAKU || boardRepository.getPiece(c+j,r-j) == UMA) && boardRepository.getTurn(c+j,r-j) != turnKing) return true
                else if(boardRepository.getTurn(c+j,r-j) != 0) break
            }else{
                //盤外
                break
            }
        }
        //↘
        for(j in 1..8){
            if(c+j<9 && r+j<9){
                if(j == 1 && boardRepository.getTurn(c+j,r+j) != turnKing &&
                    ((boardRepository.findDiagonalDown(c+j,r+j) && turnKing == BLACK)||
                     (boardRepository.findDiagonalUp(c+j,r+j) && turnKing == WHITE))) return true
                else if((boardRepository.getPiece(c+j,r+j) == KAKU || boardRepository.getPiece(c+j,r+j) == UMA) && boardRepository.getTurn(c+j,r+j) != turnKing) return true
                else if(boardRepository.getTurn(c+j,r+j) != 0) break
            }else{
                //盤外
                break
            }
        }

        //桂馬のきき
        if(turn == BLACK && 0<=r-2){
            if(0<=c-1 && boardRepository.getPiece(c-1,r-2) == KEI && boardRepository.getTurn(c-1,r-2) != turnKing)return true
            if(c+1<9 && boardRepository.getPiece(c+1,r-2) == KEI && boardRepository.getTurn(c+1,r-2) != turnKing) return true
        } else if(r+2<9){
            if(0<=c-1 && boardRepository.getPiece(c-1,r+2) == KEI && boardRepository.getTurn(c-1,r+2) != turnKing)return true
            if(c+1<9 && boardRepository.getPiece(c+1,r+2) == KEI && boardRepository.getTurn(c+1,r+2) != turnKing) return true
        }

        //王手がなかったらfalseを返す
        return false
    }

    //将棋描画
    override fun drawView(){
        view.drawBoard()

        //盤上の駒セット
        for (i in 0..8) {
            for (j in 0..8) {
                if (boardRepository.getHint(i,j))view.drawHint(i,j)
                if (boardRepository.getTurn(i,j) == WHITE) view.drawWhitePiece(boardRepository.getJPName(i,j),i,j)
                else view.drawBlackPiece(boardRepository.getJPName(i,j),i,j)
            }
        }
        //持ち駒セット
        var count = 0
        boardRepository.getAllHoldPiece(BLACK).forEach { (key, value) ->
            if(value != 0) view.drawHoldPirceBlack(key.nameJP, value, count)
            count++
        }
        count = 0
        boardRepository.getAllHoldPiece(WHITE).forEach { (key, value) ->
            if(value != 0) view.drawHoldPirceWhite(key.nameJP, value, count)
            count++
        }
    }

    //逃げる場所判定
    private fun checkmate():Boolean{
        val turnOpponent = if(this.turn == BLACK) WHITE else BLACK

        //逃げれる場所 or 防げる駒があるか判定
        for(i in 0..8)for(j in 0..8)if(boardRepository.getTurn(i,j) == turnOpponent){
            getHint(i, j,turnOpponent)
        }

        val count = boardRepository.getCountByHint()
        boardRepository.resetHint()
        if(count == 0)return true
        return false
    }

    //成り判定
     private fun evolutionCheck(){
        if(boardRepository.checkForcedevolution())evolutionPiece(true)
        else view.showDialog()
    }

    //成り
    override fun evolutionPiece(bool:Boolean){
        if(bool) boardRepository.setEvolution()
    }

    //持ち駒を使う場合
    private fun useHoldPiece(x:Int, y:Int){
        val (newX, newY) = if(y == 10) Pair(x,y) else Pair(8-x, -1)
        val piece =
             if(y ==  0 && turn == WHITE)boardRepository.findHoldPieceBy(8-x, turn)
        else if(y == 10 && turn == BLACK)boardRepository.findHoldPieceBy(x, turn)
        else None
        boardRepository.resetHint()
        if(piece == None)return

        boardRepository.setPre(newX, newY)
        when(piece){
            GIN,KIN,HISYA,KAKU ->
                for(i in 0..8){
                    for(j in 0..8){
                        if(boardRepository.getTurn(i,j) == 0) {
                            boardRepository.setPre(newX, newY)
                            boardRepository.setMove(i,j,turn)
                            val(kingX:Int,kingY:Int) = boardRepository.findKing(turn)
                            if(!checkJudg(kingX, kingY,turn)) boardRepository.setHint(i, j)
                            boardRepository.setBackMove()
                        }
                    }
                }
            KYO ->
                for(i in 0..8){
                    for(j in 1..8){
                        val J = if(turn ==BLACK) j else 8-j
                        if(boardRepository.getTurn(i,J) == 0) {
                            boardRepository.setPre(newX, newY)
                            boardRepository.setMove(i, J, turn)
                            val (kingX: Int, kingY: Int) = boardRepository.findKing(turn)
                            if (!checkJudg(kingX, kingY, turn)) boardRepository.setHint(i, J)
                            boardRepository.setBackMove()
                        }
                    }
                }
            KEI ->
                for(i in 0..8){
                    for(j in 2..8){
                        val J = if(turn == BLACK) j else 8-j
                        if(boardRepository.getTurn(i,J) == 0) {
                            boardRepository.setPre(newX, newY)
                            boardRepository.setMove(i, J, turn)
                            val (kingX: Int, kingY: Int) = boardRepository.findKing(turn)
                            if (!checkJudg(kingX, kingY, turn)) boardRepository.setHint(i, J)
                            boardRepository.setBackMove()
                        }
                    }
                }
            FU ->
                for (i in 0..8){
                    for(j in 0..8){
                    if(boardRepository.getTurn(i,j) == turn && boardRepository.getPiece(i,j) == FU)break
                        if(j==8){
                            for(k in 1..8){
                                val K = if(y == 10) k else k-1
                                if(boardRepository.getTurn(i,K) == 0){
                                    boardRepository.setPre(newX, newY)
                                    boardRepository.setMove(i,K,turn)
                                    val(kingX:Int,kingY:Int) = boardRepository.findKing(turn)
                                    if(!checkJudg(kingX, kingY,turn)) boardRepository.setHint(i, K)
                                    boardRepository.setBackMove()
                                }
                            }
                        }
                    }
                }
            else -> Log.e("GameLogicPresenter","不正な持ち駒を取得しようとしています")
        }
    }
}