package com.example.syogibase.data.local

// 判定結果を返すクラス
sealed class GameResult {
    // 勝ち
    data class Win(val winner: Int) : GameResult()

    // 引き分け
    object Draw : GameResult()

    // まだ決着がついていない
    object Continue : GameResult()
}