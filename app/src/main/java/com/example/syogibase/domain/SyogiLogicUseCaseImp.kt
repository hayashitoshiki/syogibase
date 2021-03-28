package com.example.syogibase.domain

import android.util.Log
import com.example.syogibase.data.entity.*
import com.example.syogibase.data.entity.Piece.*
import com.example.syogibase.data.value.*
import com.example.syogibase.data.value.Turn.BLACK
import com.example.syogibase.data.value.Turn.WHITE

class SyogiLogicUseCaseImp(
    private val board: Board = Board()
) : SyogiLogicUseCase {

    private var turn: Turn = BLACK

    private val logList = mutableListOf<GameLog>()
    private var positionList = mutableMapOf<String, Int>()
    private var previousX: Int = 0
    private var previousY: Int = 0
    private var previousPiece: Piece? = null
    private var logIndex = 0

    private val blackHoldPiece = HoldPieceStand()
    private val whiteHoldPiece = HoldPieceStand()

    // region アクション

    // 現在の手番を返す
    override fun getTurn(): Turn {
        return turn
    }

    // 駒落ち設定
    override fun setHandicap(turn: Turn, handicap: Handicap) {
        board.setHandicap(turn, handicap)
    }

    // 指定したマスのヒント探す
    override fun setTouchHint(x: Int, y: Int) {
        board.restHintAll()
        searchHint(x, y, turn)
    }

    // 駒を動かす
    override fun setMove(x: Int, y: Int, evolution: Boolean) {
        var position = ""
        previousPiece ?: return
        setMove(x, y, turn, evolution)
        board.restHintAll()
        board.getCells().forEach {
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
    override fun setHintHoldPiece(piece: Piece, turn: Turn) {
        board.restHintAll()
        when (turn) {
            BLACK -> blackHoldPiece.getStandPiece(piece) ?: return
            WHITE -> whiteHoldPiece.getStandPiece(piece) ?: return
        }

        when (piece) {
            GIN, KIN, HISYA, KAKU ->
                for (x in 1..9) {
                    for (y in 1..9) {
                        if (board.getCell(x, y).turn == null) {
                            setHint(0, 0, piece, x, y, turn)
                        }
                    }
                }
            KYO ->
                for (x in 1..9) {
                    for (y in 1..9) {
                        if (turn == BLACK && y == 1) continue
                        if (turn == WHITE && y == 9) break
                        if (board.getCell(x, y).turn == null) {
                            setHint(0, 0, piece, x, y, turn)
                        }
                    }
                }
            KEI ->
                for (x in 1..9) {
                    for (y in 1..9) {
                        if (turn == BLACK && y <= 2) continue
                        if (turn == WHITE && 8 <= y) break
                        if (board.getCell(x, y).turn == null) {
                            setHint(0, 0, piece, x, y, turn)
                        }
                    }
                }
            FU -> {
                val xyList = mutableListOf<Pair<Int, Int>>()
                for (x in 1..9) {
                    for (j in 1..9) {
                        val cell = board.getCell(x, j)
                        if (cell.turn == turn && cell.piece == FU) break
                        if (j == 9) {
                            for (y in 1..9) {
                                if (turn == BLACK && y == 1) continue
                                if (turn == WHITE && y == 9) break
                                if (board.getCell(
                                        x,
                                        y
                                    ).turn == null && !isCheckMateByPossessionFu(x, y, turn)
                                ) {
                                    xyList.add(Pair(x, y))
                                }
                            }
                        }
                    }
                }
                xyList.forEach {
                    setHint(0, 0, piece, it.first, it.second, turn)
                }
            }
            else -> Log.e("GameLogicPresenter", "不正な持ち駒を取得しようとしています")
        }
    }

    // ヒント取得
    private fun searchHint(touchX: Int, touchY: Int, turn: Turn) {
        val moveList: Array<Array<PieceMove>> =
            board.getCell(touchX, touchY).piece?.getMove() ?: return

        for (moveDirection in moveList) {
            for (move in moveDirection) {
                var toX = 0
                var toY = 0
                when (turn) {
                    BLACK -> {
                        toX = touchX + move.x
                        toY = touchY + move.y
                    }
                    WHITE -> {
                        toX = touchX - move.x
                        toY = touchY - move.y
                    }
                }
                // 範囲外か自分のコマとかぶったらその方向の検索はストップ
                if (toX !in 1..9 || toY !in 1..9 || board.getCell(toX, toY).turn == turn) {
                    break
                }
                val piece = board.getCell(touchX, touchY).piece!!
                setHint(touchX, touchY, piece, toX, toY, turn)
                if (board.getCell(toX, toY).turn != null) break
            }
        }
    }

    // ヒントを設定する
    private fun setHint(x: Int, y: Int, piece: Piece, toX: Int, toY: Int, turn: Turn) {
        setPre(x, y, piece)
        previousPiece ?: return
        setMove(toX, toY, turn, false)
        val log: GameLog = logList.last()
        findKing(turn)?.let {
            if (!isCheck(it.first, it.second, turn)) {
                board.setHint(toX, toY, true)
            }
        }
        setBackMove(log)
        logList.remove(log)
    }

    //指定した手番の王様の座標を返す
    private fun findKing(turn: Turn): Pair<Int, Int>? {
        for (x in 1..9) for (y in 1..9) {
            val cell = board.getCell(x, y)
            if ((cell.piece == OU || cell.piece == GYOKU) && cell.turn == turn) {
                return Pair(x, y)
            }
        }
        return null
    }

    // キャンセル
    override fun cancel() {
        board.restHintAll()
    }

    // endregion

    // region 盤面描画

    // (駒の名前,手番,ヒントの表示)を返す
    override fun getCellInformation(x: Int, y: Int): Cell {
        return board.getCell(x, y)
    }

    // 指定した手番の持ち駒を返す
    override fun getPieceHand(turn: Turn): Map<Piece, Int> {
        return when (turn) {
            BLACK -> blackHoldPiece.pieceList
            WHITE -> whiteHoldPiece.pieceList
        }
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
            BLACK -> board.getCell(5, 1)
            WHITE -> board.getCell(5, 9)
        }
        return ((cell.piece == GYOKU || cell.piece == OU) && cell.turn == turn)
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
    private fun isCheck(x: Int, y: Int, turnKing: Turn): Boolean {
        // ↑
        loop@ for (j in 1..8) {
            val moveY = y - j
            if (moveY < 1) break

            // 判定
            val cellTurn = board.getCell(x, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(x, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isUpMovePiece() && turnKing == BLACK) || (cellPiece.isDownMovePiece() && turnKing == WHITE))) return true
            if ((cellPiece.isLongDownMovePiece() && cellTurn == WHITE) || (cellPiece.isLongUpMovePiece() && turnKing == BLACK)) return true
            break
        }
        // ↓
        loop@ for (j in 1..8) {
            val moveY = y + j
            if (9 < moveY) break

            // 判定
            val cellTurn = board.getCell(x, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(x, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isDownMovePiece() && turnKing == BLACK) || (cellPiece.isUpMovePiece() && turnKing == WHITE))) return true
            if ((cellPiece.isLongDownMovePiece() && cellTurn == BLACK) || (cellPiece.isLongUpMovePiece() && turnKing == WHITE)) return true
            break
        }
        // ←
        loop@ for (j in 1..8) {
            val moveX = x - j
            if (moveX < 1) break

            // 判定
            val cellTurn = board.getCell(moveX, y).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, y).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && cellPiece.isLRMovePiece()) return true
            if (cellPiece.isLongLRMovePiece()) return true
            break
        }
        // →
        loop@ for (j in 1..8) {
            val moveX = x + j
            if (9 < moveX) break

            // 判定
            val cellTurn = board.getCell(moveX, y).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, y).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && cellPiece.isLRMovePiece()) return true
            if (cellPiece.isLongLRMovePiece()) return true
            break
        }
        // ↖
        loop@ for (j in 1..8) {
            val moveX = x - j
            val moveY = y - j
            if (moveX < 1 || moveY < 1) break

            // 判定
            val cellTurn = board.getCell(moveX, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isDiagonalUp() && turnKing == BLACK) || (cellPiece.isDiagonalDown() && turnKing == WHITE))) return true
            if (cellPiece.isLongDiagonal()) return true
            break
        }
        // ↙
        loop@ for (j in 1..8) {
            val moveX = x - j
            val moveY = y + j
            if (moveX < 1 || 9 < moveY) break

            // 判定
            val cellTurn = board.getCell(moveX, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isDiagonalDown() && turnKing == BLACK) || (cellPiece.isDiagonalUp() && turnKing == WHITE))) return true
            if (cellPiece.isLongDiagonal()) return true
            break
        }
        // ↗
        loop@ for (j in 1..8) {
            val moveX = x + j
            val moveY = y - j
            if (moveY < 1 || 9 < moveX) break

            // 判定
            val cellTurn = board.getCell(moveX, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isDiagonalUp() && turnKing == BLACK) || (cellPiece.isDiagonalDown() && turnKing == WHITE))) return true
            if (cellPiece.isLongDiagonal()) return true
            break
        }
        // ↘
        loop@ for (j in 1..8) {
            val moveX = x + j
            val moveY = y + j
            if (9 < moveX || 9 < moveY) break

            // 判定
            val cellTurn = board.getCell(moveX, moveY).turn ?: continue@loop
            val cellPiece = board.getCell(moveX, moveY).piece ?: continue@loop
            if (cellTurn == turnKing) break
            if (j == 1 && ((cellPiece.isDiagonalDown() && turnKing == BLACK) || (cellPiece.isDiagonalUp() && turnKing == WHITE))) return true
            if (cellPiece.isLongDiagonal()) return true
            break
        }

        // 桂馬のきき
        val y1 = y - 2
        val y2 = y + 2
        val x1 = x - 1
        val x2 = x + 1
        if (turnKing == BLACK && 1 <= y1) {
            if (1 <= x1) {
                val rightCell = board.getCell(x1, y1)
                if (rightCell.piece == KEI && rightCell.turn == WHITE) return true
            }
            if (x2 <= 9) {
                val leftCell = board.getCell(x2, y1)
                if (leftCell.piece == KEI && leftCell.turn == WHITE) return true
            }
        } else if (turnKing == WHITE && y2 <= 9) {
            if (1 <= x1) {
                val rightCell = board.getCell(x1, y2)
                if (rightCell.piece == KEI && rightCell.turn == BLACK) return true
            }
            if (x2 <= 9) {
                val leftCell = board.getCell(x2, y2)
                if (leftCell.piece == KEI && leftCell.turn == BLACK) return true
            }
        }

        // 王手がなかったらfalseを返す
        return false
    }

    // 逃げる場所判定
    private fun isCheckmate(): Boolean {
        val kingTurn = if (this.turn == BLACK) WHITE else BLACK

        // 逃げる場所 or 防げる駒があるか判定
        for (i in 1..9) {
            for (j in 1..9) {
                if (board.getCell(i, j).turn == kingTurn) {
                    searchHint(i, j, kingTurn)
                }
            }
        }
        var count = board.getCountByHint()
        // 持ち駒使う
        getPieceHand(kingTurn).map {
            if (it.value != 0) {
                setHintHoldPiece(it.key, kingTurn)
                count += board.getCountByHint()
            }
        }
        board.restHintAll()
        return count == 0
    }

    // 打ち歩詰め判定
    private fun isCheckMateByPossessionFu(
        toX: Int,
        toY: Int,
        turn: Turn
    ): Boolean {
        setPre(0, 0, FU)
        previousPiece ?: return false
        setMove(toX, toY, turn, false)
        val log: GameLog = logList.last()
        val kingTurn = if (this.turn == BLACK) WHITE else BLACK

        // 逃げる場所 or 防げる駒があるか判定
        for (i in 1..9) {
            for (j in 1..9) {
                if (board.getCell(i, j).turn == kingTurn) {
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
        val fromY = logList.last().fromY.value
        val toX = logList.last().toX.value
        val toY = logList.last().toY.value
        val isEvolution: Boolean = board.getCell(toX, toY).piece?.isEvolution() ?: return false
        return when (turn) {
            BLACK -> (toY <= 3 || fromY <= 3) && (toY in 1..9 && fromY in 1..9) && isEvolution
            WHITE -> (7 <= toY || 7 <= fromY) && (toY in 1..9 && fromY in 1..9) && isEvolution
        }
    }

    // 強制的にならないといけないか判定
    override fun isCompulsionEvolution(): Boolean {
        val log: GameLog = logList.last()
        return when (log.piece) {
            FU, HISYA, KAKU -> {
                setEvolution()
                true
            }
            KYO, KEI -> {
                if ((log.toY.value <= 1 && log.turn == BLACK) || (7 <= log.toY.value && log.turn == WHITE)) {
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
        board.getCell(log.toX.value, log.toY.value).setPiece(log.piece.evolution(), log.turn)
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
    private fun setPre(x: Int, y: Int, piece: Piece) {
        previousX = x
        previousY = y
        previousPiece = piece
    }

    // 駒を動かす
    private fun setMove(x: Int, y: Int, turn: Turn, evolution: Boolean) {
        val newCell = board.getCell(x, y)
        val gameLog = GameLog(
            X(previousX),
            Y(previousY),
            previousPiece!!,
            turn,
            X(x),
            Y(y),
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
        blackHoldPiece.reset()
        whiteHoldPiece.reset()
        board.clear()
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
        turn = BLACK
        board.setBoard(Board().getCells())
        blackHoldPiece.reset()
        whiteHoldPiece.reset()
    }

    // 指定した盤面設定
    override fun setBoard(customBoard: Array<Array<Cell>>) {
        board.setBoard(customBoard)
    }

    // 持ち駒台設定
    override fun setHoldPiece(holdPiece: MutableMap<Piece, Int>, turn: Turn) {
        when (turn) {
            BLACK -> blackHoldPiece.setCustomStand(holdPiece)
            WHITE -> whiteHoldPiece.setCustomStand(holdPiece)
        }
    }

    // １手戻す
    private fun setBackMove(log: GameLog) {
        // 元のマスの状態に戻す
        if (log.stealTurn == null || log.stealPiece == null) {
            board.getCell(log.toX.value, log.toY.value).setNone()
        } else {
            board.getCell(log.toX.value, log.toY.value).setPiece(log.stealPiece, log.stealTurn)
        }
        // 動かす前のマスに戻す
        if (log.fromX.value == 0 || log.fromY.value == 0) {
            when (log.turn) {
                BLACK -> blackHoldPiece.add(log.piece)
                WHITE -> whiteHoldPiece.add(log.piece)
            }
        } else {
            board.getCell(log.fromX.value, log.fromY.value).setPiece(log.piece, log.turn)
        }
        // 持ち駒削除
        val piece = log.stealPiece?.degeneration() ?: return
        when (log.stealTurn) {
            WHITE -> blackHoldPiece.remove(piece)
            BLACK -> whiteHoldPiece.remove(piece)
        }
    }

    // 駒を動かす
    private fun setGoMove(log: GameLog) {
        // 動かした先の設定
        val movePiece = when {
            log.fromX.value == 0 || log.fromY.value == 0 -> log.piece
            log.evolution -> log.piece.evolution()
            else -> log.piece
        }
        board.getCell(log.toX.value, log.toY.value).setPiece(movePiece, log.turn)
        // 持ち駒消費 or 前のマスを空にする
        if (log.fromX.value == 0 || log.fromY.value == 0) {
            when (log.turn) {
                BLACK -> blackHoldPiece.remove(log.piece)
                WHITE -> whiteHoldPiece.remove(log.piece)
            }
        } else {
            board.getCell(log.fromX.value, log.fromY.value).clear()
        }
        // 持ち駒追加
        val piece = log.stealPiece?.degeneration() ?: return
        when (log.stealTurn) {
            WHITE -> blackHoldPiece.add(piece)
            BLACK -> whiteHoldPiece.add(piece)
        }
    }

    // endregion
}
