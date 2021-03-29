package com.example.syogibase.domain.model

import com.example.syogibase.domain.value.Turn
import com.example.syogibase.domain.value.X
import com.example.syogibase.domain.value.Y

/**
 * １手の指し手のログ
 * @param fromX 動かす前のX座標
 * @param fromY 動かす前のY座標
 * @param piece 動かす駒
 * @param turn 動かす駒の手番
 * @param toX 動かす先のX座標
 * @param toY 動かす先のY座標
 * @param stealPiece 動かした先にある駒(駒がない場合はnull)
 * @param stealTurn 動かした先にある駒の手番(駒がない場合はnull)
 */
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