package com.example.syogibase.domain

import com.example.syogibase.data.BoardRepository
import com.example.syogibase.data.BoardRepositoryImp
import com.example.syogibase.data.local.Board
import com.example.syogibase.data.local.Cell
import com.example.syogibase.data.local.GameLog
import com.example.syogibase.data.local.Piece
import com.example.syogibase.util.BLACK
import com.example.syogibase.util.BLACK_HOLD
import com.example.syogibase.util.WHITE
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import java.lang.reflect.Field

class SyogiLogicUseCaseTest {


    @Before
    fun setUp() {
    }

    // region 詰み判定

    /**
     * 王手判定
     * 条件：王が逃げて逃れることができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteEscapeTrue() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][1].piece = Piece.OU
        cells[4][1].turn = WHITE
        cells[4][2].piece = Piece.KIN
        cells[4][2].turn = BLACK
        cells[4][3].piece = Piece.KIN
        cells[4][3].turn = BLACK
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：逃げようとすると王手になるから詰み
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteEscapeFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][0].piece = Piece.OU
        cells[4][0].turn = 2
        cells[4][1].piece = Piece.KIN
        cells[4][1].turn = 1
        cells[4][2].piece = Piece.KIN
        cells[4][2].turn = 1
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：コマを動かして防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteMoveTrue() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][0].piece = Piece.OU
        cells[0][0].turn = WHITE
        cells[1][0].piece = Piece.KYO
        cells[1][0].turn = WHITE
        cells[1][1].piece = Piece.KIN
        cells[1][1].turn = WHITE
        cells[4][2].piece = Piece.KYO
        cells[4][2].turn = BLACK
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：コマを動かしても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteMoveFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][0].piece = Piece.OU
        cells[0][0].turn = 2
        cells[1][0].piece = Piece.KYO
        cells[1][0].turn = 2
        cells[1][1].piece = Piece.KIN
        cells[1][1].turn = 2
        cells[0][3].piece = Piece.KYO
        cells[0][3].turn = 1
        cells[3][3].piece = Piece.KAKU
        cells[3][3].turn = 1
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：駒をとって防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteTakeTrue() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][0].piece = Piece.OU
        cells[0][0].turn = 2
        cells[0][1].piece = Piece.KYO
        cells[1][1].turn = 2
        cells[1][0].piece = Piece.KEI
        cells[1][0].turn = 2
        cells[1][1].piece = Piece.KIN
        cells[1][1].turn = 2
        cells[1][2].piece = Piece.KYO
        cells[1][2].turn = 1
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：駒をとっても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateWhiteTakeFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][0].piece = Piece.GYOKU
        cells[0][0].turn = 2
        cells[0][1].piece = Piece.KYO
        cells[0][1].turn = 2
        cells[1][0].piece = Piece.KEI
        cells[1][0].turn = 2
        cells[1][1].piece = Piece.KEI
        cells[1][1].turn = 2
        cells[1][2].piece = Piece.KEI
        cells[1][2].turn = 1
        cells[3][3].piece = Piece.KAKU
        cells[3][3].turn = 1
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：持ち駒を打って防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateWhiteHandPiece() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][0].piece = Piece.GYOKU
        cells[0][0].turn = 2
        cells[1][0].piece = Piece.KYO
        cells[1][0].turn = 2
        cells[1][1].piece = Piece.KYO
        cells[1][1].turn = 2
        cells[0][2].piece = Piece.KYO
        cells[0][2].turn = 1
        val holdPieceWhite = mutableMapOf(
            Piece.FU to 1,
            Piece.KYO to 1,
            Piece.KEI to 1,
            Piece.GIN to 1,
            Piece.KIN to 1,
            Piece.KAKU to 1,
            Piece.HISYA to 1
        )
        doReturn(holdPieceWhite).whenever(repository).getAllHoldPiece(any())
        doReturn(Piece.KIN).whenever(repository).findHoldPieceBy(any(), any())
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：王が逃げて逃れることができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackEscapeTrue() {
        // 王手（逃げるのみ）
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][7].piece = Piece.OU
        cells[4][7].turn = BLACK
        cells[4][6].piece = Piece.KIN
        cells[4][6].turn = WHITE
        cells[4][5].piece = Piece.KIN
        cells[4][5].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：逃げようとすると王手になるから詰み
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackEscapeFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][8].piece = Piece.OU
        cells[4][8].turn = BLACK
        cells[4][7].piece = Piece.KIN
        cells[4][7].turn = WHITE
        cells[4][6].piece = Piece.KIN
        cells[4][6].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val log = mutableListOf(GameLog(4, 7, Piece.KIN, WHITE, 4, 7, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：コマを動かして防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackMoveTrue() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][8].piece = Piece.OU
        cells[0][8].turn = BLACK
        cells[1][8].piece = Piece.KYO
        cells[1][8].turn = BLACK
        cells[1][7].piece = Piece.KIN
        cells[1][7].turn = BLACK
        cells[4][6].piece = Piece.KYO
        cells[4][6].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：コマを動かしても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackMoveFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][8].piece = Piece.OU
        cells[0][8].turn = BLACK
        cells[1][8].piece = Piece.KYO
        cells[1][8].turn = BLACK
        cells[1][7].piece = Piece.KIN
        cells[1][7].turn = BLACK
        cells[0][5].piece = Piece.KYO
        cells[0][5].turn = WHITE
        cells[3][5].piece = Piece.KAKU
        cells[3][5].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：駒をとって防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackTakeTrue() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][8].piece = Piece.OU
        cells[0][8].turn = BLACK
        cells[0][7].piece = Piece.KYO
        cells[1][7].turn = BLACK
        cells[1][8].piece = Piece.KEI
        cells[1][8].turn = BLACK
        cells[1][7].piece = Piece.KIN
        cells[1][7].turn = BLACK
        cells[1][6].piece = Piece.KYO
        cells[1][6].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
    }

    /**
     * 王手判定
     * 条件：駒をとっても防ぐことができない
     * 期待結果：isGameEnd()の戻り値がtrueになる
     */
    @Test
    fun isCheckmateBlackTakeFalse() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][8].piece = Piece.GYOKU
        cells[0][8].turn = BLACK
        cells[0][7].piece = Piece.KYO
        cells[0][7].turn = BLACK
        cells[1][8].piece = Piece.KEI
        cells[1][8].turn = BLACK
        cells[1][7].piece = Piece.GIN
        cells[1][7].turn = BLACK
        cells[1][6].piece = Piece.KEI
        cells[1][6].turn = WHITE
        cells[3][5].piece = Piece.KAKU
        cells[3][5].turn = WHITE
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, true)
    }

    /**
     * 王手判定
     * 条件：持ち駒を打って防ぐことができる
     * 期待結果：isGameEnd()の戻り値がfalseになる
     */
    @Test
    fun isCheckmateBlackHandPiece() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][8].piece = Piece.GYOKU
        cells[0][8].turn = BLACK
        cells[1][8].piece = Piece.KYO
        cells[1][8].turn = BLACK
        cells[1][7].piece = Piece.KYO
        cells[1][7].turn = BLACK
        cells[0][6].piece = Piece.KYO
        cells[0][6].turn = WHITE
        val holdPieceWhite = mutableMapOf(
            Piece.FU to 1,
            Piece.KYO to 1,
            Piece.KEI to 1,
            Piece.GIN to 1,
            Piece.KIN to 1,
            Piece.KAKU to 1,
            Piece.HISYA to 1
        )
        doReturn(holdPieceWhite).whenever(repository).getAllHoldPiece(any())
        doReturn(Piece.KIN).whenever(repository).findHoldPieceBy(any(), any())
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        val result = useCase.isGameEnd()
        assertEquals(result, false)
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
        val boarRepository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][4].piece = Piece.OU
        cells[4][4].turn = 1
        val useCase = SyogiLogicUseCaseImp(boarRepository)
        useCase.setBoard(cells)
        // 実行
        useCase.setTouchHint(5, 5)
        verify(boarRepository, times(8)).setHint(any(), any())
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：左上の王(周りなし)をタップ
     * 期待結果：3回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintTop() {
        val boarRepository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[8][0].piece = Piece.OU
        cells[8][0].turn = 1
        val useCase = SyogiLogicUseCaseImp(boarRepository)
        useCase.setBoard(cells)
        // 実行
        useCase.setTouchHint(9, 1)
        verify(boarRepository, times(3)).setHint(any(), any())
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：右下端の王(周りなし)をタップ
     * 期待結果：setHint()が呼ばれない
     */
    @Test
    fun setTouchHintBottom() {
        val boarRepository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[8][8].piece = Piece.OU
        cells[8][8].turn = 1
        val useCase = SyogiLogicUseCaseImp(boarRepository)
        useCase.setBoard(cells)
        // 実行
        useCase.setTouchHint(9, 9)
        verify(boarRepository, times(3)).setHint(any(), any())
    }

    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(周り全部自分の駒)をタップ
     * 期待結果：8回setHint()が呼ばれる
     */
    @Test
    fun setTouchHintCenter2() {
        val boarRepository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[3][3].piece = Piece.FU
        cells[3][3].turn = 1
        cells[3][4].piece = Piece.KIN
        cells[3][4].turn = 1
        cells[3][5].piece = Piece.GIN
        cells[3][5].turn = 1
        cells[4][3].piece = Piece.FU
        cells[4][3].turn = 1
        cells[4][4].piece = Piece.OU
        cells[4][4].turn = 1
        cells[4][5].piece = Piece.KEI
        cells[4][5].turn = 1
        cells[5][3].piece = Piece.FU
        cells[5][3].turn = 1
        cells[5][4].piece = Piece.KIN
        cells[5][4].turn = 1
        cells[5][5].piece = Piece.GIN
        cells[5][5].turn = 1
        val useCase = SyogiLogicUseCaseImp(boarRepository)
        useCase.setBoard(cells)
        // 実行
        useCase.setTouchHint(5, 5)
        verify(boarRepository, never()).setHint(any(), any())
    }


    /**
     * 盤上の自分の駒をタップしたときにヒントを表示する
     * 条件：中央の王(動かした先が全て王手になる)をタップ
     * 期待結果：setHint()が呼ばれない
     */
    @Test
    fun setTouchHintCenter3() {
        val boarRepository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][2].piece = Piece.KEI
        cells[4][2].turn = 2
        cells[4][4].piece = Piece.OU
        cells[4][4].turn = 1
        cells[2][3].piece = Piece.HISYA
        cells[2][3].turn = 2
        cells[4][6].piece = Piece.KAKU
        cells[4][6].turn = 2
        cells[5][6].piece = Piece.GIN
        cells[5][6].turn = 2
        val useCase = SyogiLogicUseCaseImp(boarRepository)
        useCase.setBoard(cells)
        // 実行
        useCase.setTouchHint(5, 5)
        verify(boarRepository, never()).setHint(any(), any())
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
        val cell = Cell()
        val repository = mock<BoardRepository> {
            on { findHoldPieceBy(anyInt(), anyInt()) } doReturn Piece.FU
            on { findKing(anyInt()) } doReturn Pair(5, 5)
            on { getPiece(anyInt(), anyInt()) } doReturn Piece.None
            on { getTurn(any(), any()) } doReturn 0
            on { getCountByHint() } doReturn 2
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
            on { changeIntToPiece(anyInt()) } doReturn Piece.FU
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(72)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；全ての行に歩がある時に歩をタップした場合
     * 結果：0回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceFU2() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[0][1].piece = Piece.FU
        cells[0][1].turn = 1
        cells[1][1].piece = Piece.FU
        cells[1][1].turn = 1
        cells[2][2].piece = Piece.FU
        cells[2][2].turn = 1
        cells[3][3].piece = Piece.FU
        cells[3][3].turn = 1
        cells[4][4].piece = Piece.FU
        cells[4][4].turn = 1
        cells[5][5].piece = Piece.FU
        cells[5][5].turn = 1
        cells[6][6].piece = Piece.FU
        cells[6][6].turn = 1
        cells[7][7].piece = Piece.FU
        cells[7][7].turn = 1
        cells[8][8].piece = Piece.FU
        cells[8][8].turn = 1
        doReturn(Piece.FU).whenever(repository).findHoldPieceBy(any(), any())
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(2, BLACK_HOLD, BLACK)
        verify(repository, never()).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：79回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceOther() {
        val repository = spy(BoardRepositoryImp())
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[5][5].piece = Piece.OU
        cells[5][5].turn = 1
        cells[1][1].piece = Piece.GIN
        cells[1][1].turn = 2

        doReturn(Piece.KIN).whenever(repository).findHoldPieceBy(any(), any())
        val useCase = SyogiLogicUseCaseImp(repository)
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(79)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；香車をタップした場合
     * 結果：63回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKYO() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.KYO).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(72)).setHint(anyInt(), anyInt())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；桂馬をタップした場合
     * 結果：63回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKEI() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.KEI).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(63)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；銀をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceGIN() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.GIN).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(81)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；金をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKIN() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.KIN).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(81)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；飛車をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceHISYA() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.HISYA).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(81)).setHint(any(), any())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；角をタップした場合
     * 結果：81回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceKAKU() {
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = spy(BoardRepositoryImp())
        val useCase = SyogiLogicUseCaseImp(repository)
        doReturn(Piece.KAKU).whenever(repository).findHoldPieceBy(any(), any())
        useCase.setBoard(cells)
        // 実行
        useCase.setHintHoldPiece(5, BLACK_HOLD, BLACK)
        verify(repository, times(81)).setHint(anyInt(), anyInt())
    }

    /**
     * 持ち駒の打てる場所判定関数
     * 条件；空をタップした場合
     * 結果：0回useCaseのsetHint()が呼ばれる
     */
    @Test
    fun getHintHoldPieceNone() {
        val repository = mock<BoardRepository> {
            on { findHoldPieceBy(anyInt(), anyInt()) } doReturn Piece.None
            on { findKing(anyInt()) } doReturn Pair(5, 5)
            on { getPiece(anyInt(), anyInt()) } doReturn Piece.None
        }

        val useCase = SyogiLogicUseCaseImp(repository)
        // 実行
        useCase.setHintHoldPiece(5, 10, BLACK)
        verify(repository, never()).setHint(any(), any())
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
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val cell = Cell()
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        // 実行
        useCase.setMove(3, 3, false)
        verify(repository, times(1)).setGoMove(any())
        verify(repository, times(1)).resetHint()
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
        val repository = mock<BoardRepository> {
            on { getPiece(2, 2) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 7, Piece.HISYA, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution(3, 3)
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが敵陣から自陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByDownY() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(2, 7) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, BLACK, 2, 7, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution(3, 8)
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが敵陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByStayY1() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(2, 2) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(7, 2, Piece.HISYA, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution(3, 3)
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：先手番でコマが自陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckBlackByStayY2() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(2, 6) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(7, 6, Piece.HISYA, BLACK, 2, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isEvolution(3, 7)
        assertEquals(result, false)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが敵陣から自陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByUpY() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(2, 6) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, WHITE, 2, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution(3, 7)
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが自陣から敵陣へ移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByDownY() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(2, 2) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 7, Piece.HISYA, WHITE, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution(3, 3)
        assertEquals(result, true)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが自陣内で移動した時
     * 結果：trueが返る
     */
    @Test
    fun evolutionCheckWhiteByStayY1() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(7, 2) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 2, Piece.HISYA, WHITE, 7, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution(8, 3)
        assertEquals(result, false)
    }

    /**
     * 成り判定メソッド
     * 条件：後手番でコマが敵陣内で移動した時
     * 結果：trueを返す
     */
    @Test
    fun evolutionCheckWhiteByStayY2() {
        // テストクラス作成
        val repository = mock<BoardRepository> {
            on { getPiece(7, 6) } doReturn Piece.HISYA
        }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(2, 6, Piece.HISYA, WHITE, 7, 6, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isEvolution(8, 7)
        assertEquals(result, true)
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で歩を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckFU() {
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(6, 2, Piece.FU, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, true)
        verify(repository, times(1)).setEvolution(any())
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で桂馬を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckKYO1() {
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(6, 2, Piece.GIN, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
        verify(repository, times(0)).setEvolution(any())
    }

    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で金を動かす
     * 結果：falseを返す
     */
    @Test
    fun compulsionEvolutionCheckKIN() {
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
        val log = mutableListOf(GameLog(6, 2, Piece.KIN, BLACK, 2, 2, Piece.None, 0, false))
        val logList: Field = useCase.javaClass.getDeclaredField("logList")
        logList.isAccessible = true
        logList.set(useCase, log)
        // 実行
        val result = useCase.isCompulsionEvolution()
        assertEquals(result, false)
        verify(repository, times(0)).setEvolution(any())
    }


    /**
     * 強制でならないといけないか判定するメソッド
     * 条件；成れる条件で飛車を動かす
     * 結果：trueを返す
     */
    @Test
    fun compulsionEvolutionCheckHISYA() {
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val repository = mock<BoardRepository> { }
        val useCase = SyogiLogicUseCaseImp(repository)
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
    fun getCellTrunBLACKNoHint() {
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.FU
        cell.turn = 1
        cell.hint = false
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
    fun getCellTrunBLACKAndHint() {
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.FU
        cell.turn = 1
        cell.hint = true
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
    fun getCellTrunWHITENoHint() {
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.FU
        cell.turn = 2
        cell.hint = false
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
    fun getCellTrunWHITEAndHint() {
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.FU
        cell.turn = 2
        cell.hint = true
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.None
        cell.turn = 0
        cell.hint = false
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 4)
    }

    /**
     * 指定したマスの手番情報を返す関数
     * 条件：駒なし、ヒントなしのマスを検索する
     * 結果；手番＝ヒント、が返る
     */
    @Test
    fun getCellTurnNoneAndHint() {
        // テストクラス作成
        val cell = Cell()
        cell.piece = Piece.None
        cell.turn = 0
        cell.hint = true
        val repository = mock<BoardRepository> {
            on { getCellInformation(4, 4) } doReturn cell
        }

        val useCase = SyogiLogicUseCaseImp(repository)
        // 実行
        val result = useCase.getCellTurn(5, 5)
        assertEquals(result, 3)
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
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(any(), any()) } doReturn cells[0][0]
        }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(any(), any()) } doReturn cells[0][0]
        }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][0].piece = Piece.OU
        cells[4][0].turn = BLACK
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(any(), any()) } doReturn cells[4][0]
        }
        val useCase = SyogiLogicUseCaseImp(repository)
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
        // テストクラス作成
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][5].piece = Piece.OU
        cells[4][5].turn = 1
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(any(), any()) } doReturn cells[4][0]
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
        // テストクラス作成
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][8].piece = Piece.OU
        cells[4][8].turn = 2
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(4, 8) } doReturn cells[4][8]
        }

        val useCase = SyogiLogicUseCaseImp(repository)
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
        // テストクラス作成
        val cells = Array(Board.COLS) { Array(Board.ROWS) { Cell() } }
        cells[4][5].piece = Piece.OU
        cells[4][5].turn = 2
        val repository = mock<BoardRepository> {
            on { getBoard() } doReturn cells
            on { getCellInformation(4, 8) } doReturn cells[4][8]
        }

        val useCase = SyogiLogicUseCaseImp(repository)
        val turn: Field = useCase.javaClass.getDeclaredField("turn")
        turn.isAccessible = true
        turn.set(useCase, WHITE)
        // 実行
        val result = useCase.isTryKing()
        assertEquals(result, false)
    }

    // endregion
}
