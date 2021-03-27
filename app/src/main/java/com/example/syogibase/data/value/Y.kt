package com.example.syogibase.data.value

import com.example.syogibase.data.entity.Board.Companion.ROWS

class Y(value: Int) {
    val value =
        if (value in 1..ROWS) {
            value
        } else {
            0
        }
}