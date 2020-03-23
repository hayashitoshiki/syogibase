package com.example.syogibase.data


import android.util.Log
import com.example.syogibase.data.local.*
import com.example.syogibase.data.local.Piece.*
import com.example.syogibase.util.*


class BoardRepositoryImp :BoardRepository{

    private val board: Board = Board()
    private val logList = mutableListOf<GameLog>()
    private var previousX: Int = 0
    private var previousY: Int = 0
    private var previousPiece: Piece = None

    //動かす前の駒の状態をセット
    override fun setPre(x: Int, y: Int) {
        previousX = x
        previousY = y
        previousPiece =
            when(y){
                -1,10 -> changeIntToPiece(x)
                else  -> board.cells[previousX][previousY].piece
            }
    }

    //最新手を返す
    override fun getLogList(): GameLog {
        return logList.last()
    }

    //駒を動かす
    override fun setMove(x: Int, y: Int, turn: Int,evolution:Boolean) {
        val piece = changeIntToPiece(previousX)
        val gameLog = GameLog(previousX, previousY, previousPiece, turn, x, y, board.cells[x][y].piece, board.cells[x][y].turn,evolution)
        logList.add(gameLog)
        board.cells[x][y].piece =
            if(evolution) previousPiece.evolution()
            else previousPiece
        board.cells[x][y].turn = turn
        when(previousY){
            10   -> board.holdPieceBlack[piece] = board.holdPieceBlack[piece]!! - 1
            -1   -> board.holdPieceWhite[piece] = board.holdPieceWhite[piece]!! - 1
            else -> {
                board.cells[previousX][previousY].piece = None
                board.cells[previousX][previousY].turn = 0
            }
        }
    }

    //１手戻す
    override fun setBackMove() {
        val log: GameLog = logList.last()
        when (log.oldY) {
            10 -> board.holdPieceBlack[changeIntToPiece(log.oldX)] = board.holdPieceBlack[changeIntToPiece(log.oldX)]!! + 1
            -1 -> board.holdPieceWhite[changeIntToPiece(log.oldX)] = board.holdPieceWhite[changeIntToPiece(log.oldX)]!! + 1
            else -> {
                board.cells[log.oldX][log.oldY].piece = log.afterPiece
                board.cells[log.oldX][log.oldY].turn = log.afterTurn
            }
        }
        board.cells[log.newX][log.newY].piece = log.beforpiece
        board.cells[log.newX][log.newY].turn = log.beforturn
        logList.remove(log)
    }

    //成る
    override fun setEvolution() {
        val log: GameLog = logList.last()
        logList.last().evolution = true
        board.cells[log.newX][log.newY].piece = log.afterPiece.evolution()
    }

    //成れる駒か判別
    override fun findEvolutionBy(x: Int, y: Int): Boolean {
        return board.cells[x][y].piece.findEvolution()
    }

    //打った駒の打つ前のY軸取得
    override fun findLogY(): Int {
        return logList.last().oldY
    }

    //ヒントが表示されているマスの数を返す
    override fun getCountByHint(): Int {
        var count = 0
        board.cells.forEach { count += it.filter { it.hint }.count() }

        return count
    }

    //持ち駒リスト取得
    override fun getAllHoldPiece(turn: Int): Map<Piece, Int> {
        return if (turn == BLACK) {
            board.holdPieceBlack
        } else {
            board.holdPieceWhite
        }
    }

    //持ち駒の数を取得
    override fun getCountHoldPiece(turn: Int): Int {
        var count = 0
        for ((_, v) in getAllHoldPiece(turn)) {
            count += v
        }
        return count
    }

    //持ち駒マスから取得
    override fun findHoldPieceBy(i: Int, turn: Int): Piece {
        if (turn == 1 && board.holdPieceBlack[changeIntToPiece(i)] == 0 ||
            turn == 2 && board.holdPieceWhite[changeIntToPiece(i)] == 0
        ) return None

        return changeIntToPiece(i)
    }

    //持ち駒台の座標から駒を取得(本当はよくない)
    private fun changeIntToPiece(i: Int): Piece {
        return when (i) {
            2 -> FU
            3 -> KYO
            4 -> KEI
            5 -> GIN
            6 -> KIN
            7 -> KAKU
            8 -> HISYA
            else -> None
        }
    }

    //取った駒を表示
    override fun getTakePiece(): Piece {
        return logList.last().beforpiece
    }

    //持ち駒追加
    override fun setHoldPiece() {
        val log: GameLog = logList.last()
        if (log.beforpiece != None) {
            if (log.beforturn == 2) {
                when (log.beforpiece) {
                    FU, TO -> board.holdPieceBlack[FU] =
                        board.holdPieceBlack[FU]!! + 1
                    KYO, N_KYO -> board.holdPieceBlack[KYO] =
                        board.holdPieceBlack[KYO]!! + 1
                    KEI, N_KEI -> board.holdPieceBlack[KEI] =
                        board.holdPieceBlack[KEI]!! + 1
                    GIN, N_GIN -> board.holdPieceBlack[GIN] =
                        board.holdPieceBlack[GIN]!! + 1
                    KIN -> board.holdPieceBlack[KIN] =
                        board.holdPieceBlack[KIN]!! + 1
                    HISYA, RYU -> board.holdPieceBlack[HISYA] =
                        board.holdPieceBlack[HISYA]!! + 1
                    KAKU, UMA -> board.holdPieceBlack[KAKU] =
                        board.holdPieceBlack[KAKU]!! + 1
                    else -> Log.e("BoaedRepository", "不正な駒を取ろうとしています")
                }
            } else if (log.beforturn == 1) {
                when (log.beforpiece) {
                    FU, TO -> board.holdPieceWhite[FU] =
                        board.holdPieceWhite[FU]!! + 1
                    KYO, N_KYO -> board.holdPieceWhite[KYO] =
                        board.holdPieceWhite[KYO]!! + 1
                    KEI, N_KEI -> board.holdPieceWhite[KEI] =
                        board.holdPieceWhite[KEI]!! + 1
                    GIN, N_GIN -> board.holdPieceWhite[GIN] =
                        board.holdPieceWhite[GIN]!! + 1
                    KIN -> board.holdPieceWhite[KIN] =
                        board.holdPieceWhite[KIN]!! + 1
                    HISYA, RYU -> board.holdPieceWhite[HISYA] =
                        board.holdPieceWhite[HISYA]!! + 1
                    KAKU, UMA -> board.holdPieceWhite[KAKU] =
                        board.holdPieceWhite[KAKU]!! + 1
                    else -> Log.e("BoaedRepository", "不正な駒を取ろうとしています")
                }
            }
        }
    }

    //強制的にならないといけない駒かチェック
    override fun checkForcedevolution(): Boolean {
        val log: GameLog = logList.last()
        val piece = board.cells[log.newX][log.newY].piece
        if ((piece == KYO && (((log.newY <= 1 && log.afterTurn == BLACK) || (7 <= log.newY && log.afterTurn == WHITE)))) ||
            (piece == KEI && (((log.newY <= 1 && log.afterTurn == BLACK) || (7 <= log.newY && log.afterTurn == WHITE)))) ||
            piece == FU || piece == HISYA || piece == KAKU
        ) return true
        return false
    }

    //駒の動きを取得
    override fun getMove(x: Int, y: Int): Array<Array<PieceMove>> {
        return getPiece(x, y).getMove()
    }

    //ヒントセット
    override fun setHint(x: Int, y: Int) {
        board.cells[x][y].hint = true
    }

    //ヒントリセット
    override fun resetHint() {
        board.cells.forEach { it.forEach { it.hint = false } }
    }

    // 局面を取得
    override fun getBoard(): Array<Array<Cell>> {
        return board.cells
    }

    //指定のマスの駒の所有者を返す
    override fun getTurn(x: Int, y: Int): Int {
        return board.cells[x][y].turn
    }

    //指定したマスのヒントを返す
    override fun getHint(x: Int, y: Int): Boolean {
        return board.cells[x][y].hint
    }

    //指定したマスの駒を返す
    override fun getPiece(x: Int, y: Int): Piece {
        return board.cells[x][y].piece
    }

    //指定したマスの駒の名前を返す
    override fun getJPName(x: Int, y: Int): String {
        return board.cells[x][y].piece.nameJP
    }

    //指定した手番の王様の座標を返す
    override fun findKing(turn: Int): Pair<Int, Int> {
        for (i in 0..8) for (j in 0..8){
            if (board.cells[i][j].piece == OU && board.cells[i][j].turn == turn){
                return Pair(i, j)
            }
        }
        return Pair(0, 0)
    }

    //指定したマスの情報を返す
    override fun getCellInformation(x: Int, y: Int): Cell {
        return board.cells[x][y]
    }
}