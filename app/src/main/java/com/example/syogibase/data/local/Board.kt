package com.example.syogibase.data.local

//将棋盤定義クラス

import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.util.*

class Board{

    //マスの数
    companion object {
        const val COLS = 9
        const val ROWS = 9
    }

    //９×９の将棋盤
    val cells = Array(COLS, {Array(ROWS, {Cell()})})

    // 持ち駒　名前　数
    var holdPieceBlack = mutableMapOf<Piece, Int>(
        FU    to 0,
        KYO   to 0,
        KEI   to 0,
        GIN   to 0,
        KIN   to 0,
        KAKU  to 0,
        HISYA to 0
    )

    var holdPieceWhite = mutableMapOf<Piece, Int>(
        FU    to 0,
        KYO   to 0,
        KEI   to 0,
        GIN   to 0,
        KIN   to 0,
        KAKU  to 0,
        HISYA to 0
    )


    //将棋盤の生成
    init {
        //初期の配置をセット
        for (i in 0..8) {
            //歩
            cells[i][2].piece = FU
            cells[i][2].turn = WHITE
            cells[i][6].piece = FU
            cells[i][6].turn = BLACK
            if (i == 4) {
                //王
                cells[i][0].piece = OU
                cells[i][0].turn = WHITE
                cells[i][8].piece = OU
                cells[i][8].turn = BLACK
            }
            if (i == 3 || i == 5) {
                //金
                cells[i][0].piece = KIN
                cells[i][0].turn = WHITE
                cells[i][8].piece = KIN
                cells[i][8].turn = BLACK
            } else if (i == 2 || i == 6) {
                //銀
                cells[i][0].piece = GIN
                cells[i][0].turn = WHITE
                cells[i][8].piece = GIN
                cells[i][8].turn = BLACK
            } else if (i == 1) {
                //佳
                cells[i][0].piece = KEI
                cells[i][0].turn = WHITE
                cells[i][8].piece = KEI
                cells[i][8].turn = BLACK
                cells[i][7].piece = KAKU
                cells[i][7].turn = BLACK
                cells[i][1].piece = HISYA
                cells[i][1].turn = WHITE
            } else if (i == 7) {
                //佳
                cells[i][0].piece = KEI
                cells[i][0].turn = WHITE
                cells[i][8].piece = KEI
                cells[i][8].turn = BLACK
                cells[i][7].piece = HISYA
                cells[i][7].turn = BLACK
                cells[i][1].piece = KAKU
                cells[i][1].turn = WHITE
            } else if (i == 0 || i == 8) {
                //香
                cells[i][0].piece = KYO
                cells[i][0].turn = WHITE
                cells[i][8].piece = KYO
                cells[i][8].turn = BLACK
            }
        }
    }
}