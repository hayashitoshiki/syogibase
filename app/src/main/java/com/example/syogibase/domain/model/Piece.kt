package com.example.syogibase.domain.model

import com.example.syogibase.domain.value.PieceMove

/**
 * 将棋で使う駒の種類
 */
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

    /**
     * 指定した駒を動きを返す
     * @return 指定した駒の動き
     */
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

    /**
     * 成ったときの駒を返す
     * @return 成った後の駒
     */
    fun evolution(): Piece {
        return when (this) {
            FU, TO -> TO
            KEI, N_KEI -> N_KYO
            KYO, N_KYO -> N_KYO
            GIN, N_GIN -> N_GIN
            HISYA, RYU -> RYU
            KAKU, UMA -> UMA
            KIN -> KIN
            OU -> OU
            GYOKU -> GYOKU
        }
    }

    /**
     * 成る前の駒を返す
     * @return 成る前の駒
     */
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

    /**
     * 駒が成ることができるか返す
     * @return 成ることができるか
     */
    fun isEvolution(): Boolean {
        return when (this) {
            FU, KEI, KYO, GIN, HISYA, KAKU -> true
            else -> false
        }
    }

    /**
     * １マス上に動けるか判定する
     * @return １マス上に動けるか
     */
    fun isUpMovePiece(): Boolean {
        return when (this) {
            FU, TO, KYO, N_KYO, GIN, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    /**
     * ２マス以上上に動けるか判定する
     * @return ２マス以上上に動けるか
     */
    fun isLongUpMovePiece(): Boolean {
        return when (this) {
            KYO, HISYA, RYU -> true
            else -> false
        }
    }

    /**
     * １マス下に動けるか判定する
     * @return １マス下に動けるか
     */
    fun isDownMovePiece(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    /**
     * ２マス以上下に動けるか判定する
     * @return ２マス以上下に動けるか
     */
    fun isLongDownMovePiece(): Boolean {
        return when (this) {
            HISYA, RYU -> true
            else -> false
        }
    }

    /**
     * １マス横に動けるか判定する
     * @return １マス横に動けるか
     */
    fun isLRMovePiece(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, KIN, OU, GYOKU, HISYA, RYU, UMA -> true
            else -> false
        }
    }

    /**
     * ２マス以上横に動けるか判定する
     * @return ２マス以上横に動けるか
     */
    fun isLongLRMovePiece(): Boolean {
        return when (this) {
            HISYA, RYU -> true
            else -> false
        }
    }

    /**
     * １マス斜め上に動けるか判定する
     * @return １マス斜め上に動けるか
     */
    fun isDiagonalUp(): Boolean {
        return when (this) {
            TO, N_KYO, N_KEI, N_GIN, GIN, KIN, OU, GYOKU, KAKU, RYU, UMA -> true
            else -> false
        }
    }

    /**
     * １マス斜め下に動けるか判定する
     * @return １マス斜め下に動けるか
     */
    fun isDiagonalDown(): Boolean {
        return when (this) {
            GIN, OU, GYOKU, KAKU, RYU, UMA -> true
            else -> false
        }
    }

    /**
     * ２マス以上斜めに動けるか判定する
     * @return ２マス以上斜めに動けるか
     */
    fun isLongDiagonal(): Boolean {
        return when (this) {
            KAKU, UMA -> true
            else -> false
        }
    }

}