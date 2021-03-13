package com.example.syogibase.presentation.presenter

import com.example.syogibase.data.local.Board.Companion.COLS
import com.example.syogibase.data.local.Board.Companion.ROWS
import com.example.syogibase.domain.SyogiLogicUseCase
import com.example.syogibase.presentation.contact.GameViewContact
import com.example.syogibase.util.*

class GameLogicPresenter(
    private val view: GameViewContact.View,
    private val useCase: SyogiLogicUseCase
) : GameViewContact.Presenter {


    private var isVerticalStand = false
    private var isHorizontalStand = false

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
            if (cell.hint) view.drawHint((COLS - 1) - i, j)
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

    // 対局モード
    override fun onTouchEvent(touchX: Int, touchY: Int) {
        when {
            // 上下に駒台が存在
            isVerticalStand && !isHorizontalStand -> {
                // 持ち駒
                if (touchX == 0 || touchX == 10) {
                    setStandTouch(touchY)
                }
                // 盤上
                else if (touchX in 1..9 && touchY in 0..8) {
                    val x = 9 - touchX + 1
                    val y = touchY + 1
                    setBoardTouch(x, y)
                }
                // 盤外
                else useCase.cancel()
            }
            // 左右に駒台が存在
            !isVerticalStand && isHorizontalStand -> {
                // 持ち駒
                if (touchY == 0 || touchY == 10) {
                    setStandTouch(touchX)
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
                if (useCase.isEvolution(x, y) && !useCase.isCompulsionEvolution()) {
                    view.showDialog()
                }
                // 千日手判定
                if (useCase.isRepetitionMove()) view.gameEnd(3)
                // トライルール判定
                if (useCase.isTryKing()) view.gameEnd(useCase.getTurn())
                // 王手判定
                if (useCase.isGameEnd()) view.gameEnd(useCase.getTurn())
            }
            useCase.getTurn() -> useCase.setTouchHint(x, y)
            else -> useCase.cancel()
        }
    }

    // 駒台タッチイベント
    private fun setStandTouch(index: Int) {
        var y = 0
        var x = 0
        when (useCase.getTurn()) {
            BLACK -> {
                x = index
                y = BLACK_HOLD
            }
            WHITE -> {
                x = 8 - index
                y = WHITE_HOLD
            }
        }
        useCase.setHintHoldPiece(x, y, useCase.getTurn())
    }


    // 感想戦モード

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

    // 成り判定
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
}