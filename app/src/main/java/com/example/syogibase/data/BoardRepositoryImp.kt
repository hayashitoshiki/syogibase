package com.example.syogibase.data


import com.example.syogibase.data.local.Board
import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece
import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.util.*


class BoardRepositoryImp : BoardRepository {

    private val board: Board = Board()

    // カスタム初期値設定
    override fun setBoard(customBoard: Array<Array<Cell>>) {
        board.setBoard(customBoard)
    }

    // 盤面を初期化する
    override fun resetBoard() {
        board.setBoard(Board().cells)
        board.holdPieceBlack.mapValues { 0 }
        board.holdPieceWhite.mapValues { 0 }
    }

    // region マスの情報取得

    // 局面を取得
    override fun getBoard(): Array<Array<Cell>> {
        return board.cells
    }

    //指定したマスの情報を返す
    override fun getCellInformation(x: Int, y: Int): Cell {
        return board.cells[x][y]
    }

    //指定したマスの駒を返す
    override fun getPiece(x: Int, y: Int): Piece {
        return board.cells[x][y].piece
    }

    //指定のマスの駒の所有者を返す
    override fun getTurn(x: Int, y: Int): Int {
        return board.cells[x][y].turn
    }

    //指定したマスのヒントを返す
    override fun getHint(x: Int, y: Int): Boolean {
        return board.cells[x][y].hint
    }

    //ヒントが表示されているマスの数を返す
    override fun getCountByHint(): Int {
        var count = 0
        board.cells.forEach { count += it.filter { it.hint }.count() }

        return count
    }

    // endregion

    // region マスの情報更新

    // 駒落ち設定
    override fun setHandicap(turn: Int, handicap: Handicap) {
        board.setHandicap(turn, handicap)
    }

    // 駒を動かす
    override fun setGoMove(log: GameLog) {
        board.cells[log.newX][log.newY].turn = log.afterTurn
        board.cells[log.newX][log.newY].piece =
            if (log.evolution) log.afterPiece.evolution() else log.afterPiece
        when (log.oldY) {
            BLACK_HOLD -> board.holdPieceBlack[log.afterPiece] =
                board.holdPieceBlack[log.afterPiece]!! - 1
            WHITE_HOLD -> board.holdPieceWhite[log.afterPiece] =
                board.holdPieceWhite[log.afterPiece]!! - 1
            else -> {
                board.cells[log.oldX][log.oldY].piece = None
                board.cells[log.oldX][log.oldY].turn = 0
            }
        }
        val piece = log.beforpiece.degeneration()
        if (board.holdPieceBlack.keys.contains(piece)) {
            when (log.beforturn) {
                BLACK -> board.holdPieceWhite[piece] = board.holdPieceWhite[piece]!! + 1
                WHITE -> board.holdPieceBlack[piece] = board.holdPieceBlack[piece]!! + 1
            }
        }
    }

    // １手戻す
    override fun setBackMove(log: GameLog) {
        val piece = log.beforpiece.degeneration()
        if (board.holdPieceBlack.keys.contains(piece)) {
            when (log.beforturn) {
                BLACK -> board.holdPieceWhite[piece] = board.holdPieceWhite[piece]!! - 1
                WHITE -> board.holdPieceBlack[piece] = board.holdPieceBlack[piece]!! - 1
            }
        }
        when (log.oldY) {
            BLACK_HOLD -> board.holdPieceBlack[log.afterPiece] =
                board.holdPieceBlack[log.afterPiece]!! + 1
            WHITE_HOLD -> board.holdPieceWhite[log.afterPiece] =
                board.holdPieceWhite[log.afterPiece]!! + 1
            else -> {
                board.cells[log.oldX][log.oldY].piece = log.afterPiece
                board.cells[log.oldX][log.oldY].turn = log.afterTurn
            }
        }
        board.cells[log.newX][log.newY].piece = log.beforpiece
        board.cells[log.newX][log.newY].turn = log.beforturn
    }

    //ヒントセット
    override fun setHint(x: Int, y: Int) {
        board.cells[x][y].hint = true
    }

    //ヒントリセット
    override fun resetHint() {
        board.cells.forEach { it.forEach { it.hint = false } }
    }

    //成る
    override fun setEvolution(log: GameLog) {
        board.cells[log.newX][log.newY].piece = log.afterPiece.evolution()
    }

    // endregion

    // region 持ち駒

    //持ち駒リスト取得
    override fun getAllHoldPiece(turn: Int): Map<Piece, Int> {
        return if (turn == BLACK) {
            board.holdPieceBlack
        } else {
            board.holdPieceWhite
        }
    }

    //持ち駒の数を取得
    override fun getCountHoldPiece(turn: Int): Int {
        var count = 0
        for ((_, v) in getAllHoldPiece(turn)) {
            count += v
        }
        return count
    }

    //持ち駒マスから取得
    override fun findHoldPieceBy(i: Int, turn: Int): Piece {
        if (turn == BLACK && board.holdPieceBlack[changeIntToPiece(i)] == 0 ||
            turn == WHITE && board.holdPieceWhite[changeIntToPiece(i)] == 0
        ) return None

        return changeIntToPiece(i)
    }

    //持ち駒台の座標から駒を取得(本当はよくない)
    override fun changeIntToPiece(i: Int): Piece {
        return when (i) {
            2 -> FU
            3 -> KYO
            4 -> KEI
            5 -> GIN
            6 -> KIN
            7 -> KAKU
            8 -> HISYA
            else -> None
        }
    }

    // endregion

    //指定した手番の王様の座標を返す
    override fun findKing(turn: Int): Pair<Int, Int> {
        for (i in 0..8) for (j in 0..8) {
            if ((board.cells[i][j].piece == OU || board.cells[i][j].piece == GYOKU) && board.cells[i][j].turn == turn) {
                return Pair(i, j)
            }
        }
        return Pair(0, 0)
    }
}