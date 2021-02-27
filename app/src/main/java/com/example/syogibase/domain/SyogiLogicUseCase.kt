package com.example.syogibase.domain

import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.Piece

interface SyogiLogicUseCase {

    // region アクション

    // ターンを返す
    fun getTurn(): Int

    // 駒落ち設定
    fun setHandicap(turn: Int, handicap: Int)

    // 指定した盤面設定
    fun setBoard(customBoard: Array<Array<Cell>>)

    // ヒントセットする
    fun setTouchHint(x: Int, y: Int)

    // 駒を動かす
    fun setMove(x: Int, y: Int, evolution: Boolean)

    // 持ち駒を使う場合
    fun setHintHoldPiece(x: Int, y: Int, kingTurn: Int)

    // キャンセル
    fun cancel()

    // endregion

    // region 盤面描画

    // (駒の名前,手番,ヒントの表示)を返す
    fun getCellInformation(x: Int, y: Int): Cell

    // マスの手番を返す
    fun getCellTurn(x: Int, y: Int): Int

    // 持ち駒を加工して返す
    fun getPieceHand(turn: Int): MutableList<Pair<Piece, Int>>

    // endregion

    // region ルール

    // 詰み判定
    fun isGameEnd(): Boolean

    // 千日手判定
    fun isRepetitionMove(): Boolean

    // トライルール判定
    fun isTryKing(): Boolean

    // 成り判定
    fun isEvolution(x: Int, y: Int): Boolean

    // 成り判定 強制か否か
    fun isCompulsionEvolution(): Boolean

    // 成り
    fun setEvolution()

    // endregion
}