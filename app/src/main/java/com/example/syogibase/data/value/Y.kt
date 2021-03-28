package com.example.syogibase.data.value

import com.example.syogibase.data.entity.Board.Companion.ROWS

/**
 * 将棋盤のY軸
 * 1~将棋盤のY軸の上限までの値でバリデート
 * 範囲外なら0(盤外)に設定
 */
class Y(value: Int) {
    /**
     * Y軸の値
     */
    val value =
        if (value in 1..ROWS) {
            value
        } else {
            0
        }
}