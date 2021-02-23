package com.example.syogibase.data.local

//１マスの情報クラス

class Cell {
    var piece: Piece = Piece.None
    var turn: Int = 0
    var hint: Boolean = false
}