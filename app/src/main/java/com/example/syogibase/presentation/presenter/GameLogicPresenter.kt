package com.example.syogibase.presentation.presenter

import com.example.syogibase.domain.model.Board.Companion.COLS
import com.example.syogibase.domain.model.Board.Companion.ROWS
import com.example.syogibase.domain.model.GameLog
import com.example.syogibase.domain.model.Piece
import com.example.syogibase.domain.usecase.SyogiLogicUseCase
import com.example.syogibase.domain.value.BoardMode
import com.example.syogibase.domain.value.GameResult
import com.example.syogibase.domain.value.Handicap
import com.example.syogibase.domain.value.Turn
import com.example.syogibase.domain.value.Turn.BLACK
import com.example.syogibase.domain.value.Turn.WHITE
import com.example.syogibase.presentation.contact.GameViewContact

class GameLogicPresenter(
    private val view: GameViewContact.View,
    private val useCase: SyogiLogicUseCase
) : GameViewContact.Presenter {


    // 有効設定
    private var isVerticalStand = false
    private var isHorizontalStand = false
    private var isMoveSound = true
    private var isShowHint = true

    // マスのサイズ計算
    override fun computeCellSize(width: Int, height: Int) {
        if (width < height) {
            isVerticalStand = false
            isHorizontalStand = true
            if (width / COLS < height / (COLS + 2)) {
                view.setCellSize((width / COLS).toFloat())
            } else {
                view.setCellSize((height / (COLS + 2)).toFloat())
            }
        } else {
            isVerticalStand = true
            isHorizontalStand = false
            if (width / (COLS + 2) > height / COLS) {
                view.setCellSize((height / COLS).toFloat())
            } else {
                view.setCellSize((width / (COLS + 2)).toFloat())
            }
        }
    }

    // 将棋描画
    override fun drawView() {
        view.drawBoard()
        // 盤上
        for (i in 1..COLS) for (j in 1..ROWS) {
            val cell = useCase.getCellInformation(i, j)
            cell.piece?.let { piece ->
                when (cell.turn) {
                    BLACK -> view.drawBlackPiece(piece.nameJP, COLS - i, j - 1)
                    WHITE -> view.drawWhitePiece(piece.nameJP, COLS - i, j - 1)
                }
            }
            if (cell.hint && isShowHint) view.drawHint(COLS - i, j - 1)
        }
        // 駒台
        if (isVerticalStand) {
            view.drawVerticalStand()
        }
        if (isHorizontalStand) {
            view.drawHorizontalStand()
        }
        // 持ち駒
        var blackIndex = 0
        useCase.getPieceHand(BLACK).map {
            if (it.value != 0) {
                if (isHorizontalStand) {
                    val x = blackIndex + 2
                    val y = 10
                    view.drawHoldPieceBlack(it.key.nameJP, it.value, x, y)
                } else if (isVerticalStand) {
                    val x = 10
                    val y = blackIndex + 2
                    view.drawHoldPieceBlack(it.key.nameJP, it.value, x, y)
                }
            }
            blackIndex++
        }
        var whiteIndex = 0
        useCase.getPieceHand(WHITE).map {
            if (it.value != 0) {
                if (isHorizontalStand) {
                    val x = 6 - whiteIndex
                    val y = 0
                    view.drawHoldPieceWhite(it.key.nameJP, it.value, x, y)
                } else if (isVerticalStand) {
                    val x = 0
                    val y = 6 - whiteIndex
                    view.drawHoldPieceWhite(it.key.nameJP, it.value, x, y)
                }
            }
            whiteIndex++
        }
    }

    // タッチイベントのロジック
    override fun onTouchEventLogic(x: Int, y: Int, mode: BoardMode) {
        when (mode) {
            BoardMode.GAME -> {
                view.onTouchEventByGameMode(x, y)
            }
            BoardMode.REPLAY -> {
                view.onTouchEventByReplayMode(x, y)
            }
        }
    }

    // 対局モード
    override fun onTouchEventByGameMode(touchX: Int, touchY: Int) {
        when {
            // 左右に駒台が存在
            isVerticalStand && !isHorizontalStand -> {
                // 持ち駒
                val turn = useCase.getTurn()
                if (touchX == 0 && turn == WHITE) {
                    changePieceByIndex(8 - touchY)?.let { piece ->
                        useCase.setHintHoldPiece(piece, turn)
                    } ?: run {
                        useCase.cancel()
                    }
                } else if (touchX == 10 && turn == BLACK) {
                    changePieceByIndex(touchY)?.let { piece ->
                        useCase.setHintHoldPiece(piece, turn)
                    } ?: run {
                        useCase.cancel()
                    }
                }
                // 盤上
                else if (touchX in 1..9 && touchY in 0..8) {
                    val x = 10 - touchX
                    val y = touchY + 1
                    setBoardTouch(x, y)
                }
                // 盤外
                else useCase.cancel()
            }
            // 上下に駒台が存在
            !isVerticalStand && isHorizontalStand -> {
                // 持ち駒
                val turn = useCase.getTurn()
                if (touchY == 0 && turn == WHITE) {
                    changePieceByIndex(8 - touchX)?.let { piece ->
                        useCase.setHintHoldPiece(piece, turn)
                    } ?: run {
                        useCase.cancel()
                    }
                } else if (touchY == 10 && turn == BLACK) {
                    changePieceByIndex(touchX)?.let { piece ->
                        useCase.setHintHoldPiece(piece, turn)
                    } ?: run {
                        useCase.cancel()
                    }
                }
                // 盤上
                else if (touchX in 0..8 && touchY in 1..9) {
                    val x = 9 - touchX
                    val y = touchY
                    setBoardTouch(x, y)
                }
                // 盤外
                else useCase.cancel()
            }
        }
    }

    // 盤面タッチイベント
    private fun setBoardTouch(x: Int, y: Int) {
        val cell = useCase.getCellInformation(x, y)
        when {
            cell.hint -> {
                useCase.setMove(x, y, false)
                if (isMoveSound) view.playbackEffect()
                if (useCase.isSelectEvolution()) {
                    view.showDialog()
                } else {
                    checkGameEnd()
                }
            }
            cell.turn == useCase.getTurn() -> useCase.setTouchHint(x, y)
            else -> useCase.cancel()
        }
    }

    // 終了判定
    override fun checkGameEnd() {
        when (val result = useCase.isGameEnd()) {
            is GameResult.Draw -> view.gameEnd(null)
            is GameResult.Win -> view.gameEnd(result.winner)
        }
    }

    // 座標から持ち駒へ変換
    private fun changePieceByIndex(index: Int): Piece? {
        return when (index) {
            2 -> Piece.FU
            3 -> Piece.KYO
            4 -> Piece.KEI
            5 -> Piece.GIN
            6 -> Piece.KIN
            7 -> Piece.KAKU
            8 -> Piece.HISYA
            else -> null
        }
    }


    // 感想戦モード

    // 感想戦のタッチダウンイベント処理のロジック
    override fun onTouchDownEventByReplayModeLogic(x: Int, center: Int) {
        if (x < center) {
            view.setLongJobByBack()
        } else {
            view.setLongJobByGo()
        }
    }

    // 感想戦のタッチアップイベント処理のロジック
    override fun onTouchUpEventByReplayModeLogic(x: Int, center: Int) {
        if (x < center) {
            setBackMove()
        } else {
            setGoMove()
        }
        view.cancelLongJob()
    }


    // １手戻る
    override fun setBackMove() {
        useCase.setBackMove()
    }

    // 最初まで戻る
    override fun setBackFirstMove() {
        useCase.setBackFirstMove()
    }

    // １手進む
    override fun setGoMove() {
        useCase.setGoMove()
    }

    // 最後まで進む
    override fun setGoLastMove() {
        useCase.setGoLastMove()
    }

    // 成り
    override fun evolutionPiece() {
        useCase.setEvolution()
    }

    // リセット
    override fun reset() {
        useCase.resetBoard()
    }

    // ハンデ設定
    override fun setHandicap(turn: Turn, handicap: Handicap) {
        useCase.setHandicap(turn, handicap)
    }

    // 駒音有効設定
    override fun setEnableTouchSound(enable: Boolean) {
        isMoveSound = enable
    }

    // ヒントの表示設定
    override fun setEnableHint(enable: Boolean) {
        isShowHint = enable
    }

    // 棋譜設定
    override fun setGameLog(logList: List<GameLog>) {
        useCase.setGameLog(logList)
    }

    // 棋譜取得
    override fun getGameLog(): List<GameLog> {
        return useCase.getGameLog()
    }
}