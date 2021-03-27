package com.example.syogibase.data.value

import com.example.syogibase.data.entity.Board.Companion.COLS

class X(value: Int) {
    val value =
        if (value in 1..COLS) {
            value
        } else {
            0
        }
}