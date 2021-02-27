package com.example.syogibase.domain

import android.util.Log
import com.example.syogibase.data.BoardRepository
import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.Piece
import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.data.local.PieceMove
import com.example.syogibase.util.BLACK
import com.example.syogibase.util.WHITE

class SyogiLogicUseCaseImp(
    private val boardRepository: BoardRepository
) : SyogiLogicUseCase {

    private var turn: Int = BLACK

    private var positionList = mutableMapOf<String, Int>()

    // region アクション

    // 現在の手番を返す
    override fun getTurn(): Int {
        return turn
    }

    // 駒落ち設定
    override fun setHandicap(turn: Int, handicap: Int) {
        boardRepository.setHandicap(turn, handicap)
    }

    // 指定した盤面設定
    override fun setBoard(customBoard: Array<Array<Cell>>) {
        boardRepository.setBoard(customBoard)
    }

    // 指定したマスのヒント探す
    override fun setTouchHint(x: Int, y: Int) {
        boardRepository.resetHint()
        searchHint(x - 1, y - 1, turn)
    }

    // 駒を動かす
    override fun setMove(x: Int, y: Int, evolution: Boolean) {
        var position = ""
        boardRepository.setMove(x - 1, y - 1, turn, evolution)
        boardRepository.setHoldPiece()
        boardRepository.resetHint()
        boardRepository.getBoard().forEach {
            it.forEach { cell ->
                position += cell.hint.toString() + cell.piece.toString() + cell.turn.toString()
            }
        }
        if (positionList.containsKey(position)) {
            positionList[position] = positionList[position]!!.toInt() + 1
        } else {
            positionList[position] = 1
        }
    }

    // 持ち駒を使う場合
    override fun setHintHoldPiece(x: Int, y: Int, kingTurn: Int) {
        boardRepository.resetHint()
        val piece =
            if ((y == -1 && kingTurn == WHITE) || (y == 10 && kingTurn == BLACK)) {
                boardRepository.findHoldPieceBy(x, kingTurn)
            } else {
                None
            }
        if (piece == None) return

        boardRepository.setPre(x, y)
        when (piece) {
            GIN, KIN, HISYA, KAKU ->
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (boardRepository.getTurn(i, j) == 0) {
                            setHint(x, y, i, j, kingTurn)
                        }
                    }
                }
            KYO ->
                for (i in 0..8) {
                    for (j in 1..8) {
                        val k = if (kingTurn == BLACK) j else 8 - j
                        if (boardRepository.getTurn(i, k) == 0) {
                            setHint(x, y, i, k, kingTurn)
                        }
                    }
                }
            KEI ->
                for (i in 0..8) {
                    for (j in 2..8) {
                        val k = if (kingTurn == BLACK) j else 8 - j
                        if (boardRepository.getTurn(i, k) == 0) {
                            setHint(x, y, i, k, kingTurn)
                        }
                    }
                }
            FU -> {
                val xList = mutableListOf<Int>()
                val yList = mutableListOf<Int>()
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (boardRepository.getTurn(i, j) == kingTurn && boardRepository.getPiece(
                                i,
                                j
                            ) == FU
                        ) break
                        if (j == 8) {
                            for (k in 1..8) {
                                val l = if (y == 10) k else k - 1
                                if (boardRepository.getTurn(
                                        i,
                                        l
                                    ) == 0 && !isCheckMateByPossessionFu(x, y, i, l, kingTurn)
                                ) {
                                    xList.add(i)
                                    yList.add(l)
                                }
                            }
                        }
                    }
                }
                xList.forEachIndexed { i, _ ->
                    setHint(x, y, xList[i], yList[i], kingTurn)
                }
            }
            else -> Log.e("GameLogicPresenter", "不正な持ち駒を取得しようとしています")
        }
    }

    // ヒント取得
    private fun searchHint(touchX: Int, touchY: Int, turn: Int) {
        val moveList: Array<Array<PieceMove>> = boardRepository.getPiece(touchX, touchY).getMove()

        for (moveDirection in moveList) {
            for (move in moveDirection) {
                var newX = 0
                var newY = 0
                when (turn) {
                    BLACK -> {
                        newX = touchX + move.x
                        newY = touchY + move.y
                    }
                    WHITE -> {
                        newX = touchX - move.x
                        newY = touchY - move.y
                    }
                }
                // 範囲外か自分のコマとかぶったらその方向の検索はストップ
                if (newX !in 0..8 || newY !in 0..8 || boardRepository.getTurn(newX, newY) == turn) {
                    break
                }

                setHint(touchX, touchY, newX, newY, turn)
                if (boardRepository.getTurn(newX, newY) != 0) break
            }
        }
    }

    // ヒントを設定する
    private fun setHint(x: Int, y: Int, newX: Int, newY: Int, turn: Int) {
        boardRepository.setPre(x, y)
        boardRepository.setMove(newX, newY, turn, false)
        val (kingX: Int, kingY: Int) = boardRepository.findKing(turn)
        if (!isCheck(kingX, kingY, turn))
            boardRepository.setHint(newX, newY)
        boardRepository.setPreBackMove()
    }

    // キャンセル
    override fun cancel() {
        boardRepository.resetHint()
    }

    // endregion

    // region 盤面描画

    // (駒の名前,手番,ヒントの表示)を返す
    override fun getCellInformation(x: Int, y: Int): Cell {
        return boardRepository.getCellInformation(x, y)
    }

    // マスの手番を返す
    override fun getCellTurn(x: Int, y: Int): Int {
        val cell = boardRepository.getCellInformation(x - 1, y - 1)
        return when {
            cell.hint -> 3
            cell.turn == BLACK -> BLACK
            cell.turn == WHITE -> WHITE
            else -> 4
        }
    }

    // 指定した手番の持ち駒を返す
    override fun getPieceHand(turn: Int): MutableList<Pair<Piece, Int>> {
        val hold = mutableListOf<Pair<Piece, Int>>()
        var i = 0
        boardRepository.getAllHoldPiece(turn).forEach { (piece, count) ->
            hold.add(i, Pair(piece, count))
            i++
        }
        return hold
    }

    // endregion

    // region ルール

    // 千日手判定
    override fun isRepetitionMove(): Boolean {
        positionList.forEach { (_, v) ->
            if (v >= 4) return true
        }
        return false
    }

    // トライルール判定
    override fun isTryKing(): Boolean {
        val cell = when (turn) {
            BLACK -> boardRepository.getCellInformation(4, 0)
            WHITE -> boardRepository.getCellInformation(4, 8)
            else -> null
        }
        return (cell != null && (cell.piece == GYOKU || cell.piece == OU) && cell.turn == turn)
    }

    // 終了判定
    override fun isGameEnd(): Boolean {
        // もし王手&詰み判定
        if (isCheckmate()) {
            return true
        }
        turn = if (turn == BLACK) WHITE else BLACK
        return false
    }

    // 王手判定
    private fun isCheck(x: Int, y: Int, turnKing: Int): Boolean {
        // ↑
        for (j in 1..8) {
            val moveY = y - j
            if (moveY < 0) break

            // 判定
            val cellTurn = boardRepository.getTurn(x, moveY)
            val cellPiece = boardRepository.getPiece(x, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalUpMovePiece() && turnKing == BLACK) || (cellPiece.equalDownMovePiece() && turnKing == WHITE))) return true
            else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == BLACK && turnKing == WHITE)) return true
            else if (cellTurn != 0) break
        }
        // ↓
        for (j in 1..8) {
            val moveY = y + j
            if (9 <= moveY) break

            // 判定
            val cellTurn = boardRepository.getTurn(x, moveY)
            val cellPiece = boardRepository.getPiece(x, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDownMovePiece() && turnKing == BLACK) || (cellPiece.equalUpMovePiece() && turnKing == WHITE))) return true
            else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == BLACK && turnKing == WHITE)) return true
            else if (cellTurn != 0) break
        }
        // ←
        for (j in 1..8) {
            val moveX = x - j
            if (moveX < 0) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, y)
            val cellPiece = boardRepository.getPiece(moveX, y)
            if (cellTurn == turnKing) break
            else if (j == 1 && cellPiece.equalLRMovePiece()) return true
            else if (cellPiece.equalLongLRMovePiece()) return true
            else if (cellTurn != 0) break
        }
        // →
        for (j in 1..8) {
            val moveX = x + j
            if (9 <= moveX) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, y)
            val cellPiece = boardRepository.getPiece(moveX, y)
            if (cellTurn == turnKing) break
            else if (j == 1 && cellPiece.equalLRMovePiece()) return true
            else if (cellPiece.equalLongLRMovePiece()) return true
            else if (cellTurn != 0) break
        }
        // ↖
        for (j in 1..8) {
            val moveX = x - j
            val moveY = y - j
            if (moveX < 0 || moveY < 0) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, moveY)
            val cellPiece = boardRepository.getPiece(moveX, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != 0) break

        }
        // ↙
        for (j in 1..8) {
            val moveX = x - j
            val moveY = y + j
            if (moveX < 0 || 9 <= moveY) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, moveY)
            val cellPiece = boardRepository.getPiece(moveX, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != 0) break
        }
        // ↗
        for (j in 1..8) {
            val moveX = x + j
            val moveY = y - j
            if (9 <= moveX || moveY < 0) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, moveY)
            val cellPiece = boardRepository.getPiece(moveX, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != 0) break
        }
        // ↘
        for (j in 1..8) {
            val moveX = x + j
            val moveY = y + j
            if (9 <= moveX || 9 <= moveY) break

            // 判定
            val cellTurn = boardRepository.getTurn(moveX, moveY)
            val cellPiece = boardRepository.getPiece(moveX, moveY)
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != 0) break
        }

        // 桂馬のきき
        val y1 = y - 2
        val y2 = y + 2
        val x1 = x - 1
        val x2 = x + 1
        if (turnKing == BLACK && 0 <= y1) {
            if (0 <= x1 && boardRepository.getPiece(x1, y1) == KEI && boardRepository.getTurn(
                    x1,
                    y1
                ) != turnKing
            ) return true
            if (x2 < 9 && boardRepository.getPiece(x2, y1) == KEI && boardRepository.getTurn(
                    x2,
                    y1
                ) != turnKing
            ) return true
        } else if (turnKing == WHITE && y2 < 9) {
            if (0 <= x1 && boardRepository.getPiece(x1, y2) == KEI && boardRepository.getTurn(
                    x1,
                    y2
                ) != turnKing
            ) return true
            if (x2 < 9 && boardRepository.getPiece(x2, y2) == KEI && boardRepository.getTurn(
                    x2,
                    y2
                ) != turnKing
            ) return true
        }

        // 王手がなかったらfalseを返す
        return false
    }

    // 逃げる場所判定
    private fun isCheckmate(): Boolean {
        val kingTurn = if (this.turn == BLACK) WHITE else BLACK

        // 逃げる場所 or 防げる駒があるか判定
        for (i in 0..8) {
            for (j in 0..8) {
                if (boardRepository.getTurn(i, j) == kingTurn) {
                    searchHint(i, j, kingTurn)
                }
            }
        }
        // 持ち駒使う
        getPieceHand(kingTurn).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                val (x, y) =
                    if (kingTurn == BLACK) {
                        Pair(index + 2, 10)
                    } else {
                        Pair(index + 2, -1)
                    }
                setHintHoldPiece(x, y, kingTurn)
            }
        }

        // もしHint(逃げる場所)がなかったら詰み
        val count = boardRepository.getCountByHint()
        boardRepository.resetHint()
        return count == 0
    }

    // 打ち歩詰め判定
    private fun isCheckMateByPossessionFu(
        x: Int,
        y: Int,
        newX: Int,
        newY: Int,
        turn: Int
    ): Boolean {
        boardRepository.setPre(x, y)
        boardRepository.setMove(newX, newY, turn, false)
        val result = isCheckmate()
        boardRepository.setPreBackMove()
        return result
    }

    // 成り判定
    override fun isEvolution(x: Int, y: Int): Boolean {
        val before = boardRepository.getBeforePieceCoordinate()
        return (before.y + 1 in 1..9 && boardRepository.getPiece(x - 1, y - 1)
            .findEvolution()) && ((turn == BLACK && (y <= 3 || before.y + 1 <= 3)) || (turn == WHITE && (7 <= y || 7 <= before.y + 1)))
    }

    // 成り判定 強制か否か
    override fun isCompulsionEvolution(): Boolean {
        if (boardRepository.isCompulsionEvolution()) {
            setEvolution()
            return true
        }
        return false
    }

    // 成り
    override fun setEvolution() {
        boardRepository.setEvolution()
    }

    // endregion
}
