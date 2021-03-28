package com.example.syogibase.data.entity

import com.example.syogibase.data.value.Turn

/**
 * 将棋盤のマスの情報クラス
 */
class Cell {

    /**
     * 駒の情報
     */
    var piece: Piece? = null
        private set

    /**
     * 駒の手番の情報
     */
    var turn: Turn? = null
        private set

    /**
     * マスに対して駒を動かせるか
     */
    var hint: Boolean = false

    /**
     * マスに駒を設定する
     * @param piece 設定する駒
     * @param turn 設定する手番
     */
    fun setPiece(piece: Piece, turn: Turn) {
        this.piece = piece
        this.turn = turn
    }

    /**
     * マスに対して設定している駒の情報を空にする
     */
    fun setNone() {
        piece = null
        turn = null
    }

    /**
     * マスに対する情報を全て空にする
     */
    fun clear() {
        piece = null
        turn = null
        hint = false
    }
}