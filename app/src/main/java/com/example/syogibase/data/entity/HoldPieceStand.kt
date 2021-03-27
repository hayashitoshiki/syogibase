package com.example.syogibase.data.entity

class HoldPieceStand {

    private val _pieceList = mutableMapOf(
        Piece.FU to 0,
        Piece.KYO to 0,
        Piece.KEI to 0,
        Piece.GIN to 0,
        Piece.KIN to 0,
        Piece.KAKU to 0,
        Piece.HISYA to 0
    )
    val pieceList: Map<Piece, Int> get() = _pieceList

    // 持ち駒追加
    fun add(piece: Piece) {
        // 通常モードの場合
        _pieceList.keys.forEach {
            if (piece == it || piece == it.evolution()) {
                _pieceList[it] = _pieceList[it]!! + 1
            }
        }
    }

    // 持ち駒削除
    fun remove(piece: Piece) {
        // 通常モードの場合
        _pieceList.keys.forEach {
            if (piece == it) {
                _pieceList[it] = _pieceList[it]!! - 1
            }
        }
    }

    // 持ち駒初期化
    fun reset() {
        _pieceList.keys.forEach {
            _pieceList[it] = 0
        }
    }

    // 指定した持ち駒で初期化
    fun setCustomStand(holdPieceBlack: MutableMap<Piece, Int>) {
        _pieceList.clear()
        _pieceList.putAll(holdPieceBlack)
    }

    // 持ち駒台から指定したコマを取得
    fun getStandPiece(piece: Piece): Piece? {
        when (piece) {
            Piece.FU -> if (pieceList[Piece.FU] != 0) return Piece.FU
            Piece.KYO -> if (pieceList[Piece.KYO] != 0) return Piece.KYO
            Piece.KEI -> if (pieceList[Piece.KEI] != 0) return Piece.KEI
            Piece.GIN -> if (pieceList[Piece.GIN] != 0) return Piece.GIN
            Piece.KIN -> if (pieceList[Piece.KIN] != 0) return Piece.KIN
            Piece.KAKU -> if (pieceList[Piece.KAKU] != 0) return Piece.KAKU
            Piece.HISYA -> if (pieceList[Piece.HISYA] != 0) return Piece.HISYA
        }
        return null
    }

}