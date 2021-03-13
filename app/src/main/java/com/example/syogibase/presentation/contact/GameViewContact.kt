package com.example.syogibase.presentation.contact

import com.example.syogibase.util.Handicap

interface GameViewContact {

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
    }

    interface Presenter {
        // マスのサイズを計算
        fun computeCellSize(width: Int, height: Int)

        // タッチイベントロジック
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
    }
}