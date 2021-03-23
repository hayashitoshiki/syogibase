package com.example.syogibase.data.entity

//１マスの情報クラス

class Cell {

    var piece: Piece = Piece.None
        private set

    var turn: Int? = null
        private set

    var hint: Boolean = false

    fun setPiece(piece: Piece, turn: Int) {
        this.piece = piece
        this.turn = turn
    }

    fun setNone() {
        piece = Piece.None
        turn = null
    }

    fun clear() {
        piece = Piece.None
        turn = null
        hint = false
    }
}