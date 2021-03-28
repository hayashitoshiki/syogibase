package com.example.syogibase.data.value

import com.example.syogibase.data.entity.Board.Companion.COLS

/**
 * 将棋盤のX軸
 * 1~将棋盤のX軸の上限までの値でバリデート
 * 範囲外なら0(盤外)に設定
 */
class X(value: Int) {
    /**
     * X軸の値
     */
    val value =
        if (value in 1..COLS) {
            value
        } else {
            0
        }
}