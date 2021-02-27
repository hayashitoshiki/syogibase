package com.example.syogibase.data.local

//将棋盤定義クラス

import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.util.BLACK
import com.example.syogibase.util.WHITE

class Board {

    // マスの数
    companion object {
        const val COLS = 9
        const val ROWS = 9
    }

    // ９×９の将棋盤
    val cells = Array(COLS) { Array(ROWS) { Cell() } }

    // 持ち駒　名前　数
    val holdPieceBlack = mutableMapOf(
        FU to 0,
        KYO to 0,
        KEI to 0,
        GIN to 0,
        KIN to 0,
        KAKU to 0,
        HISYA to 0
    )

    val holdPieceWhite = mutableMapOf(
        FU to 0,
        KYO to 0,
        KEI to 0,
        GIN to 0,
        KIN to 0,
        KAKU to 0,
        HISYA to 0
    )


    //将棋盤の生成
    init {
        //初期の配置をセット
        for (i in 0 until COLS) {
            // 歩
            cells[i][2].piece = FU
            cells[i][2].turn = WHITE
            cells[i][(ROWS - 1) - 2].piece = FU
            cells[i][(ROWS - 1) - 2].turn = BLACK
            if (i == 4) {
                // 王
                cells[i][0].piece = OU
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = OU
                cells[i][ROWS - 1].turn = BLACK
            }
            if (i == 3 || i == 5) {
                // 金
                cells[i][0].piece = KIN
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = KIN
                cells[i][ROWS - 1].turn = BLACK
            } else if (i == 2 || i == 6) {
                // 銀
                cells[i][0].piece = GIN
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = GIN
                cells[i][ROWS - 1].turn = BLACK
            } else if (i == 1) {
                // 桂
                cells[i][0].piece = KEI
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = KEI
                cells[i][ROWS - 1].turn = BLACK
                cells[i][(ROWS - 1) - 1].piece = HISYA
                cells[i][(ROWS - 1) - 1].turn = BLACK
                cells[i][1].piece = KAKU
                cells[i][1].turn = WHITE
            } else if (i == 7) {
                // 桂
                cells[i][0].piece = KEI
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = KEI
                cells[i][ROWS - 1].turn = BLACK
                cells[i][(ROWS - 1) - 1].piece = KAKU
                cells[i][(ROWS - 1) - 1].turn = BLACK
                cells[i][1].piece = HISYA
                cells[i][1].turn = WHITE
            } else if (i == 0 || i == 8) {
                // 香
                cells[i][0].piece = KYO
                cells[i][0].turn = WHITE
                cells[i][ROWS - 1].piece = KYO
                cells[i][ROWS - 1].turn = BLACK
            }
        }
    }

    // カスタム初期値設定
    fun setBoard(customBoard: Array<Array<Cell>>) {
        for (i in 0 until COLS) {
            for (j in 0 until ROWS) {
                cells[i][j] = customBoard[i][j]
            }
        }
    }

    // 駒落ちセット
    fun setHandicap(turn: Int, handicap: Int) {
        when (turn) {
            BLACK -> {
                if (handicap >= 8) {
                    cells[2][ROWS - 1].piece = None
                    cells[2][ROWS - 1].turn = 0
                    cells[6][ROWS - 1].piece = None
                    cells[6][ROWS - 1].turn = 0
                }
                if (handicap >= 7) {
                    cells[1][ROWS - 1].piece = None
                    cells[1][ROWS - 1].turn = 0
                    cells[7][ROWS - 1].piece = None
                    cells[7][ROWS - 1].turn = 0
                }
                if (handicap >= 6) {
                    cells[0][ROWS - 1].piece = None
                    cells[0][ROWS - 1].turn = 0
                    cells[8][ROWS - 1].piece = None
                    cells[8][ROWS - 1].turn = 0
                }
                if (handicap >= 4) {
                    cells[7][(ROWS - 1) - 1].piece = None
                    cells[7][(ROWS - 1) - 1].turn = 0
                }
                if (handicap == 3 || handicap >= 5) {
                    cells[1][(ROWS - 1) - 1].piece = None
                    cells[1][(ROWS - 1) - 1].turn = 0
                }
            }
            WHITE -> {
                if (handicap >= 8) {
                    cells[2][0].piece = None
                    cells[2][0].turn = 0
                    cells[6][0].piece = None
                    cells[6][0].turn = 0
                }
                if (handicap >= 7) {
                    cells[1][0].piece = None
                    cells[1][0].turn = 0
                    cells[7][0].piece = None
                    cells[7][0].turn = 0
                }
                if (handicap >= 6) {
                    cells[0][0].piece = None
                    cells[0][0].turn = 0
                    cells[8][0].piece = None
                    cells[8][0].turn = 0
                }
                if (handicap >= 4) {
                    cells[1][1].piece = None
                    cells[1][1].turn = 0
                }
                if (handicap == 3 || handicap >= 5) {
                    cells[7][1].piece = None
                    cells[7][1].turn = 0
                }
            }
        }
    }
}