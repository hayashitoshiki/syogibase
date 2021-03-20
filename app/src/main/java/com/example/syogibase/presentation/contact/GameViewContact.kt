package com.example.syogibase.presentation.contact

import com.example.syogibase.data.local.GameLog
import com.example.syogibase.util.BoardMode
import com.example.syogibase.util.Handicap

interface GameViewContact {

    interface GameEndListener {
        fun onGameEnd(winner: Int)
    }

    interface View {
        // マスのサイズ設定
        fun setCellSize(size: Float)

        // 将棋盤描画
        fun drawBoard()

        // 垂直方向の駒台描画
        fun drawHorizontalStand()

        // 水平方向の駒台描画
        fun drawVerticalStand()

        // 先手の駒描画
        fun drawBlackPiece(name: String, x: Int, y: Int)

        //後手の駒描画
        fun drawWhitePiece(name: String, x: Int, y: Int)

        // ヒント描画
        fun drawHint(x: Int, y: Int)

        // 先手の持ち駒描画
        fun drawHoldPieceBlack(name: String, stock: Int, x: Int, y: Int)

        // 後手の持ち駒描画
        fun drawHoldPieceWhite(name: String, stock: Int, x: Int, y: Int)

        // 成るか判断するダイアログ生成
        fun showDialog()

        // 対局終了ダイアログ生成
        fun gameEnd(turn: Int)

        // 駒音再生
        fun playbackEffect()

        // 対局モードのタッチイベント処理
        fun onTouchEventByGameMode(x: Int, y: Int)

        // 感想戦モードのタッチイベント処理
        fun onTouchEventByReplayMode(x: Int, y: Int)

        // 戻るの遅延処理セット
        fun setLongJobByBack()

        // 進むの遅延処理セット
        fun setLongJobByGo()

        // キャンセル
        fun cancelLongJob()
    }

    interface Presenter {
        // マスのサイズを計算
        fun computeCellSize(width: Int, height: Int)

        // 感想戦のタッチダウンイベントのロジック
        fun onTouchDownEventByReplayModeLogic(x: Int, center: Int)

        // 感想戦のタッチアップイベントのロジック
        fun onTouchUpEventByReplayModeLogic(x: Int, center: Int)

        fun onTouchEventLogic(x: Int, y: Int, mode: BoardMode)

        fun onTouchEvent(touchX: Int, touchY: Int)

        // 描画ロジック
        fun drawView()

        // 成り
        fun evolutionPiece()

        // １手進む
        fun setGoMove()

        // 最初まで戻る
        fun setBackFirstMove()

        // 最後まで進む
        fun setGoLastMove()

        // １手戻る
        fun setBackMove()

        // リセットする
        fun reset()

        // 駒落ち設定
        fun setHandicap(turn: Int, handicap: Handicap)

        // 駒音有効設定
        fun setEnableTouchSound(enable: Boolean)

        // ヒントの表示設定
        fun setEnableHint(enable: Boolean)

        //棋譜設定
        fun setGameLog(logList: List<GameLog>)

        // 棋譜取得
        fun getGameLog(): List<GameLog>
    }
}