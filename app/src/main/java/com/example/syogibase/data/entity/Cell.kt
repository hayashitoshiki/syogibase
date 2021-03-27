package com.example.syogibase.data.entity

import com.example.syogibase.data.value.Turn

//１マスの情報クラス

class Cell {

    var piece: Piece? = null
        private set

    var turn: Turn? = null
        private set

    var hint: Boolean = false

    fun setPiece(piece: Piece, turn: Turn) {
        this.piece = piece
        this.turn = turn
    }

    fun setNone() {
        piece = null
        turn = null
    }

    fun clear() {
        piece = null
        turn = null
        hint = false
    }
}