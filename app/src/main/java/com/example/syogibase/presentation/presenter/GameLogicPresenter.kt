package com.example.syogibase.presentation.presenter

import com.example.syogibase.data.entity.Board.Companion.COLS
import com.example.syogibase.data.entity.Board.Companion.ROWS
import com.example.syogibase.data.entity.GameLog
import com.example.syogibase.data.entity.GameResult
import com.example.syogibase.domain.SyogiLogicUseCase
import com.example.syogibase.presentation.contact.GameViewContact
import com.example.syogibase.util.*

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
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            when (cell.turn) {
                BLACK -> view.drawBlackPiece(cell.piece.nameJP, (COLS - 1) - i, j)
                WHITE -> view.drawWhitePiece(cell.piece.nameJP, (COLS - 1) - i, j)
            }
            if (cell.hint && isShowHint) view.drawHint((COLS - 1) - i, j)
        }
        // 駒台
        if (isVerticalStand) {
            view.drawVerticalStand()
        }
        if (isHorizontalStand) {
            view.drawHorizontalStand()
        }
        // 持ち駒
        useCase.getPieceHand(BLACK).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                if (isHorizontalStand) {
                    val x = index + 2
                    val y = 10
                    view.drawHoldPieceBlack(piece.first.nameJP, piece.second, x, y)
                } else if (isVerticalStand) {
                    val x = 10
                    val y = index + 2
                    view.drawHoldPieceBlack(piece.first.nameJP, piece.second, x, y)
                }

            }
        }
        useCase.getPieceHand(WHITE).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                if (isHorizontalStand) {
                    val x = 6 - index
                    val y = 0
                    view.drawHoldPieceWhite(piece.first.nameJP, piece.second, x, y)
                } else if (isVerticalStand) {
                    val x = 0
                    val y = 6 - index
                    view.drawHoldPieceWhite(piece.first.nameJP, piece.second, x, y)
                }
            }
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
                if (touchX == 0 && touchY in 0..6) {
                    useCase.setHintHoldPiece(8 - touchY, WHITE_HOLD, useCase.getTurn())
                } else if (touchX == 10 && touchY in 2..8) {
                    useCase.setHintHoldPiece(touchY, BLACK_HOLD, useCase.getTurn())
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
                if (touchY == 0 && touchX in 0..6) {
                    useCase.setHintHoldPiece(8 - touchX, WHITE_HOLD, useCase.getTurn())
                } else if (touchY == 10 && touchX in 2..8) {
                    useCase.setHintHoldPiece(touchX, BLACK_HOLD, useCase.getTurn())
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
        when (useCase.getCellTurn(x, y)) {
            HINT -> {
                useCase.setMove(x, y, false)
                if (isMoveSound) view.playbackEffect()
                if (useCase.isSelectEvolution()) {
                    view.showDialog()
                } else {
                    checkGameEnd()
                }
            }
            useCase.getTurn() -> useCase.setTouchHint(x, y)
            else -> useCase.cancel()
        }
    }

    // 終了判定
    override fun checkGameEnd() {
        when (val result = useCase.isGameEnd()) {
            is GameResult.Draw -> view.gameEnd(0)
            is GameResult.Win -> view.gameEnd(result.winner)
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
        useCase.reset()
    }

    // ハンデ設定
    override fun setHandicap(turn: Int, handicap: Handicap) {
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