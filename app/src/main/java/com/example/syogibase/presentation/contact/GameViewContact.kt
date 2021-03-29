package com.example.syogibase.presentation.contact

import com.example.syogibase.domain.model.GameLog
import com.example.syogibase.domain.value.BoardMode
import com.example.syogibase.domain.value.Handicap
import com.example.syogibase.domain.value.Turn

/**
 * GameViewのContact定義
 */
interface GameViewContact {

    interface GameEndListener {

        /**
         * 対局終了通知リスナ
         * @param winner 勝った手番
         */
        fun onGameEnd(winner: Turn?)
    }

    interface View {

        /**
         * マスのサイズ設定
         */
        fun setCellSize(size: Float)

        /**
         * 将棋盤描画
         */
        fun drawBoard()

        /**
         * 垂直方向の駒台描画
         */
        fun drawHorizontalStand()

        /**
         * 水平方向の駒台描画
         */
        fun drawVerticalStand()

        /**
         * 先手の駒描画
         * @param name 駒の名前
         * @param x 描画するマスのX座標
         * @param y 描画するマスのY座標
         */
        fun drawBlackPiece(name: String, x: Int, y: Int)

        /**
         * 後手の駒描画
         * @param name 駒の名前
         * @param x 描画するマスのX座標
         * @param y 描画するマスのY座標
         */
        fun drawWhitePiece(name: String, x: Int, y: Int)

        /**
         * 駒を動かせるマスのヒントを描画
         * @param x 描画するマスのX座標
         * @param y 描画するマスのY座標
         */
        fun drawHint(x: Int, y: Int)

        /**
         * 先手の持ち駒を描画
         * @param name 駒の名前
         * @param stock 所持数
         * @param x 描画するX座標
         * @param y 描画するY座標
         */
        fun drawHoldPieceBlack(name: String, stock: Int, x: Int, y: Int)

        /**
         * 後手の持ち駒を描画
         * @param name 駒の名前
         * @param stock 所持数
         * @param x 描画するX座標
         * @param y 描画するY座標
         */
        fun drawHoldPieceWhite(name: String, stock: Int, x: Int, y: Int)

        /**
         * 成るか判別するダイアログ生成
         */
        fun showDialog()

        /**
         * 対局終了通知
         * @param turn 勝った手番(引き分けの場合はnullを渡す)
         */
        fun gameEnd(turn: Turn?)

        /**
         * 駒音再生
         */
        fun playbackEffect()

        /**
         * 対局モード時のタッチイベント処理
         * @param x タップしたx座標
         * @param y タップしたy座標
         */
        fun onTouchEventByGameMode(x: Int, y: Int)

        /**
         * 感想戦モード時のタッチイベント処理
         * @param x タップしたx座標
         * @param y タップしたy座標
         */
        fun onTouchEventByReplayMode(x: Int, y: Int)

        /**
         * 感想戦モードの一番最初まで戻る処理の遅延処理設定
         */
        fun setLongJobByBack()

        /**
         * 感想戦モードの一番最後まで進む処理の遅延処理設定
         */
        fun setLongJobByGo()

        /**
         * 感想戦モードの遅延処理のキャンセル
         */
        fun cancelLongJob()
    }

    interface Presenter {

        /**
         * マスのサイズを計算
         * @param width 幅
         * @param height 高さ
         */
        fun computeCellSize(width: Int, height: Int)

        /**
         * 画面をタッチしたときのモード判定ロジック
         * @param x タッチしたX軸
         * @param y Y軸
         * @param mode 現在の将棋盤のモード
         */
        fun onTouchEventLogic(x: Int, y: Int, mode: BoardMode)

        /**
         * 対局モードのタッチイベントのロジック
         * @param touchX ViewをタッチしたときのX軸
         * @param touchY ViewをタッチしたときのY軸
         */
        fun onTouchEventByGameMode(touchX: Int, touchY: Int)

        /**
         * 感想戦のタッチダウンイベントのロジック
         * @param x タッチダウンしたときのX座標
         * @param center X軸の中心点
         */
        fun onTouchDownEventByReplayModeLogic(x: Int, center: Int)

        /**
         * 感想戦のタッチアップイベントのロジック
         * @param x タッチアップしたときのX座標
         * @param center X軸の中心点
         */
        fun onTouchUpEventByReplayModeLogic(x: Int, center: Int)

        /**
         * 対局終了判定
         */
        fun checkGameEnd()

        /**
         * 描画ロジック
         */
        fun drawView()

        /**
         * 駒を成らせる
         */
        fun evolutionPiece()

        /**
         * １手進む
         */
        fun setGoMove()

        /**
         * 最初まで戻る
         */
        fun setBackFirstMove()

        /**
         * 最後まで進む
         */
        fun setGoLastMove()

        /**
         * １手戻る
         */
        fun setBackMove()

        /**
         * 初期状態に戻す
         */
        fun reset()

        /**
         * 駒落ち設定
         * @param turn 設定する手番
         * @param handicap 設定するハンデ
         */
        fun setHandicap(turn: Turn, handicap: Handicap)

        /**
         * 駒音を有無の設定
         * @param enable 駒音の有無
         */
        fun setEnableTouchSound(enable: Boolean)

        /**
         * 駒を動かせる場所の表示設定
         * @param enable 駒を動かせる場所の表示の有無
         */
        fun setEnableHint(enable: Boolean)

        /**
         * 棋譜設定
         * @param logList 設定する棋譜
         */
        fun setGameLog(logList: List<GameLog>)

        /**
         * 棋譜を取得
         * @return 現時点の棋譜
         */
        fun getGameLog(): List<GameLog>
    }
}