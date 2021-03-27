package com.example.syogibase.data.entity

import com.example.syogibase.data.value.Turn
import com.example.syogibase.data.value.X
import com.example.syogibase.data.value.Y

//１局の対局ログ格納クラス

data class GameLog(
    val fromX: X,
    val fromY: Y,
    val piece: Piece,
    val turn: Turn,
    val toX: X,
    val toY: Y,
    val stealPiece: Piece?,
    val stealTurn: Turn?,
    var evolution: Boolean
)