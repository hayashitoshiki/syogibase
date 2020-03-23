package com.example.syogibase.domain

import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece

interface SyogiLogicUseCase {

    //ヒントセットする
    fun setTouchHint(x:Int,y:Int)
    //持ち駒を使う場合
    fun setHintHoldPiece(x: Int, y: Int)
    //駒を動かす
    fun setMove(x: Int, y: Int, evolution:Boolean)
    //成り判定
    fun evolutionCheck(x:Int, y:Int):Boolean
    //成り判定 強制か否か
    fun compulsionEvolutionCheck():Boolean
    //成り
    fun evolutionPiece(bool: Boolean)
    //駒を動かした後～王手判定
    fun checkGameEnd():Boolean
    //キャンセル
    fun cancel()
    //(駒の名前,手番,ヒントの表示)を返す
    fun getCellInformation(x:Int,y:Int):Triple<String,Int,Boolean>
    //(駒の名前,手番,ヒントの表示)を返す
    fun getCellTrun(x:Int,y:Int):Int
    //持ち駒を加工して返す
    fun getPieceHand(turn:Int):MutableList<Pair<Piece,Int>>
    //ターンを返す
    fun getTurn():Int
    //手番を設定する
    fun setTurn(turn:Int)
    //ヒントを設定する
    fun setHint(x:Int, y:Int, newX:Int, newY:Int, turn:Int)
    //最後のログを取得する
    fun getLogLast(): GameLog
    //動かす駒の元の位置をセットする
    fun setPre(x:Int, y:Int)
    // 千日手判定
    fun isRepetitionMove(): Boolean
    // トライルール判定
    fun isTryKing(): Boolean
}