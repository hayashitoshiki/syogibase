package com.example.syogibase.domain.usecase

import com.example.syogibase.domain.model.Cell
import com.example.syogibase.domain.model.GameLog
import com.example.syogibase.domain.model.Piece
import com.example.syogibase.domain.value.GameResult
import com.example.syogibase.domain.value.Handicap
import com.example.syogibase.domain.value.Turn

/**
 * 将棋ロジック
 */
interface SyogiLogicUseCase {

    // region アクション

    /**
     * 現在の手番を返す
     * @return 現在の手番
     */
    fun getTurn(): Turn

    /**
     * 駒落ちの設定
     * @param turn 設定する手番
     * @param handicap　設定するハンデ
     */
    fun setHandicap(turn: Turn, handicap: Handicap)

    /**
     * 持ち駒の設定
     * @param holdPiece 設定する持ち駒
     * @param turn 設定する手番
     */
    fun setHoldPiece(holdPiece: MutableMap<Piece, Int>, turn: Turn)

    /**
     * 指定した盤面を設定
     * @param customBoard 設定する盤面
     */
    fun setBoard(customBoard: Array<Array<Cell>>)

    /**
     * 指定したマスの駒の動けるマスを検索
     * @param x 指定するマスのX軸
     * @param y 指定するマスのY軸
     */
    fun setTouchHint(x: Int, y: Int)

    /**
     * 指定した位置に駒を動かす
     * @param x 動かす先のマスのX軸
     * @param y 動かす先のマスのY軸
     * @param evolution 動いた後駒が成るか成らないか
     */
    fun setMove(x: Int, y: Int, evolution: Boolean)

    /**
     * 持ち駒を使う場合の駒をおける場所の検索
     * @param piece 使う駒
     * @param turn 使用する手番
     */
    fun setHintHoldPiece(piece: Piece, turn: Turn)

    /**
     * 動かせるマスの設定をリセットする
     */
    fun cancel()

    // endregion

    // region 盤面描画

    /**
     * 指定したマスの情報を返す
     * @param x 取得したいマスのX軸
     * @param y 取得したいマスのY軸
     * @return 指定したマスの情報
     */
    fun getCellInformation(x: Int, y: Int): Cell

    /**
     * 指定した手番の持ち駒を返す
     * @param turn 取得したい持ち駒の手番
     * @return 指定した手番の持ち駒
     */
    fun getPieceHand(turn: Turn): Map<Piece, Int>

    // endregion

    // region ルール

    /**
     * 対局終了判定を行う
     * @return 対局終了判定結果
     */
    fun isGameEnd(): GameResult

    /**
     * 千日手判定を行う
     * @return 千日手の判定結果
     */
    fun isRepetitionMove(): Boolean

    /**
     * トライルールの判定を行う
     * @return トライルールの判定結果
     */
    fun isTryKing(): Boolean

    /**
     * 駒を任意で成るかの選択ができるか判定を行う
     * @return 任意で成るか判定できるかの判定結果
     */
    fun isSelectEvolution(): Boolean

    /**
     * 駒が成れるかの判定を行う
     * @return 駒が成れるかの判定結果
     */
    fun isEvolution(): Boolean

    /**
     * 強制的に成らないといけないかの判定を行う
     * @return 強制的に成らないといけないかの判定結果
     */
    fun isCompulsionEvolution(): Boolean

    /**
     * 駒を成らせる
     */
    fun setEvolution()

    // endregion

    // region 棋譜保存

    /**
     * １手戻す
     */
    fun setBackMove()

    /**
     * １手進む
     */
    fun setGoMove()

    /**
     * 最初まで戻る
     */
    fun setBackFirstMove()

    /**
     * 最後まで進む
     */
    fun setGoLastMove()

    // endregion

    // region 設定

    /**
     * 盤面をまっさらにする
     */
    fun reset()

    /**
     * 棋譜を設定する
     * @param logList 設定する棋譜
     */
    fun setGameLog(logList: List<GameLog>)

    /**
     * 現在の棋譜を取得する
     * @return 取得する棋譜
     */
    fun getGameLog(): List<GameLog>

    /**
     * 盤面を初期化する
     */
    fun resetBoard()

    // endregion
}