package com.example.syogibase.presentation.contact

interface GameViewContact {

    interface View {
        // 将棋盤描画
        fun drawBoard()

        // 先手の駒描画
        fun drawBlackPiece(name: String, i: Int, j: Int)

        //後手の駒描画
        fun drawWhitePiece(name: String, i: Int, j: Int)

        // ヒント描画
        fun drawHint(x: Int, y: Int)

        // 先手の持ち駒描画
        fun drawHoldPieceBlack(nameJP: String, stock: Int, count: Int)

        // 後手の持ち駒描画
        fun drawHoldPieceWhite(nameJP: String, stock: Int, count: Int)

        // 成るか判断するダイアログ生成
        fun showDialog()

        // 対局終了ダイアログ生成
        fun gameEnd(turn: Int)
    }

    interface Presenter {
        // タッチイベントロジック
        fun onTouchEvent(x: Int, y: Int)

        // 描画ロジック
        fun drawView()

        // 成り
        fun evolutionPiece()
    }
}