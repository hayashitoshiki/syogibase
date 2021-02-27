package com.example.syogibase.presentation.presenter

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
        for (i in 0..8) for (j in 0..8) {
            val cell = useCase.getCellInformation(i, j)
            when (cell.turn) {
                BLACK -> view.drawBlackPiece(cell.piece.nameJP, i, j)
                WHITE -> view.drawWhitePiece(cell.piece.nameJP, i, j)
            }
            if (cell.hint) view.drawHint(i, j)
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
    override fun onTouchEvent(x: Int, y: Int) {
        // 持ち駒
        if (y == 0 || y == 10) useCase.setHintHoldPiece(x, y, useCase.getTurn())
        // 盤上
        else if (x in 0..8 && y in 1..9) {
            when (useCase.getCellTurn(x, y - 1)) {
                HINT -> {
                    useCase.setMove(x, y - 1, false)
                    if (useCase.isEvolution(x, y - 1) && !useCase.isCompulsionEvolution()) {
                        view.showDialog()
                    }
                    // 千日手判定
                    if (useCase.isRepetitionMove()) view.gameEnd(3)
                    // トライルール判定
                    if (useCase.isTryKing()) view.gameEnd(useCase.getTurn())
                    // 王手判定
                    if (useCase.isGameEnd()) view.gameEnd(useCase.getTurn())
                }
                useCase.getTurn() -> useCase.setTouchHint(x, y - 1)
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