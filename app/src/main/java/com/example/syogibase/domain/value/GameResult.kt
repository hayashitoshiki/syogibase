package com.example.syogibase.domain.value

/**
 * 対局終了の判定結果の種類
 */
sealed class GameResult {

    /**
     * 勝ち
     * @param winner 勝利した手番
     */
    data class Win(val winner: Turn) : GameResult()

    /**
     * 引き分け
     */
    object Draw : GameResult()

    /**
     * まだ決着がついていない
     */
    object Continue : GameResult()
}