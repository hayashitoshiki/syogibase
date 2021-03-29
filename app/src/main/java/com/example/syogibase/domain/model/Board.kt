package com.example.syogibase.domain.model

import com.example.syogibase.domain.model.Piece.*
import com.example.syogibase.domain.value.Handicap
import com.example.syogibase.domain.value.Turn
import com.example.syogibase.domain.value.Turn.BLACK
import com.example.syogibase.domain.value.Turn.WHITE

/**
 * 将棋盤
 */
class Board {

    // マスの数
    companion object {
        const val COLS = 9
        const val ROWS = 9
    }

    // ９×９の将棋盤
    private val cells = Array(COLS) { Array(ROWS) { Cell() } }


    //将棋盤の生成
    init {
        //初期の配置をセット
        for (i in 0 until COLS) {
            // 歩
            cells[i][2].setPiece(FU, WHITE)
            cells[i][(ROWS - 1) - 2].setPiece(FU, BLACK)
            if (i == 4) {
                // 王
                cells[i][0].setPiece(OU, WHITE)
                cells[i][ROWS - 1].setPiece(OU, BLACK)
            }
            if (i == 3 || i == 5) {
                // 金
                cells[i][0].setPiece(KIN, WHITE)
                cells[i][ROWS - 1].setPiece(KIN, BLACK)
            } else if (i == 2 || i == 6) {
                // 銀
                cells[i][0].setPiece(GIN, WHITE)
                cells[i][ROWS - 1].setPiece(GIN, BLACK)
            } else if (i == 1) {
                // 桂
                cells[i][0].setPiece(KEI, WHITE)
                cells[i][ROWS - 1].setPiece(KEI, BLACK)
                cells[i][1].setPiece(KAKU, WHITE)
                cells[i][(ROWS - 1) - 1].setPiece(HISYA, BLACK)
            } else if (i == 7) {
                // 桂
                cells[i][0].setPiece(KEI, WHITE)
                cells[i][ROWS - 1].setPiece(KEI, BLACK)
                cells[i][1].setPiece(HISYA, WHITE)
                cells[i][(ROWS - 1) - 1].setPiece(KAKU, BLACK)
            } else if (i == 0 || i == 8) {
                // 香
                cells[i][0].setPiece(KYO, WHITE)
                cells[i][ROWS - 1].setPiece(KYO, BLACK)
            }
        }
    }

    /**
     * 将棋盤を指定した盤面で設定
     * @param customBoard 設定する盤面
     */
    fun setBoard(customBoard: Array<Array<Cell>>) {
        for (i in 0 until COLS) {
            for (j in 0 until ROWS) {
                cells[i][j] = customBoard[i][j]
            }
        }
    }

    /**
     * 駒落ちのハンデを設定
     * @param turn ハンデ設定するマス
     * @param handicap 設定するハンデ
     */
    fun setHandicap(turn: Turn, handicap: Handicap) {
        when (turn) {
            BLACK -> {
                if (handicap == Handicap.HATIMAI) {
                    cells[2][ROWS - 1].clear()
                    cells[6][ROWS - 1].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI) {
                    cells[1][ROWS - 1].clear()
                    cells[7][ROWS - 1].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI) {
                    cells[0][ROWS - 1].clear()
                    cells[8][ROWS - 1].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI || handicap == Handicap.NIMAI || handicap == Handicap.KAKU) {
                    cells[7][(ROWS - 1) - 1].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI || handicap == Handicap.NIMAI || handicap == Handicap.HISYA) {
                    cells[1][(ROWS - 1) - 1].clear()
                }
                if (handicap == Handicap.HIDARIKYO) {
                    cells[8][(ROWS - 1) - 1].clear()
                }
            }
            WHITE -> {
                if (handicap == Handicap.HATIMAI) {
                    cells[2][0].clear()
                    cells[6][0].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI) {
                    cells[1][0].clear()
                    cells[7][0].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI) {
                    cells[0][0].clear()
                    cells[8][0].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI || handicap == Handicap.NIMAI || handicap == Handicap.KAKU) {
                    cells[1][1].clear()
                }
                if (handicap == Handicap.HATIMAI || handicap == Handicap.ROKUMAI || handicap == Handicap.YONMAI || handicap == Handicap.NIMAI || handicap == Handicap.HISYA) {
                    cells[7][1].clear()
                }
                if (handicap == Handicap.HIDARIKYO) {
                    cells[0][0].clear()
                }
            }
        }
    }

    /**
     * 全てのマスの情報をクリアする
     */
    fun clear() {
        cells.forEach {
            it.forEach {
                it.clear()
            }
        }
    }

    /**
     * コマを置ける場所の総数を返す
     * @return コマを置ける場所の総数
     */
    fun getCountByHint(): Int {
        var count = 0
        cells.forEach { count += it.filter { it.hint }.count() }
        return count
    }

    /**
     * 全てのマスのヒントをリセット
     */
    fun restHintAll() {
        cells.forEach { it.forEach { it.hint = false } }
    }

    /**
     * 指定されたマスの情報を取得
     * @param x 取得するマスのX座標
     * @param y 取得するマスのY座標
     */
    fun getCell(x: Int, y: Int): Cell {
        return cells[x - 1][y - 1]
    }

    /**
     * 指定されたマスに動かせるか設定
     * @param x 設定するマスのX座標
     * @param y 設定するマスのY座標
     * @param bool 動かせるかの有無
     */
    fun setHint(x: Int, y: Int, bool: Boolean) {
        cells[x - 1][y - 1].hint = bool
    }

    /**
     * 将棋盤の全てのマスを返す
     */
    fun getCells(): Array<Array<Cell>> {
        return cells
    }
}