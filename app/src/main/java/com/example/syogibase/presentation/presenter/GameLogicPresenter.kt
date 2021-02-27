package com.example.syogibase.presentation.presenter

import com.example.syogibase.data.local.Board.Companion.COLS
import com.example.syogibase.data.local.Board.Companion.ROWS
import com.example.syogibase.domain.SyogiLogicUseCase
import com.example.syogibase.presentation.contact.GameViewContact
import com.example.syogibase.util.BLACK
import com.example.syogibase.util.HINT
import com.example.syogibase.util.WHITE

class GameLogicPresenter(
    private val view: GameViewContact.View,
    private val useCase: SyogiLogicUseCase
) : GameViewContact.Presenter {

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
        // 持ち駒
        useCase.getPieceHand(BLACK).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                view.drawHoldPieceBlack(piece.first.nameJP, piece.second, index)
            }
        }
        useCase.getPieceHand(WHITE).forEachIndexed { index, piece ->
            if (piece.second != 0) {
                view.drawHoldPieceWhite(piece.first.nameJP, piece.second, index)
            }
        }
    }

    // タッチ判定
    override fun onTouchEvent(touchX: Int, touchY: Int) {
        // 持ち駒
        if (touchY == 0 || touchY == 10) {
            var y = 0
            var x = 0
            when (useCase.getTurn()) {
                BLACK -> {
                    x = touchX
                    y = touchY
                }
                WHITE -> {
                    x = 8 - touchX
                    y = -1
                }
            }
            useCase.setHintHoldPiece(x, y, useCase.getTurn())
        }
        // 盤上
        else if (touchX in 0..8 && touchY in 1..9) {
            val x = 9 - touchX
            val y = touchY
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
        // 盤外
        else useCase.cancel()
    }

    // 成り判定
    override fun evolutionPiece() {
        useCase.setEvolution()
    }
}