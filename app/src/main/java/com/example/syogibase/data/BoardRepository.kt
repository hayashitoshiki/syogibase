package com.example.syogibase.data

import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece


interface BoardRepository {

    // region マスの情報取得

    // 局面を取得
    fun getBoard(): Array<Array<Cell>>

    // 指定したマスの情報を返す
    fun getCellInformation(x: Int, y: Int): Cell

    // 指定したマスの駒を返す
    fun getPiece(x: Int, y: Int): Piece

    // 指定したマスの駒の所有者を返す
    fun getTurn(x: Int, y: Int): Int

    // 指定したマスのヒントを返す
    fun getHint(x: Int, y: Int): Boolean

    // ヒントが表示されているマスの数を返す
    fun getCountByHint(): Int

    // 指定した手番の王様の座標を返す
    fun findKing(turn: Int): Pair<Int, Int>

    // endregion

    // region マスの情報更新

    // 駒落ち設定
    fun setHandicap(turn: Int, handicap: Int)

    // 指定した盤面を設定する
    fun setBoard(customBoard: Array<Array<Cell>>)

    // １手進める
    fun setGoMove(log: GameLog)

    // １手戻す
    fun setBackMove(log: GameLog)

    // ヒントセット
    fun setHint(x: Int, y: Int)

    // ヒントリセット
    fun resetHint()

    // 成る
    fun setEvolution(log: GameLog)

    // endregion

    // region 持ち駒

    // 持ち駒リスト取得
    fun getAllHoldPiece(turn: Int): Map<Piece, Int>

    // 持ち駒の数を取得
    fun getCountHoldPiece(turn: Int): Int

    // 持ち駒マスから取得
    fun findHoldPieceBy(i: Int, turn: Int): Piece

    // 持ち駒台の座標から駒を取得(本当はよくない)
    fun changeIntToPiece(i: Int): Piece
    // endregion

}