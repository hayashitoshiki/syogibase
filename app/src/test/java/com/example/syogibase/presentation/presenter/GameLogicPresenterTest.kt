package com.example.syogibase.presentation.presenter

import com.example.syogibase.domain.model.Cell
import com.example.syogibase.domain.model.Piece
import com.example.syogibase.domain.usecase.SyogiLogicUseCase
import com.example.syogibase.domain.usecase.SyogiLogicUseCaseImp
import com.example.syogibase.domain.value.BoardMode
import com.example.syogibase.domain.value.Turn.BLACK
import com.example.syogibase.domain.value.Turn.WHITE
import com.example.syogibase.presentation.contact.GameViewContact
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import java.lang.reflect.Field

class GameLogicPresenterTest {


    // region 盤面描画

    /**
     * 盤面初期化
     * 条件：幅より高さの方が長い
     * 期待値：ボード・先手の駒(20こ)・後手の駒(20こ)・駒台(上下)のみ描画されること
     */
    @Test
    fun drawViewByLongHeight() {
        val useCase = spy(SyogiLogicUseCaseImp())
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        presenter.drawView()
        verify(view, times(1)).drawBoard()
        verify(view, times(20)).drawBlackPiece(any(), anyInt(), anyInt())
        verify(view, times(20)).drawWhitePiece(any(), anyInt(), anyInt())
        verify(view, times(1)).drawVerticalStand()
        verify(view, never()).drawHoldPieceWhite(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawHoldPieceBlack(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawHorizontalStand()
    }

    /**
     * 盤面初期化
     * 条件：高さより幅の方が長い
     * 期待値：ボード・先手の駒(20こ)・後手の駒(20こ)・駒台(左右)のみ描画されること
     */
    @Test
    fun drawViewByLongWidth() {
        val useCase = spy(SyogiLogicUseCaseImp())
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        presenter.drawView()
        verify(view, times(1)).drawBoard()
        verify(view, times(20)).drawBlackPiece(any(), anyInt(), anyInt())
        verify(view, times(20)).drawWhitePiece(any(), anyInt(), anyInt())
        verify(view, times(1)).drawHorizontalStand()
        verify(view, never()).drawHoldPieceWhite(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawHoldPieceBlack(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawVerticalStand()
    }

    /**
     * 盤面描画
     * 条件：先手の持ち駒がある場合
     * 期待値：ボード・先手の駒・後手の駒・駒台・先手の駒のみ描画されること
     */
    @Test
    fun drawViewByBlackHold() {
        val holdPiece = mutableMapOf(
            Piece.FU to 1,
            Piece.KYO to 1,
            Piece.KEI to 1,
            Piece.GIN to 1,
            Piece.KIN to 1,
            Piece.KAKU to 1,
            Piece.HISYA to 1
        )
        val useCase = spy(SyogiLogicUseCaseImp())
        doReturn(holdPiece).whenever(useCase).getPieceHand(BLACK)
        useCase.getPieceHand(BLACK)
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        presenter.drawView()
        verify(view, times(1)).drawBoard()
        verify(view, times(20)).drawBlackPiece(any(), anyInt(), anyInt())
        verify(view, times(20)).drawWhitePiece(any(), anyInt(), anyInt())
        verify(view, times(1)).drawHorizontalStand()
        verify(view, times(7)).drawHoldPieceBlack(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawHoldPieceWhite(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawVerticalStand()
    }

    /**
     * 盤面描画
     * 条件：先手の持ち駒がある場合
     * 期待値：ボード・先手の駒・後手の駒・駒台・先手の駒のみ描画されること
     */
    @Test
    fun drawViewByWhiteHold() {
        val holdPiece = mutableMapOf(
            Piece.FU to 1,
            Piece.KYO to 1,
            Piece.KEI to 1,
            Piece.GIN to 1,
            Piece.KIN to 1,
            Piece.KAKU to 1,
            Piece.HISYA to 1
        )
        val useCase = spy(SyogiLogicUseCaseImp())
        doReturn(holdPiece).whenever(useCase).getPieceHand(WHITE)
        useCase.getPieceHand(WHITE)
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        presenter.drawView()
        verify(view, times(1)).drawBoard()
        verify(view, times(20)).drawBlackPiece(any(), anyInt(), anyInt())
        verify(view, times(20)).drawWhitePiece(any(), anyInt(), anyInt())
        verify(view, times(1)).drawHorizontalStand()
        verify(view, times(7)).drawHoldPieceWhite(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawHoldPieceBlack(any(), anyInt(), anyInt(), anyInt())
        verify(view, never()).drawVerticalStand()
    }

    // endregion

    // region マスのサイズ計算
    /**
     * 升目計算
     * 条件：幅が高さより長い & 幅がボード + 駒台文の長さより長い
     * 期待値：１マスのサイズがViewの高さの1/9になること
     */
    @Test
    fun computeCellSizeByLongWidthAndLongBoardStand() {
        val width = 11
        val height = 9
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.computeCellSize(width, height)
        verify(view, times(1)).setCellSize((width / 9).toFloat())
    }

    /**
     * 升目計算
     * 条件：幅が高さより長い & 幅がボード+駒台文の長さより短い
     * 期待値：１マスのサイズがViewの幅の1/11になること
     */
    @Test
    fun computeCellSizeByLongWidthAndShortBoardStand() {
        val width = 10
        val height = 9
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.computeCellSize(width, height)
        verify(view, times(1)).setCellSize((height / 11).toFloat())
    }

    /**
     * 升目計算
     * 条件：高さが幅より長い & 高さがボード + 駒台文の長さより長い
     * 期待値：１マスのサイズがViewの幅の1/9になること
     */
    @Test
    fun computeCellSizeByLongHeightAndLongBoardStand() {
        val width = 9
        val height = 11
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.computeCellSize(width, height)
        verify(view, times(1)).setCellSize((height / 9).toFloat())
    }

    /**
     * 升目計算
     * 条件：高さが幅より長い & 高さがボード+駒台文の長さより短い
     * 期待値：１マスのサイズがViewの高さの1/11になること
     */
    @Test
    fun computeCellSizeByLongHeightAndShortBoardStand() {
        val width = 9
        val height = 10
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.computeCellSize(width, height)
        verify(view, times(1)).setCellSize((width / 11).toFloat())
    }
    // endregion

    // region タッチイベント

    /**
     * 対局モードでのタッチイベント
     * 条件：対局モードで画面タッチ
     * 期待値：対局モードのイベント処理が呼ばれる
     */
    @Test
    fun onTouchByGameMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchEventLogic(5, 5, BoardMode.GAME)
        verify(view, times(1)).onTouchEventByGameMode(anyInt(), anyInt())
    }

    /**
     * 感想戦モードでのタッチイベント
     * 条件：感想戦モードで画面タッチ
     * 期待値：感想戦モードのイベント処理が呼ばれる
     */
    @Test
    fun onTouchByReplayMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchEventLogic(5, 5, BoardMode.REPLAY)
        verify(view, times(1)).onTouchEventByReplayMode(anyInt(), anyInt())
    }

    // region 感想戦モード
    /**
     * 感想戦モードでのタッチイベント
     * 条件：感想戦モードで画面左側をタッチ（タッチダウン）
     * 期待値：最初まで戻るメソッドが呼ばれる
     */
    @Test
    fun onTouchDownBoardLeftByReplayMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchDownEventByReplayModeLogic(4, 5)
        verify(view, times(1)).setLongJobByBack()
    }

    /**
     * 感想戦モードでのタッチイベント
     * 条件：感想戦モードで画面右側をタッチ（タッチダウン）
     * 期待値：最後まで進むメソッドが呼ばれる
     */
    @Test
    fun onTouchDownBoardRightByReplayMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchDownEventByReplayModeLogic(5, 5)
        verify(view, times(1)).setLongJobByGo()
    }

    /**
     * 感想戦モードでのタッチイベント
     * 条件：感想戦モードで画面左側をタッチ（タッチアップ）
     * 期待値：
     * ・１手戻るメソッドが呼ばれる
     * ・遅延処理の解除が呼ばれる
     */
    @Test
    fun onTouchUpBoardLeftByReplayMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchUpEventByReplayModeLogic(4, 5)
        verify(useCase, times(1)).setBackMove()
        verify(view, times(1)).cancelLongJob()
    }

    /**
     * 感想戦モードでのタッチイベント
     * 条件：感想戦モードで画面右側をタッチ（タッチアップ）
     * 期待値：
     * ・１手進むメソッドが呼ばれる
     * ・遅延処理の解除が呼ばれる
     */
    @Test
    fun onTouchUpBoardRightByReplayMode() {
        val useCase = mock<SyogiLogicUseCase> {}
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        presenter.onTouchUpEventByReplayModeLogic(5, 5)
        verify(view, times(1)).cancelLongJob()
    }

    // endregion

    // region 対局モード

    // region 駒台が上下にある場合

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で盤上をタッチ(1回目)
     * 期待値：
     * ・１回ごと指定の座標でヒントメソッドが呼ばれる
     * ・８１回ヒントメソッドが呼ばれる
     */
    @Test
    fun onTouchBoardByGameModeAndHorizontalStand() {
        val cell = Cell()
        val useCase = mock<SyogiLogicUseCase> {
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        for (i in 0..8) {
            for (j in 1..9) {
                presenter.onTouchEventByGameMode(i, j)
                verify(useCase, times(1)).setTouchHint(9 - i, j)
            }
        }
        verify(useCase, times(81)).setTouchHint(anyInt(), anyInt())
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で盤上をタッチ(2回目)
     * 期待値：
     *・１回ごと指定の座標でコマを動かすメソッドが呼ばれる
     * ・８１回コマを動かすメソッドが呼ばれる
     */
    @Test
    fun onTouchHintByGameModeAndHorizontalStand() {
        val cell = Cell()
        cell.hint = true
        val useCase = mock<SyogiLogicUseCase> {
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        for (i in 0..8) {
            for (j in 1..9) {
                presenter.onTouchEventByGameMode(i, j)
                verify(useCase, times(1)).setMove(9 - i, j, false)
            }
        }
        verify(useCase, times(81)).setMove(anyInt(), anyInt(), any())
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で先手が後手の駒台をタップ
     * 期待値：全てcancelメソッドが呼ばれる
     */
    @Test
    fun onTouchWhiteStandByGameModeAndHorizontalStandAndBlack() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn BLACK
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val y = 0
        for (x in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
            verify(useCase, times(x + 1)).cancel()
        }
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で先手が先手の駒台をタップ
     * 期待値：７回(歩、香、桂、銀、金、角、飛車)、持ち駒を使うメソッドが呼ばれること
     */
    @Test
    fun onTouchBlackStandByGameModeAndHorizontalStandAndBlack() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn BLACK
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val y = 10
        for (x in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
        }
        verify(useCase, times(1)).setHintHoldPiece(Piece.FU, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KYO, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KEI, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.GIN, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KIN, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KAKU, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.HISYA, BLACK)
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で後手が先手の駒台をタップ
     * 期待値：全てcancelメソッドが呼ばれる
     */
    @Test
    fun onTouchBlackStandByGameModeAndHorizontalStandAndWhite() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn WHITE
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val y = 10
        for (x in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
            verify(useCase, times(x + 1)).cancel()
        }
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：上下に駒台が存在する状態で後手が後手の駒台をタップ
     * 期待値：７回(歩、香、桂、銀、金、角、飛車)、持ち駒を使うメソッドが呼ばれること
     */
    @Test
    fun onTouchWhiteStandByGameModeAndHorizontalStandAndWhite() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn WHITE
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isHorizontalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val y = 0
        for (x in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
        }
        verify(useCase, times(1)).setHintHoldPiece(Piece.FU, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KYO, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KEI, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.GIN, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KIN, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KAKU, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.HISYA, WHITE)
    }

    // endregion

    // region 駒台が左右にある場合

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で盤上をタッチ(1回目)
     * 期待値：
     * ・１回ごと指定の座標でヒントメソッドが呼ばれる
     * ・８１回ヒントメソッドが呼ばれる
     */
    @Test
    fun onTouchBoardByGameModeAndVerticalStand() {
        val cell = Cell()
        val useCase = mock<SyogiLogicUseCase> {
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        for (x in 1..9) {
            for (y in 0..8) {
                presenter.onTouchEventByGameMode(x, y)
                verify(useCase, times(1)).setTouchHint(9 - (x - 1), y + 1)
            }
        }
        verify(useCase, times(81)).setTouchHint(anyInt(), anyInt())
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で盤上をタッチ(2回目)
     * 期待値：
     *・１回ごと指定の座標でコマを動かすメソッドが呼ばれる
     * ・８１回コマを動かすメソッドが呼ばれる
     */
    @Test
    fun onTouchHintByGameModeAndVerticalStand() {
        val cell = Cell()
        cell.hint = true
        val useCase = mock<SyogiLogicUseCase> {
            on { getCellInformation(anyInt(), anyInt()) } doReturn cell
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        for (x in 1..9) {
            for (y in 0..8) {
                presenter.onTouchEventByGameMode(x, y)
                verify(useCase, times(1)).setMove(9 - (x - 1), y + 1, false)
            }
        }
        verify(useCase, times(81)).setMove(anyInt(), anyInt(), any())
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で先手が後手の駒台をタップ
     * 期待値：全てcancelメソッドが呼ばれる
     */
    @Test
    fun onTouchWhiteStandByGameModeAndVerticalStandAndBlack() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn BLACK
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val x = 0
        for (y in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
            verify(useCase, times(y + 1)).cancel()
        }
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で先手が先手の駒台をタップ
     * 期待値：７回(歩、香、桂、銀、金、角、飛車)、持ち駒を使うメソッドが呼ばれること
     */
    @Test
    fun onTouchBlackStandByGameModeAndVerticalStandAndBlack() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn BLACK
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val x = 10
        for (y in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
        }
        verify(useCase, times(1)).setHintHoldPiece(Piece.FU, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KYO, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KEI, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.GIN, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KIN, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KAKU, BLACK)
        verify(useCase, times(1)).setHintHoldPiece(Piece.HISYA, BLACK)
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で後手が先手の駒台をタップ
     * 期待値：全てcancelメソッドが呼ばれる
     */
    @Test
    fun onTouchBlackStandByGameModeAndVerticalStandAndWhite() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn WHITE
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val x = 10
        for (y in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
            verify(useCase, times(y + 1)).cancel()
        }
    }

    /**
     * 対局モードでのタッチイベント
     * 条件：左右に駒台が存在する状態で後手が後手の駒台をタップ
     * 期待値：７回(歩、香、桂、銀、金、角、飛車)、持ち駒を使うメソッドが呼ばれること
     */
    @Test
    fun onTouchWhiteStandByGameModeAndVerticalStandAndWhite() {
        val useCase = mock<SyogiLogicUseCase> {
            on { getTurn() } doReturn WHITE
        }
        val view = mock<GameViewContact.View> {}
        val presenter = GameLogicPresenter(view, useCase)
        val turn: Field = presenter.javaClass.getDeclaredField("isVerticalStand")
        turn.isAccessible = true
        turn.set(presenter, true)
        val x = 0
        for (y in 0..8) {
            presenter.onTouchEventByGameMode(x, y)
        }
        verify(useCase, times(1)).setHintHoldPiece(Piece.FU, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KYO, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KEI, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.GIN, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KIN, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.KAKU, WHITE)
        verify(useCase, times(1)).setHintHoldPiece(Piece.HISYA, WHITE)
    }

    // endregion

    // endregion

    // endregion

}