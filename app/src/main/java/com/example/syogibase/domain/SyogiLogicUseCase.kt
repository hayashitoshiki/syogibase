package com.example.syogibase.domain

import com.example.syogibase.data.entity.Cell
import com.example.syogibase.data.entity.GameLog
import com.example.syogibase.data.entity.Piece
import com.example.syogibase.data.value.GameResult
import com.example.syogibase.data.value.Handicap
import com.example.syogibase.data.value.Turn

interface SyogiLogicUseCase {

    // region アクション

    // ターンを返す
    fun getTurn(): Turn

    // 駒落ち設定
    fun setHandicap(turn: Turn, handicap: Handicap)

    // 持ち駒台設定
    fun setHoldPiece(holdPiece: MutableMap<Piece, Int>, turn: Turn)

    // 指定した盤面設定
    fun setBoard(customBoard: Array<Array<Cell>>)

    // ヒントセットする
    fun setTouchHint(x: Int, y: Int)

    // 駒を動かす
    fun setMove(x: Int, y: Int, evolution: Boolean)

    // 持ち駒を使う場合
    fun setHintHoldPiece(piece: Piece, kingTurn: Turn)

    // キャンセル
    fun cancel()

    // endregion

    // region 盤面描画

    // (駒の名前,手番,ヒントの表示)を返す
    fun getCellInformation(x: Int, y: Int): Cell

    // 持ち駒を返す
    fun getPieceHand(turn: Turn): Map<Piece, Int>

    // endregion

    // region ルール

    // 詰み判定
    fun isGameEnd(): GameResult

    // 千日手判定
    fun isRepetitionMove(): Boolean

    // トライルール判定
    fun isTryKing(): Boolean

    // 成り判定
    fun isSelectEvolution(): Boolean

    // 成り判定
    fun isEvolution(): Boolean

    // 成り判定 強制か否か
    fun isCompulsionEvolution(): Boolean

    // 成り
    fun setEvolution()

    // endregion

    // region 棋譜保存

    // 一手戻す
    fun setBackMove()

    // 一手進む
    fun setGoMove()

    // 最初まで戻る
    fun setBackFirstMove()

    // 最後まで進む
    fun setGoLastMove()

    // endregion

    // region 設定

    // リセットする
    fun reset()

    // 棋譜設定
    fun setGameLog(logList: List<GameLog>)

    // 棋譜取得
    fun getGameLog(): List<GameLog>

    // 盤面を初期化する
    fun resetBoard()

    // endregion
}