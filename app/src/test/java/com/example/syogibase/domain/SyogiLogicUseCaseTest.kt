package com.example.syogibase.domain

import com.example.syogibase.data.entity.Board
import com.example.syogibase.data.entity.Board.Companion.COLS
import com.example.syogibase.data.entity.Board.Companion.ROWS
import com.example.syogibase.data.entity.GameLog
import com.example.syogibase.data.entity.GameResult
import com.example.syogibase.data.entity.Piece
import com.example.syogibase.util.BLACK
import com.example.syogibase.util.BLACK_HOLD
import com.example.syogibase.util.WHITE
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class SyogiLogicUseCaseTest {


    @Before
    fun setUp() {
        board.clear()
    }

    // 持ち駒全取得
    private val holdPiece = mutableMapOf(
        Piece.FU to 1,
        Piece.KYO to 1,
        Piece.KEI to 1,
        Piece.GIN to 1,
        Piece.KIN to 1,
        Piece.KAKU to 1,
        Piece.HISYA to 1
    )

    // からのボードを設定
    private val board = Board()

    // region 詰み判定

    /**
     * 王手判定
     * 条件：王が逃げて逃れることができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteEscapeTrue() {
        board.cells[4][1].setPiece(Piece.OU, WHITE)
        board.cells[4][2].setPiece(Piece.KIN, BLACK)
        board.cells[4][3].setPiece(Piece.KIN, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：逃げようとすると王手になるから詰み
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteEscapeFalse() {
        board.cells[4][0].setPiece(Piece.OU, WHITE)
        board.cells[4][1].setPiece(Piece.KIN, BLACK)
        board.cells[4][2].setPiece(Piece.KIN, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(BLACK))
    }

    /**
     * 王手判定
     * 条件：コマを動かして防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteMoveTrue() {
        board.cells[0][0].setPiece(Piece.OU, WHITE)
        board.cells[1][0].setPiece(Piece.KYO, WHITE)
        board.cells[1][1].setPiece(Piece.KIN, WHITE)
        board.cells[4][2].setPiece(Piece.KYO, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：コマを動かしても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteMoveFalse() {
        board.cells[0][0].setPiece(Piece.OU, WHITE)
        board.cells[1][0].setPiece(Piece.KYO, WHITE)
        board.cells[1][1].setPiece(Piece.KIN, WHITE)
        board.cells[0][3].setPiece(Piece.KYO, BLACK)
        board.cells[3][3].setPiece(Piece.KAKU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(BLACK))
    }

    /**
     * 王手判定
     * 条件：駒をとって防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteTakeTrue() {
        board.cells[0][0].setPiece(Piece.OU, WHITE)
        board.cells[0][1].setPiece(Piece.KYO, WHITE)
        board.cells[1][0].setPiece(Piece.KEI, WHITE)
        board.cells[1][1].setPiece(Piece.KIN, WHITE)
        board.cells[0][2].setPiece(Piece.KYO, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：駒をとっても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteTakeFalse() {
        board.cells[0][0].setPiece(Piece.GYOKU, WHITE)
        board.cells[0][1].setPiece(Piece.KYO, WHITE)
        board.cells[1][0].setPiece(Piece.KEI, WHITE)
        board.cells[1][1].setPiece(Piece.KEI, WHITE)
        board.cells[1][2].setPiece(Piece.KEI, BLACK)
        board.cells[3][3].setPiece(Piece.KAKU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(BLACK))
    }

    /**
     * 王手判定
     * 条件：持ち駒を打って防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteHandPiece() {
        board.cells[0][0].setPiece(Piece.GYOKU, WHITE)
        board.cells[1][0].setPiece(Piece.KYO, WHITE)
        board.cells[1][1].setPiece(Piece.KYO, WHITE)
        board.cells[0][2].setPiece(Piece.KYO, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：王が逃げて逃れることができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackEscapeTrue() {
        board.cells[4][7].setPiece(Piece.OU, BLACK)
        board.cells[4][6].setPiece(Piece.KIN, WHITE)
        board.cells[4][5].setPiece(Piece.KIN, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：逃げようとすると王手になるから詰み
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackEscapeFalse() {
        board.cells[4][8].setPiece(Piece.OU, BLACK)
        board.cells[4][7].setPiece(Piece.KIN, WHITE)
        board.cells[4][6].setPiece(Piece.KIN, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val log = mutableListOf(GameLog(4, 7, Piece.KIN, WHITE, 4, 7, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(WHITE))
    }

    /**
     * 王手判定
     * 条件：コマを動かして防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackMoveTrue() {
        board.cells[0][8].setPiece(Piece.OU, BLACK)
        board.cells[1][8].setPiece(Piece.KYO, BLACK)
        board.cells[1][7].setPiece(Piece.KIN, BLACK)
        board.cells[4][6].setPiece(Piece.KYO, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：コマを動かしても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackMoveFalse() {
        board.cells[0][8].setPiece(Piece.OU, BLACK)
        board.cells[1][8].setPiece(Piece.KYO, BLACK)
        board.cells[1][7].setPiece(Piece.KIN, BLACK)
        board.cells[0][5].setPiece(Piece.KYO, WHITE)
        board.cells[3][5].setPiece(Piece.KAKU, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(WHITE))
    }

    /**
     * 王手判定
     * 条件：駒をとって防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackTakeTrue() {
        board.cells[0][8].setPiece(Piece.OU, BLACK)
        board.cells[0][7].setPiece(Piece.KYO, BLACK)
        board.cells[1][8].setPiece(Piece.KEI, BLACK)
        board.cells[1][7].setPiece(Piece.KIN, BLACK)
        board.cells[1][6].setPiece(Piece.KEI, WHITE)
        board.cells[8][0].setPiece(Piece.GYOKU, WHITE)
        val useCase = SyogiLogicUseCaseImp()
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    /**
     * 王手判定
     * 条件：駒をとっても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackTakeFalse() {
        board.cells[0][8].setPiece(Piece.GYOKU, BLACK)
        board.cells[0][7].setPiece(Piece.KYO, BLACK)
        board.cells[1][8].setPiece(Piece.KEI, BLACK)
        board.cells[1][7].setPiece(Piece.GIN, BLACK)
        board.cells[1][6].setPiece(Piece.KEI, WHITE)
        board.cells[3][5].setPiece(Piece.KAKU, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Win(WHITE))
    }

    /**
     * 王手判定
     * 条件：持ち駒を打って防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackHandPiece() {
        board.cells[0][8].setPiece(Piece.GYOKU, BLACK)
        board.cells[1][8].setPiece(Piece.KYO, BLACK)
        board.cells[1][7].setPiece(Piece.KYO, BLACK)
        board.cells[0][6].setPiece(Piece.KYO, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, GameResult.Continue)
    }

    // endregion

    // region 盤上の駒の動かせる場所
    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(周りなし)をタップ
     * 期待結果：8回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintCenter() {
        board.cells[4][4].setPiece(Piece.OU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 8)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：左上の王(周りなし)をタップ
     * 期待結果：3回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintTop() {
        board.cells[8][0].setPiece(Piece.OU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(9, 1)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 3)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：右下端の王(周りなし)をタップ
     * 期待結果：3回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintBottom() {
        board.cells[8][8].setPiece(Piece.OU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(9, 9)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 3)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(周り全部自分の駒)をタップ
     * 期待結果：１回もsetHint()が呼ばれない
     */
    @Test
    fun setTouchHintCenter2() {
        board.cells[3][3].setPiece(Piece.FU, BLACK)
        board.cells[3][4].setPiece(Piece.KIN, BLACK)
        board.cells[3][5].setPiece(Piece.GIN, BLACK)
        board.cells[4][3].setPiece(Piece.FU, BLACK)
        board.cells[4][4].setPiece(Piece.OU, BLACK)
        board.cells[4][5].setPiece(Piece.KEI, BLACK)
        board.cells[5][3].setPiece(Piece.FU, BLACK)
        board.cells[5][4].setPiece(Piece.KIN, BLACK)
        board.cells[5][5].setPiece(Piece.GIN, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 0)
    }


    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(動かした先が全て王手になる)をタップ
     * 期待結果：setHint()が呼ばれない
     */
    @Test
    fun setTouchHintCenter3() {
        board.cells[4][2].setPiece(Piece.KEI, WHITE)
        board.cells[4][4].setPiece(Piece.OU, BLACK)
        board.cells[2][3].setPiece(Piece.HISYA, WHITE)
        board.cells[4][6].setPiece(Piece.KAKU, WHITE)
        board.cells[5][6].setPiece(Piece.GIN, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 0)
    }

    // endregion

    // region 持ち駒の打てる場所
    /**
     * 持ち駒の打てる場所判定関数
     * 条件；歩をタップした場合
     * 結果：72回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceFU() {
        board.cells[5][5].setPiece(Piece.OU, WHITE)
        board.cells[1][1].setPiece(Piece.GIN, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(2, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 70)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；全ての行に歩がある時に歩をタップした場合
     * 結果：0回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceFU2() {
        board.cells[0][1].setPiece(Piece.FU, BLACK)
        board.cells[1][1].setPiece(Piece.FU, BLACK)
        board.cells[2][2].setPiece(Piece.FU, BLACK)
        board.cells[3][3].setPiece(Piece.FU, BLACK)
        board.cells[4][4].setPiece(Piece.FU, BLACK)
        board.cells[5][5].setPiece(Piece.FU, BLACK)
        board.cells[6][6].setPiece(Piece.FU, BLACK)
        board.cells[7][7].setPiece(Piece.FU, BLACK)
        board.cells[8][8].setPiece(Piece.FU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setHintHoldPiece(2, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 0)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：79回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceOther() {
        board.cells[5][5].setPiece(Piece.OU, BLACK)
        board.cells[1][1].setPiece(Piece.GIN, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；香車をタップした場合
     * 結果：63回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKYO() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(3, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 72)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；桂馬をタップした場合
     * 結果：63回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKEI() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(4, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 63)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；銀をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceGIN() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 81)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKIN() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 81)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；飛車をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceHISYA() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 81)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；角をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKAKU() {
        val useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 81)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；空をタップした場合
     * 結果：0回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceNone() {
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setHintHoldPiece(5, 10, BLACK)
        var hintCount = 0
        for (i in 0 until COLS) for (j in 0 until ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        assertEquals(hintCount, 0)
    }

    // endregion

    // region 駒を動かす
    /**
     * 駒を動かす関数
     * 結果：下記のメソッドを呼ぶ
     * ・駒を動かすBoardRepositoryメソッド
     * ・持ち駒をセットするBoardRepositoryメソッド
     * ・ヒントをリセットするメソッド
     */
    @Test
    fun setMove() {
        val useCase = SyogiLogicUseCaseImp()
        val previousX: Field = useCase.javaClass.getDeclaredField("previousX")
        previousX.isAccessible = true
        previousX.set(useCase, 1)
        val previousY: Field = useCase.javaClass.getDeclaredField("previousY")
        previousY.isAccessible = true
        previousY.set(useCase, 1)
        val previousPiece: Field = useCase.javaClass.getDeclaredField("previousPiece")
        previousPiece.isAccessible = true
        previousPiece.set(useCase, Piece.FU)
        // 実行
        useCase.setMove(3, 3, false)
        val cell = useCase.getCellInformation(2, 2)
        assertEquals(cell.piece, Piece.FU)
    }

    // endregion

    // region 成り系メソッド

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが自陣から敵陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByUpY() {
        // テストクラス作成
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(2, 7, Piece.HISYA, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが敵陣から自陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByDownY() {
        board.cells[2][7].setPiece(Piece.HISYA, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, BLACK, 2, 7, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが敵陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByStayY1() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(7, 2, Piece.HISYA, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが自陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByStayY2() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(7, 6, Piece.HISYA, BLACK, 2, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, false)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが敵陣から自陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByUpY() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, WHITE, 2, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが自陣から敵陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByDownY() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(2, 7, Piece.HISYA, WHITE, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが自陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByStayY1() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, WHITE, 7, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, false)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが敵陣内で移動した時
     * 結果：trueを返す
     */
    @Test
    fun evolutionCheckWhiteByStayY2() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(2, 6, Piece.HISYA, WHITE, 7, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution()
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で歩を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckFU() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.FU, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で桂馬を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckKYO1() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.KYO, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で桂馬を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckKYO2() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.KYO, BLACK, 2, 1, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で桂馬を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckKEI1() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.KEI, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で桂馬を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckKEI2() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.KEI, BLACK, 2, 1, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で銀を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckGIN() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.GIN, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で金を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckKIN() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(6, 2, Piece.KIN, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
    }


    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で飛車を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckHISYA() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(7, 2, Piece.HISYA, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で角を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckKAKU() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(7, 2, Piece.KAKU, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
    }


    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で王を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckOU() {
        val useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(7, 2, Piece.OU, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
    }

    // endregion

    // region 指定したますの情報取得
    /**
     * 指定したマスの手番情報を返す関数
     * 条件：自分の駒あり、ヒントなしのマスを検索する
     * 結果；手番＝自分、が返る
     */
    @Test
    fun getCellTurnBLACKNoHint() {
        board.cells[4][4].setPiece(Piece.FU, BLACK)
        board.cells[4][4].hint = false
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 1)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：自分の駒あり、ヒントなしのマスを検索する
     * 結果；手番＝ヒント、が返る
     */
    @Test
    fun getCellTurnBLACKAndHint() {
        board.cells[4][4].setPiece(Piece.FU, BLACK)
        board.cells[4][4].hint = true
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 3)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：自分の駒あり、ヒントなしのマスを検索する
     * 結果；手番＝自分、が返る
     */
    @Test
    fun getCellTurnWHITENoHint() {
        board.cells[4][4].setPiece(Piece.FU, WHITE)
        board.cells[4][4].hint = false
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 2)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：相手の駒あり、ヒントなしのマスを検索する
     * 結果；手番＝ヒント、が返る
     */
    @Test
    fun getCellTurnWHITEAndHint() {
        board.cells[4][4].setPiece(Piece.FU, WHITE)
        board.cells[4][4].hint = true
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 3)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：駒なし、ヒントなしのマスを検索する
     * 結果；手番＝なし、が返る
     */
    @Test
    fun getCellTurnNoneNoHint() {
        val useCase = SyogiLogicUseCaseImp()
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 4)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：駒なし、ヒントなしのマスを検索する
     * 結果；手番＝なし、が返る
     */
    @Test
    fun getCellTurnNoneAndHint() {
        board.cells[4][4].setNone()
        board.cells[4][4].hint = false
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 4)
    }

    // endregion

    // region 千日手
    /**
     * 千日手判定を行う関数
     * 条件：千日手を行った(同じ局面に4回なった)
     * 結果：trueを返す
     */
    @Test
    fun isRepetitionMoveTrue() {
        val useCase = SyogiLogicUseCaseImp()
        // 実行
        useCase.setMove(3, 3, false)
        useCase.setMove(3, 3, false)
        useCase.setMove(3, 3, false)
        useCase.setMove(3, 3, false)
        val result = useCase.isRepetitionMove()
        assertEquals(result, true)
    }

    /**
     * 千日手判定を行う関数
     * 条件：千日手を行っていない(同じ局面に4回遭遇していない)
     * 結果：falseを返す
     */
    @Test
    fun isRepetitionMoveFalse() {
        val useCase = SyogiLogicUseCaseImp()
        // 実行
        useCase.setMove(3, 3, false)
        useCase.setMove(3, 3, false)
        useCase.setMove(3, 3, false)
        val result = useCase.isRepetitionMove()
        assertEquals(result, false)
    }

    // endregion

    // region トライルール
    /**
     * トライルール判定を行う関数
     * 条件：先手の王様がトライした
     * 結果：trueを返す
     */
    @Test
    fun isTryKingBlackTrue() {
        board.cells[4][0].setPiece(Piece.OU, BLACK)
        val useCase = SyogiLogicUseCaseImp(board)
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, true)
    }

    /**
     * トライルール判定を行う関数
     * 条件：先手の王様がトライしていない
     * 結果：falseを返す
     */
    @Test
    fun isTryKingBlackFalse() {
        val useCase = SyogiLogicUseCaseImp()
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, false)
    }

    /**
     * トライルール判定を行う関数
     * 条件：後手の王様がトライした
     * 結果：trueを返す
     */
    @Test
    fun isTryKingWhiteTrue() {
        board.cells[4][8].setPiece(Piece.OU, WHITE)
        val useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, true)
    }

    /**
     * トライルール判定を行う関数
     * 条件：後手の王様がトライしていない
     * 結果：falseを返す
     */
    @Test
    fun isTryKingWhiteFalse() {
        val useCase = SyogiLogicUseCaseImp()
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, false)
    }

    // endregion
}
