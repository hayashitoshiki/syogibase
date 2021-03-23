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

    // 持ち駒リストを返す
    fun keys(): MutableSet<Piece> {
        return _pieceList.keys
    }

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

    //持ち駒台の座標からもしコマを取得していれば駒を取得(本当はよくない)
    fun changeIntToPiece(i: Int): Piece {
        when (i) {
            2 -> if (pieceList[Piece.FU] != 0) return Piece.FU
            3 -> if (pieceList[Piece.KYO] != 0) return Piece.KYO
            4 -> if (pieceList[Piece.KEI] != 0) return Piece.KEI
            5 -> if (pieceList[Piece.GIN] != 0) return Piece.GIN
            6 -> if (pieceList[Piece.KIN] != 0) return Piece.KIN
            7 -> if (pieceList[Piece.KAKU] != 0) return Piece.KAKU
            8 -> if (pieceList[Piece.HISYA] != 0) return Piece.HISYA
        }
        return Piece.None
    }

}