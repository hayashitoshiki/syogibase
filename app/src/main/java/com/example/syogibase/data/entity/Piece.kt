package com.example.syogibase.data.entity

import com.example.syogibase.data.value.PieceMove

//将棋の駒格納クラス

enum class Piece(val nameJP: String) {
    FU("歩"),
    TO("と"),
    KYO("香"),
    N_KYO("杏"),
    KEI("桂"),
    N_KEI("圭"),
    GIN("銀"),
    N_GIN("全"),
    KIN("金"),
    HISYA("飛"),
    RYU("龍"),
    KAKU("角"),
    UMA("馬"),
    OU("王"),
    GYOKU("玉");

    // 駒の動き方
    fun getMove(): Array<Array<PieceMove>> {
        when (this) {
            FU -> return arrayOf(
                arrayOf(
                    PieceMove(
                        0,
                        -1
                    )
                )
            )
            KEI -> return arrayOf(
                arrayOf(
                    PieceMove(
                        1,
                        -2
                    )
                ), arrayOf(PieceMove(-1, -2))
            )
            KYO -> return arrayOf(
                arrayOf(
                    PieceMove(0, -1),
                    PieceMove(0, -2),
                    PieceMove(0, -3),
                    PieceMove(0, -4),
                    PieceMove(0, -5),
                    PieceMove(0, -6),
                    PieceMove(0, -7),
                    PieceMove(0, -8)
                )
            )
            GIN -> return arrayOf(
                arrayOf(PieceMove(-1, -1)), arrayOf(
                    PieceMove(0, -1)
                ), arrayOf(PieceMove(1, -1)),
                arrayOf(PieceMove(-1, 1)), arrayOf(
                    PieceMove(1, 1)
                )
            )
            KIN, TO, N_KYO, N_KEI, N_GIN -> return arrayOf(
                arrayOf(PieceMove(-1, -1)), arrayOf(
                    PieceMove(0, -1)
                ), arrayOf(PieceMove(1, -1)),
                arrayOf(PieceMove(-1, 0)), arrayOf(
                    PieceMove(1, 0)
                ),
                arrayOf(PieceMove(0, 1))
            )
            OU, GYOKU -> return arrayOf(
                arrayOf(PieceMove(-1, -1)), arrayOf(
                    PieceMove(-1, 0)
                ), arrayOf(PieceMove(-1, 1)),
                arrayOf(PieceMove(0, -1)), arrayOf(
                    PieceMove(0, 1)
                ),
                arrayOf(PieceMove(1, -1)), arrayOf(
                    PieceMove(1, 0)
                ), arrayOf(PieceMove(1, 1))
            )
            HISYA -> return arrayOf(
                arrayOf(
                    PieceMove(0, 1),
                    PieceMove(0, 2),
                    PieceMove(0, 3),
                    PieceMove(0, 4),
                    PieceMove(0, 5),
                    PieceMove(0, 6),
                    PieceMove(0, 7),
                    PieceMove(0, 8)
                ),
                arrayOf(
                    PieceMove(1, 0),
                    PieceMove(2, 0),
                    PieceMove(3, 0),
                    PieceMove(4, 0),
                    PieceMove(5, 0),
                    PieceMove(6, 0),
                    PieceMove(7, 0),
                    PieceMove(8, 0)
                ),
                arrayOf(
                    PieceMove(-1, 0),
                    PieceMove(-2, 0),
                    PieceMove(-3, 0),
                    PieceMove(-4, 0),
                    PieceMove(-5, 0),
                    PieceMove(-6, 0),
                    PieceMove(-7, 0),
                    PieceMove(-8, 0)
                ),
                arrayOf(
                    PieceMove(0, -1),
                    PieceMove(0, -2),
                    PieceMove(0, -3),
                    PieceMove(0, -4),
                    PieceMove(0, -5),
                    PieceMove(0, -6),
                    PieceMove(0, -7),
                    PieceMove(0, -8)
                )
            )
            RYU -> return arrayOf(
                arrayOf(
                    PieceMove(0, 1),
                    PieceMove(0, 2),
                    PieceMove(0, 3),
                    PieceMove(0, 4),
                    PieceMove(0, 5),
                    PieceMove(0, 6),
                    PieceMove(0, 7),
                    PieceMove(0, 8)
                ),
                arrayOf(
                    PieceMove(1, 0),
                    PieceMove(2, 0),
                    PieceMove(3, 0),
                    PieceMove(4, 0),
                    PieceMove(5, 0),
                    PieceMove(6, 0),
                    PieceMove(7, 0),
                    PieceMove(8, 0)
                ),
                arrayOf(
                    PieceMove(-1, 0),
                    PieceMove(-2, 0),
                    PieceMove(-3, 0),
                    PieceMove(-4, 0),
                    PieceMove(-5, 0),
                    PieceMove(-6, 0),
                    PieceMove(-7, 0),
                    PieceMove(-8, 0)
                ),
                arrayOf(
                    PieceMove(0, -1),
                    PieceMove(0, -2),
                    PieceMove(0, -3),
                    PieceMove(0, -4),
                    PieceMove(0, -5),
                    PieceMove(0, -6),
                    PieceMove(0, -7),
                    PieceMove(0, -8)
                ),
                arrayOf(PieceMove(-1, -1)),
                arrayOf(PieceMove(-1, 1)),
                arrayOf(PieceMove(1, -1)),
                arrayOf(PieceMove(1, 1))
            )
            KAKU -> return arrayOf(
                arrayOf(
                    PieceMove(1, 1),
                    PieceMove(2, 2),
                    PieceMove(3, 3),
                    PieceMove(4, 4),
                    PieceMove(5, 5),
                    PieceMove(6, 6),
                    PieceMove(7, 7),
                    PieceMove(8, 8)
                ),
                arrayOf(
                    PieceMove(1, -1),
                    PieceMove(2, -2),
                    PieceMove(3, -3),
                    PieceMove(4, -4),
                    PieceMove(5, -5),
                    PieceMove(6, -6),
                    PieceMove(7, -7),
                    PieceMove(8, -8)
                ),
                arrayOf(
                    PieceMove(-1, -1),
                    PieceMove(-2, -2),
                    PieceMove(-3, -3),
                    PieceMove(-4, -4),
                    PieceMove(-5, -5),
                    PieceMove(-6, -6),
                    PieceMove(-7, -7),
                    PieceMove(-8, -8)
                ),
                arrayOf(
                    PieceMove(-1, 1),
                    PieceMove(-2, 2),
                    PieceMove(-3, 3),
                    PieceMove(-4, 4),
                    PieceMove(-5, 5),
                    PieceMove(-6, 6),
                    PieceMove(-7, 7),
                    PieceMove(-8, 8)
                )
            )
            UMA -> return arrayOf(
                arrayOf(
                    PieceMove(1, 1),
                    PieceMove(2, 2),
                    PieceMove(3, 3),
                    PieceMove(4, 4),
                    PieceMove(5, 5),
                    PieceMove(6, 6),
                    PieceMove(7, 7),
                    PieceMove(8, 8)
                ),
                arrayOf(
                    PieceMove(1, -1),
                    PieceMove(2, -2),
                    PieceMove(3, -3),
                    PieceMove(4, -4),
                    PieceMove(5, -5),
                    PieceMove(6, -6),
                    PieceMove(7, -7),
                    PieceMove(8, -8)
                ),
                arrayOf(
                    PieceMove(-1, -1),
                    PieceMove(-2, -2),
                    PieceMove(-3, -3),
                    PieceMove(-4, -4),
                    PieceMove(-5, -5),
                    PieceMove(-6, -6),
                    PieceMove(-7, -7),
                    PieceMove(-8, -8)
                ),
                arrayOf(
                    PieceMove(-1, 1),
                    PieceMove(-2, 2),
                    PieceMove(-3, 3),
                    PieceMove(-4, 4),
                    PieceMove(-5, 5),
                    PieceMove(-6, 6),
                    PieceMove(-7, 7),
                    PieceMove(-8, 8)
                ),
                arrayOf(PieceMove(-1, 0)),
                arrayOf(PieceMove(1, 0)),
                arrayOf(PieceMove(0, -1)),
                arrayOf(PieceMove(0, 1))
            )
            else -> return arrayOf(
                arrayOf(
                    PieceMove(
                        0,
                        0
                    )
                )
            )
        }
    }

    //進化取得
    fun evolution(): Piece {
        return when (this) {
            FU -> TO
            KEI -> N_KYO
            KYO -> N_KYO
            GIN -> N_GIN
            HISYA -> RYU
            KAKU -> UMA
            else -> this
        }
    }

    // 退化
    fun degeneration(): Piece {
        return when (this) {
            TO, FU -> FU
            N_KEI, KEI -> KEI
            N_KYO, KYO -> KYO
            N_GIN, GIN -> GIN
            RYU, HISYA -> HISYA
            UMA, KAKU -> KAKU
            KIN -> KIN
            OU -> OU
            GYOKU -> GYOKU
        }
    }

    // 成れる駒の分類
    fun isEvolution(): Boolean {
        return when (this) {
            FU, KEI, KYO, GIN, HISYA, KAKU -> true
            else -> false
        }
    }

    // 上へ動ける駒
    fun isUpMovePiece(): Boolean {
        return when (this) {
            FU, TO, KYO, N_KYO, GIN, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    // 下へ動ける駒
    fun isDownMovePiece(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    // 横へ動ける駒
    fun isLRMovePiece(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    // 横へ2マス以上動ける駒
    fun isLongLRMovePiece(): Boolean {
        return when (this) {
            HISYA, RYU -> true
            else -> false
        }
    }

    // 斜め上へ動ける駒
    fun isDiagonalUp(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, GIN, KIN, OU, GYOKU, KAKU, RYU, UMA -> true
            else -> false
        }
    }

    // 斜め下へ動ける駒
    fun isDiagonalDown(): Boolean {
        return when (this) {
            GIN, OU, GYOKU, KAKU, RYU, UMA -> true
            else -> false
        }
    }

}