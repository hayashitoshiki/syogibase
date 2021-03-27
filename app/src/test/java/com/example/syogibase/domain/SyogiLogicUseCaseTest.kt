package com.example.syogibase.domain

import com.example.syogibase.data.entity.Board
import com.example.syogibase.data.entity.Board.Companion.COLS
import com.example.syogibase.data.entity.Board.Companion.ROWS
import com.example.syogibase.data.entity.GameLog
import com.example.syogibase.data.entity.Piece
import com.example.syogibase.data.value.GameResult
import com.example.syogibase.data.value.Turn.BLACK
import com.example.syogibase.data.value.Turn.WHITE
import com.example.syogibase.data.value.X
import com.example.syogibase.data.value.Y
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
    private lateinit var useCase: SyogiLogicUseCaseImp

    // ヒントカウント取得
    private fun findHintCount(): Int {
        var hintCount = 0
        for (i in 1..COLS) for (j in 1..ROWS) {
            val cell = useCase.getCellInformation(i, j)
            if (cell.hint) {
                hintCount++
            }
        }
        return hintCount
    }

    // region 詰み判定

    /**
     * 王手判定
     * 条件：王が逃げて逃れることができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteEscapeTrue() {
        board.getCell(5, 2).setPiece(Piece.OU, WHITE)
        board.getCell(5, 3).setPiece(Piece.KIN, BLACK)
        board.getCell(5, 4).setPiece(Piece.KIN, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(5, 1).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        board.getCell(5, 2).setPiece(Piece.KIN, BLACK)
        board.getCell(5, 3).setPiece(Piece.KIN, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 1).setPiece(Piece.OU, WHITE)
        board.getCell(2, 1).setPiece(Piece.KYO, WHITE)
        board.getCell(2, 2).setPiece(Piece.KIN, WHITE)
        board.getCell(5, 3).setPiece(Piece.KYO, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(1, 1).setPiece(Piece.OU, WHITE)
        board.getCell(2, 1).setPiece(Piece.KYO, WHITE)
        board.getCell(2, 2).setPiece(Piece.KIN, WHITE)
        board.getCell(1, 4).setPiece(Piece.KYO, BLACK)
        board.getCell(4, 4).setPiece(Piece.KAKU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 1).setPiece(Piece.OU, WHITE)
        board.getCell(1, 2).setPiece(Piece.KYO, WHITE)
        board.getCell(2, 1).setPiece(Piece.KEI, WHITE)
        board.getCell(2, 2).setPiece(Piece.KIN, WHITE)
        board.getCell(1, 3).setPiece(Piece.KYO, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 1).setPiece(Piece.GYOKU, WHITE)
        board.getCell(1, 2).setPiece(Piece.KYO, WHITE)
        board.getCell(2, 1).setPiece(Piece.KEI, WHITE)
        board.getCell(2, 2).setPiece(Piece.KEI, WHITE)
        board.getCell(2, 3).setPiece(Piece.KEI, BLACK)
        board.getCell(4, 4).setPiece(Piece.KAKU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 1).setPiece(Piece.GYOKU, WHITE)
        board.getCell(2, 1).setPiece(Piece.KYO, WHITE)
        board.getCell(2, 2).setPiece(Piece.KYO, WHITE)
        board.getCell(1, 3).setPiece(Piece.KYO, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(5, 8).setPiece(Piece.OU, BLACK)
        board.getCell(5, 7).setPiece(Piece.KIN, WHITE)
        board.getCell(5, 6).setPiece(Piece.KIN, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 8).setPiece(Piece.KIN, WHITE)
        board.getCell(5, 7).setPiece(Piece.KIN, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val log =
            mutableListOf(GameLog(X(4), Y(7), Piece.KIN, WHITE, X(4), Y(7), null, null, false))
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
        board.getCell(1, 9).setPiece(Piece.OU, BLACK)
        board.getCell(2, 9).setPiece(Piece.KYO, BLACK)
        board.getCell(2, 8).setPiece(Piece.KIN, BLACK)
        board.getCell(5, 7).setPiece(Piece.KYO, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 9).setPiece(Piece.OU, BLACK)
        board.getCell(2, 9).setPiece(Piece.KYO, BLACK)
        board.getCell(2, 8).setPiece(Piece.KIN, BLACK)
        board.getCell(1, 6).setPiece(Piece.KYO, WHITE)
        board.getCell(4, 6).setPiece(Piece.KAKU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 9).setPiece(Piece.OU, BLACK)
        board.getCell(1, 8).setPiece(Piece.KYO, BLACK)
        board.getCell(2, 9).setPiece(Piece.KEI, BLACK)
        board.getCell(2, 8).setPiece(Piece.KIN, BLACK)
        board.getCell(2, 7).setPiece(Piece.KEI, WHITE)
        board.getCell(9, 1).setPiece(Piece.GYOKU, WHITE)
        useCase = SyogiLogicUseCaseImp()
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
        board.getCell(1, 9).setPiece(Piece.GYOKU, BLACK)
        board.getCell(1, 8).setPiece(Piece.KYO, BLACK)
        board.getCell(2, 9).setPiece(Piece.KEI, BLACK)
        board.getCell(2, 8).setPiece(Piece.GIN, BLACK)
        board.getCell(2, 7).setPiece(Piece.KEI, WHITE)
        board.getCell(4, 6).setPiece(Piece.KAKU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(1, 9).setPiece(Piece.GYOKU, BLACK)
        board.getCell(2, 9).setPiece(Piece.KYO, BLACK)
        board.getCell(2, 8).setPiece(Piece.KYO, BLACK)
        board.getCell(1, 7).setPiece(Piece.KYO, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        board.getCell(5, 5).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        val hintCount = findHintCount()
        assertEquals(hintCount, 8)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：左上の王(周りなし)をタップ
     * 期待結果：3回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintTop() {
        board.getCell(9, 1).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(9, 1)
        val hintCount = findHintCount()
        assertEquals(hintCount, 3)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：右下端の王(周りなし)をタップ
     * 期待結果：3回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintBottom() {
        board.getCell(9, 9).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(9, 9)
        val hintCount = findHintCount()
        assertEquals(hintCount, 3)
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(周り全部自分の駒)をタップ
     * 期待結果：１回もsetHint()が呼ばれない
     */
    @Test
    fun setTouchHintCenter2() {
        board.getCell(4, 4).setPiece(Piece.FU, BLACK)
        board.getCell(4, 5).setPiece(Piece.KIN, BLACK)
        board.getCell(4, 6).setPiece(Piece.GIN, BLACK)
        board.getCell(5, 4).setPiece(Piece.FU, BLACK)
        board.getCell(5, 5).setPiece(Piece.OU, BLACK)
        board.getCell(5, 6).setPiece(Piece.KEI, BLACK)
        board.getCell(6, 4).setPiece(Piece.FU, BLACK)
        board.getCell(6, 5).setPiece(Piece.KIN, BLACK)
        board.getCell(6, 6).setPiece(Piece.GIN, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        val hintCount = findHintCount()
        assertEquals(hintCount, 0)
    }


    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(動かした先が全て王手になる)をタップ
     * 期待結果：setHint()が呼ばれない
     */
    @Test
    fun setTouchHintCenter3() {
        board.getCell(5, 3).setPiece(Piece.KEI, WHITE)
        board.getCell(5, 5).setPiece(Piece.OU, BLACK)
        board.getCell(3, 4).setPiece(Piece.HISYA, WHITE)
        board.getCell(5, 7).setPiece(Piece.KAKU, WHITE)
        board.getCell(6, 7).setPiece(Piece.GIN, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setTouchHint(5, 5)
        val hintCount = findHintCount()
        assertEquals(hintCount, 0)
    }

    // endregion

    // region 持ち駒の打てる場所
    /**
     * 持ち駒の打てる場所判定関数
     * 条件；歩をタップした場合
     * 結果：71マス置くことができる
     */
    @Test
    fun getHintHoldPieceFU() {
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.FU, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 71)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；全ての行に歩がある時に歩をタップした場合
     * 結果：１マスも置くことができない
     */
    @Test
    fun getHintHoldPieceFU2() {
        board.getCell(1, 2).setPiece(Piece.FU, BLACK)
        board.getCell(2, 2).setPiece(Piece.FU, BLACK)
        board.getCell(3, 3).setPiece(Piece.FU, BLACK)
        board.getCell(4, 4).setPiece(Piece.FU, BLACK)
        board.getCell(5, 5).setPiece(Piece.FU, BLACK)
        board.getCell(6, 6).setPiece(Piece.FU, BLACK)
        board.getCell(7, 7).setPiece(Piece.FU, BLACK)
        board.getCell(8, 8).setPiece(Piece.FU, BLACK)
        board.getCell(9, 9).setPiece(Piece.FU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setHintHoldPiece(Piece.FU, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 0)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：79マス置くことができる
     */
    @Test
    fun getHintHoldPieceOther() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.GIN, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.KIN, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；香車をタップした場合
     * 結果：71回マス置くことができる
     */
    @Test
    fun getHintHoldPieceKYO() {
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.KYO, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 71)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；桂馬をタップした場合
     * 結果：62マス置くことができる
     */
    @Test
    fun getHintHoldPieceKEI() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.KEI, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 62)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；銀をタップした場合
     * 結果：79マス置くことができる
     */
    @Test
    fun getHintHoldPieceGIN() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.GIN, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：79マス置くことができる
     *
     */
    @Test
    fun getHintHoldPieceKIN() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.KIN, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；飛車をタップした場合
     * 結果：79マス置くことができる
     */
    @Test
    fun getHintHoldPieceHISYA() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.HISYA, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；角をタップした場合
     * 結果：79マス置くことができる
     */
    @Test
    fun getHintHoldPieceKAKU() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        useCase.setHoldPiece(holdPiece, BLACK)
        // 実行
        useCase.setHintHoldPiece(Piece.KAKU, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 79)
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；空をタップした場合
     * 結果：１マスも置くことができない
     */
    @Test
    fun getHintHoldPieceNone() {
        board.getCell(5, 9).setPiece(Piece.OU, BLACK)
        board.getCell(5, 1).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        // 実行
        useCase.setHintHoldPiece(Piece.KIN, BLACK)
        val hintCount = findHintCount()
        assertEquals(hintCount, 0)
    }

    // endregion

    // region 駒を動かす
    /**
     * 駒を動かす関数
     * 結果：3三に金が設定されていること
     */
    @Test
    fun setMove() {
        board.getCell(2, 2).setPiece(Piece.OU, BLACK)
        board.getCell(4, 3).setPiece(Piece.KIN, BLACK)
        board.getCell(9, 9).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
        val previousX: Field = useCase.javaClass.getDeclaredField("previousX")
        previousX.isAccessible = true
        previousX.set(useCase, 4)
        val previousY: Field = useCase.javaClass.getDeclaredField("previousY")
        previousY.isAccessible = true
        previousY.set(useCase, 3)
        val previousPiece: Field = useCase.javaClass.getDeclaredField("previousPiece")
        previousPiece.isAccessible = true
        previousPiece.set(useCase, Piece.KIN)
        // 実行
        useCase.setMove(3, 3, false)
        val cell = useCase.getCellInformation(3, 3)
        assertEquals(cell.piece, Piece.KIN)
        assertEquals(cell.turn, BLACK)
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(2), Y(7), Piece.HISYA, BLACK, X(2), Y(2), null, null, false))
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
        board.getCell(2, 7).setPiece(Piece.HISYA, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
        val log =
            mutableListOf(GameLog(X(2), Y(3), Piece.HISYA, BLACK, X(2), Y(7), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(7), Y(2), Piece.HISYA, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(7), Y(6), Piece.HISYA, BLACK, X(2), Y(6), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(2), Y(3), Piece.HISYA, WHITE, X(2), Y(7), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(2), Y(7), Piece.HISYA, WHITE, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(2), Y(2), Piece.HISYA, WHITE, X(7), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(2), Y(7), Piece.HISYA, WHITE, X(7), Y(7), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(X(6), Y(2), Piece.FU, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.KYO, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.KYO, BLACK, X(2), Y(1), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.KEI, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.KEI, BLACK, X(2), Y(1), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.GIN, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(6), Y(2), Piece.KIN, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(7), Y(2), Piece.HISYA, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log =
            mutableListOf(GameLog(X(7), Y(2), Piece.KAKU, BLACK, X(2), Y(2), null, null, false))
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
        useCase = SyogiLogicUseCaseImp()
        val log = mutableListOf(GameLog(X(7), Y(2), Piece.OU, BLACK, X(2), Y(2), null, null, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
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
        useCase = SyogiLogicUseCaseImp()
        // 実行
        val previousX: Field = useCase.javaClass.getDeclaredField("previousX")
        previousX.isAccessible = true
        previousX.set(useCase, 3)
        val previousY: Field = useCase.javaClass.getDeclaredField("previousY")
        previousY.isAccessible = true
        previousY.set(useCase, 2)
        val previousPiece: Field = useCase.javaClass.getDeclaredField("previousPiece")
        previousPiece.isAccessible = true
        previousPiece.set(useCase, Piece.KIN)
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
        useCase = SyogiLogicUseCaseImp()
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
        board.getCell(5, 1).setPiece(Piece.OU, BLACK)
        useCase = SyogiLogicUseCaseImp(board)
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
        useCase = SyogiLogicUseCaseImp()
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
        board.getCell(5, 9).setPiece(Piece.OU, WHITE)
        useCase = SyogiLogicUseCaseImp(board)
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
        useCase = SyogiLogicUseCaseImp()
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, false)
    }

    // endregion
}
