package com.example.syogibase.data

import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece
import com.example.syogibase.data.local.PieceMove


interface BoardRepository {

    // region マスの情報取得

    // 局面を取得
    fun getBoard(): Array<Array<Cell>>

    // ログ設定
    fun setLog(log: MutableList<GameLog>)

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

    // endregion

    // region マスの情報更新

    // 駒落ち設定
    fun setHandicap(turn: Int, handicap: Int)

    // ヒントセット
    fun setHint(x: Int, y: Int)

    // １手戻す(ヒント)
    fun setPreBackMove()

    // ヒントリセット
    fun resetHint()

    // 駒を動かす
    fun setMove(x: Int, y: Int, turn: Int, evolution: Boolean)

    // 成る
    fun setEvolution()

    // 動かす前の駒の状態をセット
    fun setPre(x: Int, y: Int)

    // endregion

    // region 持ち駒

    // 持ち駒リスト取得
    fun getAllHoldPiece(turn: Int): Map<Piece, Int>

    // 持ち駒の数を取得
    fun getCountHoldPiece(turn: Int): Int

    // 持ち駒マスから取得
    fun findHoldPieceBy(i: Int, turn: Int): Piece

    // 持ち駒追加
    fun setHoldPiece(log: GameLog)

    // endregion

    // 打ったコマの打つ前の座標取得
    fun getBeforePieceCoordinate(): PieceMove

    // 指定した手番の王様の座標を返す
    fun findKing(turn: Int): Pair<Int, Int>

    // 強制的にならないといけない駒かチェック
    fun isCompulsionEvolution(): Boolean

    fun setBoard(customBoard: Array<Array<Cell>>)

    // region 棋譜再生

    // １手進める(感想戦)
    fun setGoMove(log: GameLog)

    // １手戻す(感想戦)
    fun setBackMove(log: GameLog)

    // 指定したログ取得
    fun getLogByIndex(index: Int): GameLog

    // ログサイズ取得
    fun getLogSize(): Int

    // endregion
}