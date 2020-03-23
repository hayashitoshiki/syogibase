package com.example.syogibase.presentation.presenter

import com.example.syogibase.presentation.contact.GameViewContact
import com.example.syogibase.domain.SyogiLogicUseCase
import com.example.syogibase.util.*

class GameLogicPresenter(private val view: GameViewContact.View,private val useCase:SyogiLogicUseCase): GameViewContact.Presenter {

    //将棋描画
    override fun drawView(){
        view.drawBoard()
        //盤上
        for (i in 0..8) for (j in 0..8) {
            val(pieceName, turn, hint) = useCase.getCellInformation(i, j)
            when(turn){
                BLACK -> view.drawBlackPiece(pieceName,i,j)
                WHITE -> view.drawWhitePiece(pieceName,i,j)
            }
            if(hint)view.drawHint(i,j)
        }
        //持ち駒
        useCase.getPieceHand(BLACK).forEachIndexed{ index, piece ->
            if(piece.second != 0) {
                view.drawHoldPieceBlack(piece.first.nameJP, piece.second, index)
            }
        }
        useCase.getPieceHand(WHITE).forEachIndexed{ index, piece ->
            if(piece.second != 0) {
                view.drawHoldPieceWhite(piece.first.nameJP, piece.second, index)
            }
        }
    }

    //タッチ判定
    override fun onTouchEvent(x:Int, y:Int) {
        //持ち駒
        if(y == 0 || y == 10)useCase.setHintHoldPiece(x,y)
        //盤上
        else if( x in 0..8 && y in 1..9 ){
            when (useCase.getCellTrun(x, y-1)){
                HINT  ->{
                    useCase.setMove(x, y-1,false)
                    if(useCase.evolutionCheck(x,y-1) && !useCase.compulsionEvolutionCheck()){
                        view.showDialog()
                    }
                    // 千日手判定
                    if (useCase.isRepetitionMove()) view.gameEnd(3)
                    if (useCase.checkGameEnd()) view.gameEnd(useCase.getTurn())

                }
                useCase.getTurn() -> useCase.setTouchHint(x, y-1)
                else ->useCase.cancel()
            }
        }
        //盤外
        else useCase.cancel()
    }

    //成り判定
    override fun evolutionPiece(bool:Boolean){
        useCase.evolutionPiece(bool)
    }
}