package com.example.syogibase.domain

import android.util.Log
import com.example.syogibase.data.entity.*
import com.example.syogibase.data.entity.Piece.*
import com.example.syogibase.util.*

class SyogiLogicUseCaseImp(
    private val board: Board = Board()
) : SyogiLogicUseCase {

    private var turn: Int = BLACK

    private val logList = mutableListOf<GameLog>()
    private var positionList = mutableMapOf<String, Int>()
    private var previousX: Int = 0
    private var previousY: Int = 0
    private var previousPiece: Piece = None
    private var logIndex = 0

    private val blackHoldPiece = HoldPieceStand()
    private val whiteHoldPiece = HoldPieceStand()

    // region アクション

    // 現在の手番を返す
    override fun getTurn(): Int {
        return turn
    }

    // 駒落ち設定
    override fun setHandicap(turn: Int, handicap: Handicap) {
        board.setHandicap(turn, handicap)
    }

    // 指定したマスのヒント探す
    override fun setTouchHint(x: Int, y: Int) {
        board.restHintAll()
        searchHint(x - 1, y - 1, turn)
    }

    // 駒を動かす
    override fun setMove(x: Int, y: Int, evolution: Boolean) {
        var position = ""
        setMove(x - 1, y - 1, turn, evolution)
        board.restHintAll()
        board.cells.forEach {
            it.forEach { cell ->
                position += cell.hint.toString() + cell.piece.toString() + cell.turn.toString()
            }
        }
        if (positionList.containsKey(position)) {
            positionList[position] = positionList[position]!!.toInt() + 1
        } else {
            positionList[position] = 1
        }
        logIndex = logList.size - 1
    }

    // 持ち駒を使う場合
    override fun setHintHoldPiece(x: Int, y: Int, kingTurn: Int) {
        board.restHintAll()
        val piece =
            if (y == WHITE_HOLD && kingTurn == WHITE) {
                whiteHoldPiece.changeIntToPiece(x)
            } else if (y == BLACK_HOLD && kingTurn == BLACK) {
                blackHoldPiece.changeIntToPiece(x)
            } else {
                None
            }
        if (piece == None) return

        when (piece) {
            GIN, KIN, HISYA, KAKU ->
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (board.cells[i][j].turn == null) {
                            setHint(x, y, i, j, kingTurn)
                        }
                    }
                }
            KYO ->
                for (i in 0..8) {
                    for (j in 1..8) {
                        val k = if (kingTurn == BLACK) j else 8 - j
                        if (board.cells[i][k].turn == null) {
                            setHint(x, y, i, k, kingTurn)
                        }
                    }
                }
            KEI ->
                for (i in 0..8) {
                    for (j in 2..8) {
                        val k = if (kingTurn == BLACK) j else 8 - j
                        if (board.cells[i][k].turn == null) {
                            setHint(x, y, i, k, kingTurn)
                        }
                    }
                }
            FU -> {
                val xyList = mutableListOf<Pair<Int, Int>>()
                for (i in 0..8) {
                    for (j in 0..8) {
                        if (board.cells[i][j].turn == kingTurn && board.cells[i][j].piece == FU) break
                        if (j == 8) {
                            for (k in 1..8) {
                                val l = if (y == BLACK_HOLD) k else k - 1
                                if (board.cells[i][l].turn == null && !isCheckMateByPossessionFu(
                                        x,
                                        y,
                                        i,
                                        l,
                                        kingTurn
                                    )
                                ) {
                                    xyList.add(Pair(i, l))
                                }
                            }
                        }
                    }
                }
                xyList.forEach {
                    setHint(x, y, it.first, it.second, kingTurn)
                }
            }
            else -> Log.e("GameLogicPresenter", "不正な持ち駒を取得しようとしています")
        }
    }

    // ヒント取得
    private fun searchHint(touchX: Int, touchY: Int, turn: Int) {
        val moveList: Array<Array<PieceMove>> = board.cells[touchX][touchY].piece.getMove()

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
                if (newX !in 0..8 || newY !in 0..8 || board.cells[newX][newY].turn == turn) {
                    break
                }

                setHint(touchX, touchY, newX, newY, turn)
                if (board.cells[newX][newY].turn != null) break
            }
        }
    }

    // ヒントを設定する
    private fun setHint(x: Int, y: Int, newX: Int, newY: Int, turn: Int) {
        setPre(x, y)
        setMove(newX, newY, turn, false)
        val log: GameLog = logList.last()
        val (kingX: Int, kingY: Int) = findKing(turn)
        if (!isCheck(kingX, kingY, turn)) {
            board.cells[newX][newY].hint = true
        }
        setBackMove(log)
        logList.remove(log)
    }

    //指定した手番の王様の座標を返す
    private fun findKing(turn: Int): Pair<Int, Int> {
        for (i in 0..8) for (j in 0..8) {
            if ((board.cells[i][j].piece == OU || board.cells[i][j].piece == GYOKU) && board.cells[i][j].turn == turn) {
                return Pair(i, j)
            }
        }
        return Pair(0, 0)
    }

    // キャンセル
    override fun cancel() {
        board.restHintAll()
    }

    // endregion

    // region 盤面描画

    // (駒の名前,手番,ヒントの表示)を返す
    override fun getCellInformation(x: Int, y: Int): Cell {
        return board.cells[x][y]
    }

    // マスの手番を返す
    override fun getCellTurn(x: Int, y: Int): Int {
        val cell = board.cells[x - 1][y - 1]
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
        if (turn == BLACK) {
            blackHoldPiece.pieceList.forEach { (piece, count) ->
                hold.add(i, Pair(piece, count))
                i++
            }
        } else {
            whiteHoldPiece.pieceList.forEach { (piece, count) ->
                hold.add(i, Pair(piece, count))
                i++
            }
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
            BLACK -> board.cells[4][0]
            WHITE -> board.cells[4][8]
            else -> null
        }
        return (cell != null && (cell.piece == GYOKU || cell.piece == OU) && cell.turn == turn)
    }

    // 終了判定
    override fun isGameEnd(): GameResult {
        // 千日手判定
        if (isRepetitionMove()) {
            return GameResult.Draw
        }
        // トライルール判定
        if (isTryKing()) {
            return GameResult.Win(getTurn())
        }
        // 詰み判定
        if (isCheckmate()) {
            return GameResult.Win(getTurn())
        }

        // 対局続行
        turn = if (turn == BLACK) WHITE else BLACK
        return GameResult.Continue
    }

    // 王手判定
    private fun isCheck(x: Int, y: Int, turnKing: Int): Boolean {
        // ↑
        for (j in 1..8) {
            val moveY = y - j
            if (moveY < 0) break

            // 判定
            val cellTurn = board.cells[x][moveY].turn
            val cellPiece = board.cells[x][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalUpMovePiece() && turnKing == BLACK) || (cellPiece.equalDownMovePiece() && turnKing == WHITE))) return true
            else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == WHITE && turnKing == BLACK)) return true
            else if (cellTurn != null) break
        }
        // ↓
        for (j in 1..8) {
            val moveY = y + j
            if (9 <= moveY) break

            // 判定
            val cellTurn = board.cells[x][moveY].turn
            val cellPiece = board.cells[x][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDownMovePiece() && turnKing == BLACK) || (cellPiece.equalUpMovePiece() && turnKing == WHITE))) return true
            else if ((cellPiece == HISYA || cellPiece == RYU) || (cellPiece == KYO && cellTurn == BLACK && turnKing == WHITE)) return true
            else if (cellTurn != null) break
        }
        // ←
        for (j in 1..8) {
            val moveX = x - j
            if (moveX < 0) break

            // 判定
            val cellTurn = board.cells[moveX][y].turn
            val cellPiece = board.cells[moveX][y].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && cellPiece.equalLRMovePiece()) return true
            else if (cellPiece.equalLongLRMovePiece()) return true
            else if (cellTurn != null) break
        }
        // →
        for (j in 1..8) {
            val moveX = x + j
            if (9 <= moveX) break

            // 判定
            val cellTurn = board.cells[moveX][y].turn
            val cellPiece = board.cells[moveX][y].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && cellPiece.equalLRMovePiece()) return true
            else if (cellPiece.equalLongLRMovePiece()) return true
            else if (cellTurn != null) break
        }
        // ↖
        for (j in 1..8) {
            val moveX = x - j
            val moveY = y - j
            if (moveX < 0 || moveY < 0) break

            // 判定
            val cellTurn = board.cells[moveX][moveY].turn
            val cellPiece = board.cells[moveX][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != null) break

        }
        // ↙
        for (j in 1..8) {
            val moveX = x - j
            val moveY = y + j
            if (moveX < 0 || 9 <= moveY) break

            // 判定
            val cellTurn = board.cells[moveX][moveY].turn
            val cellPiece = board.cells[moveX][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != null) break
        }
        // ↗
        for (j in 1..8) {
            val moveX = x + j
            val moveY = y - j
            if (9 <= moveX || moveY < 0) break

            // 判定
            val cellTurn = board.cells[moveX][moveY].turn
            val cellPiece = board.cells[moveX][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalUp() && turnKing == BLACK) || (cellPiece.equalDiagonalDown() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != null) break
        }
        // ↘
        for (j in 1..8) {
            val moveX = x + j
            val moveY = y + j
            if (9 <= moveX || 9 <= moveY) break

            // 判定
            val cellTurn = board.cells[moveX][moveY].turn
            val cellPiece = board.cells[moveX][moveY].piece
            if (cellTurn == turnKing) break
            else if (j == 1 && ((cellPiece.equalDiagonalDown() && turnKing == BLACK) || (cellPiece.equalDiagonalUp() && turnKing == WHITE))) return true
            else if (cellPiece == KAKU || cellPiece == UMA) return true
            else if (cellTurn != null) break
        }

        // 桂馬のきき
        val y1 = y - 2
        val y2 = y + 2
        val x1 = x - 1
        val x2 = x + 1
        if (turnKing == BLACK && 0 <= y1) {
            if (0 <= x1 && board.cells[x1][y1].piece == KEI && board.cells[x1][y1].turn == WHITE
            ) return true
            if (x2 < 9 && board.cells[x2][y1].piece == KEI && board.cells[x2][y1].turn == WHITE
            ) return true
        } else if (turnKing == WHITE && y2 < 9) {
            if (0 <= x1 && board.cells[x1][y2].piece == KEI && board.cells[x1][y2].turn == BLACK
            ) return true
            if (x2 < 9 && board.cells[x2][y2].piece == KEI && board.cells[x2][y2].turn == BLACK
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
                if (board.cells[i][j].turn == kingTurn) {
                    searchHint(i, j, kingTurn)
                }
            }
        }
        var count = board.getCountByHint()
        // 持ち駒使う
        getPieceHand(kingTurn).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                val (x, y) =
                    if (kingTurn == BLACK) {
                        Pair(index + 2, BLACK_HOLD)
                    } else {
                        Pair(index + 2, WHITE_HOLD)
                    }
                setHintHoldPiece(x, y, kingTurn)
                count += board.getCountByHint()
            }
        }
        board.restHintAll()
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
        setPre(x, y)
        setMove(newX, newY, turn, false)
        val log: GameLog = logList.last()
        val kingTurn = if (this.turn == BLACK) WHITE else BLACK

        // 逃げる場所 or 防げる駒があるか判定
        for (i in 0..8) {
            for (j in 0..8) {
                if (board.cells[i][j].turn == kingTurn) {
                    searchHint(i, j, kingTurn)
                }
            }
        }
        val count = board.getCountByHint()
        board.restHintAll()
        setBackMove(log)
        logList.remove(log)
        return count == 0
    }

    // 成るか成らないか選択できるか判定
    override fun isSelectEvolution(): Boolean {
        return isEvolution() && !isCompulsionEvolution()
    }

    // 成り判定
    override fun isEvolution(): Boolean {
        val oldY = logList.last().oldY + 1
        val newX = logList.last().newX + 1
        val newY = logList.last().newY + 1
        return (oldY in 1..9 && board.cells[newX - 1][newY - 1].piece.findEvolution()) &&
                ((turn == BLACK && (newY <= 3 || oldY <= 3)) || (turn == WHITE && (7 <= newY || 7 <= oldY)))
    }

    // 強制的にならないといけないか判定
    override fun isCompulsionEvolution(): Boolean {
        val log: GameLog = logList.last()
        return when (log.afterPiece) {
            FU, HISYA, KAKU -> {
                setEvolution()
                true
            }
            KYO, KEI -> {
                if ((log.newY <= 1 && log.afterTurn == BLACK) || (7 <= log.newY && log.afterTurn == WHITE)) {
                    setEvolution()
                    true
                } else {
                    false
                }
            }
            else -> false
        }
    }

    // 成り
    override fun setEvolution() {
        val log: GameLog = logList.last()
        logList.last().evolution = true
        board.cells[log.newX][log.newY].setPiece(log.afterPiece.evolution(), log.afterTurn)
    }

    // endregion

    // region 棋譜保存

    // 一手進む
    override fun setGoMove() {
        if (logIndex < (logList.size - 1)) {
            logIndex += 1
            val log = logList[logIndex]
            setGoMove(log)
        }
    }

    // 一手戻す
    override fun setBackMove() {
        if (0 <= logIndex) {
            val log = logList[logIndex]
            setBackMove(log)
            logIndex -= 1
        }
    }

    // 最後まで進む
    override fun setGoLastMove() {
        while (logIndex < (logList.size - 1)) {
            setGoMove()
        }
    }

    // 最初まで戻る
    override fun setBackFirstMove() {
        while (0 <= logIndex) {
            setBackMove()
        }
    }

    // 動かす前の駒の状態をセット
    private fun setPre(x: Int, y: Int) {
        previousX = x
        previousY = y
        previousPiece =
            when (y) {
                BLACK_HOLD, WHITE_HOLD -> changeIntToPiece(x)
                else -> board.cells[previousX][previousY].piece
            }
    }

    // 駒を動かす
    private fun setMove(x: Int, y: Int, turn: Int, evolution: Boolean) {
        val newCell = board.cells[x][y]
        val gameLog = GameLog(
            previousX,
            previousY,
            previousPiece,
            turn,
            x,
            y,
            newCell.piece,
            newCell.turn,
            evolution
        )
        logList.add(gameLog)
        setGoMove(gameLog)
    }

    // リセットする
    override fun reset() {
        turn = BLACK
        logList.removeAll(logList)
        setBoard(Board().cells)
    }

    //棋譜設定
    override fun setGameLog(logList: List<GameLog>) {
        this.logList.addAll(logList)
    }

    // 棋譜取得
    override fun getGameLog(): List<GameLog> {
        return logList
    }

    // 盤面を初期化する
    override fun resetBoard() {
        board.setBoard(Board().cells)
        blackHoldPiece.reset()
        whiteHoldPiece.reset()
    }

    // 指定した盤面設定
    override fun setBoard(customBoard: Array<Array<Cell>>) {
        board.setBoard(customBoard)
    }

    // 持ち駒台設定
    override fun setHoldPiece(holdPiece: MutableMap<Piece, Int>, turn: Int) {
        when (turn) {
            BLACK -> blackHoldPiece.setCustomStand(holdPiece)
            WHITE -> whiteHoldPiece.setCustomStand(holdPiece)
        }
    }

    // １手戻す
    private fun setBackMove(log: GameLog) {
        val piece = log.beforpiece.degeneration()
        when (log.beforturn) {
            WHITE -> blackHoldPiece.remove(piece)
            BLACK -> whiteHoldPiece.remove(piece)
        }
        when (log.oldY) {
            BLACK_HOLD -> blackHoldPiece.add(log.afterPiece)
            WHITE_HOLD -> whiteHoldPiece.add(log.afterPiece)
            else -> board.cells[log.oldX][log.oldY].setPiece(log.afterPiece, log.afterTurn)
        }
        if (log.beforturn == null) {
            board.cells[log.newX][log.newY].setNone()
        } else {
            board.cells[log.newX][log.newY].setPiece(log.beforpiece, log.beforturn)
        }
    }

    // 駒を動かす
    private fun setGoMove(log: GameLog) {
        // 動かした先の設定
        val movePiece = if (log.evolution) log.afterPiece.evolution() else log.afterPiece
        val turn = log.afterTurn
        board.cells[log.newX][log.newY].setPiece(movePiece, turn)
        // 持ち駒消費 or 前のマスを空にする
        when (log.oldY) {
            BLACK_HOLD -> blackHoldPiece.remove(log.afterPiece)
            WHITE_HOLD -> whiteHoldPiece.remove(log.afterPiece)
            else -> board.cells[log.oldX][log.oldY].clear()
        }
        // 持ち駒追加
        val piece = log.beforpiece.degeneration()
        when (log.beforturn) {
            WHITE -> blackHoldPiece.add(piece)
            BLACK -> whiteHoldPiece.add(piece)
        }
    }

    //持ち駒台の座標から駒を取得(本当はよくない)
    private fun changeIntToPiece(i: Int): Piece {
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
}
