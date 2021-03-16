package com.example.syogibase.presentation.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.syogibase.R
import com.example.syogibase.data.local.Board.Companion.COLS
import com.example.syogibase.presentation.contact.GameViewContact
import com.example.syogibase.util.BoardMode
import com.example.syogibase.util.Handicap
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class GameView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr), GameViewContact.View,
    KoinComponent {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    // region 変数

    private val presenter: GameViewContact.Presenter by inject { parametersOf(this) }
    private lateinit var canvas: Canvas
    private lateinit var event: MotionEvent

    private var job: Job? = null
    private var mode: BoardMode = BoardMode.GAME

    private var cellWidth: Float = 0f
    private var cellHeight: Float = 0f
    private val boardWidth: Float
        get() = cellWidth * COLS
    private val boardHeight: Float
        get() = cellHeight * COLS
    private val horizontalStandSpace: Float
        get() = if (width < height) {
            0f
        } else {
            cellWidth
        }
    private val verticalStandSpace: Float
        get() = if (width < height) {
            cellHeight
        } else {
            0f
        }

    // endregion

    // region ライフサイクル

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        presenter.computeCellSize(width, height)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        val boardStartX = (width - (boardWidth + horizontalStandSpace * 2)) / 2
        val boardStartY = (height - (boardHeight + verticalStandSpace * 2)) / 2
        this.canvas.translate(boardStartX, boardStartY)
        presenter.drawView()
    }

    // endregion

    // region タッチイベント

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.event = event
        val boardStartX = (width - (boardWidth + horizontalStandSpace * 2)) / 2
        val boardStartY = (height - (boardHeight + verticalStandSpace * 2)) / 2
        val x = ((event.x - boardStartX) / cellWidth).toInt()
        val y = ((event.y - boardStartY) / cellHeight).toInt()
        presenter.onTouchEventLogic(x, y, mode)
        return true
    }

    // 対局モードのタッチイベント処理
    override fun onTouchEventByGameMode(x: Int, y: Int) {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                presenter.onTouchEvent(x, y)
                invalidate()
            }
        }
    }

    // 感想戦モードのタッチイベント処理
    override fun onTouchEventByReplayMode(x: Int, y: Int) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val centerCell = (width / cellWidth / 2).toInt()
                presenter.onTouchDownEventByReplayModeLogic(x, centerCell)
            }
            MotionEvent.ACTION_UP -> {
                val centerCell = (width / cellWidth / 2).toInt()
                presenter.onTouchUpEventByReplayModeLogic(x, centerCell)
                invalidate()
            }
        }
    }

    // 最初まで戻る処理の遅延処理セット
    override fun setLongJobByBack() {
        job = GlobalScope.launch {
            delay(800)
            if (!isActive) {
                return@launch
            }
            handler.post {
                presenter.setBackFirstMove()
            }
        }
    }

    // 最後まで進む処理の遅延処理セット
    override fun setLongJobByGo() {
        job = GlobalScope.launch {
            delay(800)
            if (!isActive) {
                return@launch
            }
            handler.post {
                presenter.setGoLastMove()
            }
        }
    }

    // 遅延処理キャンセル
    override fun cancelLongJob() {
        job?.cancel()
    }

    // endregion

    // region 描画

    // 将棋盤描画
    override fun drawBoard() {
        // 盤面セット
        val paint = Paint()
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.free_grain_sub)
        val rect1 = Rect(0, 0, bmp.width, bmp.height)
        val boardStartX = horizontalStandSpace.toInt()
        val boardStartY = verticalStandSpace.toInt()
        val boardEndX = boardWidth.toInt() + horizontalStandSpace.toInt()
        val boardEndY = boardHeight.toInt() + verticalStandSpace.toInt()
        val rect2 = Rect(boardStartX, boardStartY, boardEndX, boardEndY)
        canvas.drawBitmap(bmp, rect1, rect2, paint)

        // 罫線セット
        paint.color = Color.rgb(40, 40, 40)
        paint.strokeWidth = 2f
        for (i in 0 until 10) {
            val linePositionX = cellWidth * i + horizontalStandSpace
            val lineStartY = verticalStandSpace
            val lineEndY = boardHeight + verticalStandSpace
            canvas.drawLine(linePositionX, lineStartY, linePositionX, lineEndY, paint)
        }
        for (i in 0 until 10) {
            val linePositionY = cellHeight * i + verticalStandSpace
            val lineStartX = horizontalStandSpace
            val lineEndX = boardWidth + horizontalStandSpace
            canvas.drawLine(lineStartX, linePositionY, lineEndX, linePositionY, paint)
        }
    }

    // 盤面の上下に駒台描画
    override fun drawHorizontalStand() {
        val paint = Paint()
        paint.color = Color.rgb(251, 171, 83)
        val whiteStandStartX = 0f
        val whiteStandStartY = 0f
        val whiteStandEndX = boardWidth - cellWidth * 2
        val whiteStandEndY = verticalStandSpace
        canvas.drawRect(whiteStandStartX, whiteStandStartY, whiteStandEndX, whiteStandEndY, paint)
        val blackStandStartX = cellWidth * 2
        val blackStandStartY = boardHeight + verticalStandSpace
        val blackStandEndX = boardWidth
        val blackStandEndY = boardHeight + verticalStandSpace * 2
        canvas.drawRect(blackStandStartX, blackStandStartY, blackStandEndX, blackStandEndY, paint)
    }

    // 盤面の左右に駒台描画
    override fun drawVerticalStand() {
        val paint = Paint()
        paint.color = Color.rgb(251, 171, 83)
        val whiteStandStartX = 0f
        val whiteStandStartY = 0f
        val whiteStandEndX = horizontalStandSpace
        val whiteStandEndY = boardHeight - cellHeight * 2
        canvas.drawRect(whiteStandStartX, whiteStandStartY, whiteStandEndX, whiteStandEndY, paint)
        val blackStandStartX = boardHeight + horizontalStandSpace
        val blackStandStartY = cellHeight * 2
        val blackStandEndX = boardWidth + horizontalStandSpace * 2
        val blackStandEndY = boardHeight
        canvas.drawRect(blackStandStartX, blackStandStartY, blackStandEndX, blackStandEndY, paint)
    }

    // 先手の駒描画
    override fun drawBlackPiece(name: String, x: Int, y: Int) {
        val newX = x + (horizontalStandSpace / cellWidth).toInt()
        val newY = y + (verticalStandSpace / cellHeight).toInt()
        drawPiece(name, newX, newY)
    }

    // 後手の駒描画
    override fun drawWhitePiece(name: String, x: Int, y: Int) {
        val cellCenterX = horizontalStandSpace + (cellWidth * x) + cellWidth / 2
        val cellCenterY = verticalStandSpace + (cellHeight * y) + cellWidth / 2
        canvas.save()
        canvas.rotate(180f, cellCenterX, cellCenterY)
        val newX = x + (horizontalStandSpace / cellWidth).toInt()
        val newY = y + (verticalStandSpace / cellHeight).toInt()
        drawPiece(name, newX, newY)
        canvas.restore()
    }

    // 先手の持ち駒描画
    override fun drawHoldPieceBlack(name: String, stock: Int, x: Int, y: Int) {
        drawPiece(name, x, y)
        drawCount(stock.toString(), x, y)
    }

    // 後手の持ち駒描画
    override fun drawHoldPieceWhite(name: String, stock: Int, x: Int, y: Int) {
        val cellCenterX = (cellWidth * x) + cellWidth / 2
        val cellCenterY = (cellHeight * y) + cellHeight / 2
        canvas.save()
        canvas.rotate(180f, cellCenterX, cellCenterY)
        drawPiece(name, x, y)
        drawCount(stock.toString(), x, y)
        canvas.restore()
    }

    // コマ描画
    private fun drawPiece(name: String, x: Int, y: Int) {
        val paint = Paint()
        paint.textSize = cellWidth / 2
        paint.color = Color.rgb(40, 40, 40)
        val textWidth = paint.measureText(name)
        val textCenter = textWidth / 2
        val cellCenterX = cellWidth * x + cellWidth / 2 - textCenter
        val cellCenterY = cellHeight * y + cellHeight / 2 - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText(name, cellCenterX, cellCenterY, paint)
    }

    // コマのカウント描画
    private fun drawCount(count: String, x: Int, y: Int) {
        val paint = Paint()
        paint.textSize = cellWidth / 5
        paint.color = Color.rgb(40, 40, 40)
        val textWidth = paint.measureText(count)
        val textCenter = textWidth / 2
        val cellCenterX = cellWidth * x + cellWidth / 4 * 3 + textCenter
        val cellCenterY =
            cellHeight * y + cellHeight / 4 * 3 - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText(count, cellCenterX, cellCenterY, paint)
    }

    // ヒント描画メソッド
    override fun drawHint(x: Int, y: Int) {
        val paint = Paint()
        paint.color = (Color.argb(200, 255, 255, 0))
        val centerX = cellWidth * (x + (horizontalStandSpace / cellWidth).toInt()) + cellWidth / 2
        val centerY = cellHeight * (y + (verticalStandSpace / cellHeight).toInt()) + cellHeight / 2
        val radius = (cellWidth * 0.46).toFloat()
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    // endregion

    // region ダイアログ

    // 成り判定ダイアログ
    override fun showDialog() {
        val alertBuilder = AlertDialog.Builder(context).setCancelable(false)
        alertBuilder.setMessage("成りますか？")
        alertBuilder.setPositiveButton("はい") { _, _ ->
            presenter.evolutionPiece()
            invalidate()
        }
        alertBuilder.setNegativeButton("いいえ") { _, _ ->
            invalidate()
        }
        alertBuilder.create().show()
    }

    // 終了ダイアログ表示
    override fun gameEnd(turn: Int) {
        AlertDialog.Builder(context)
            .setTitle("終了")
            .setMessage("勝ち")
            .setPositiveButton("終了") { dialog, which ->
                //activity.finish()
                setBoardMove(BoardMode.REPLAY)
            }
            .setNeutralButton("もう一度") { dialog, which ->
                presenter.reset()
            }
            .setNegativeButton("感想戦") { dialog, which ->
                setBoardMove(BoardMode.REPLAY)
            }
            .show()
    }

    // region 設定

    // マスのサイズ設定
    override fun setCellSize(size: Float) {
        cellWidth = size
        cellHeight = size
    }

    // Viewのモード設定
    fun setBoardMove(mode: BoardMode) {
        this.mode = mode
    }

    // ハンデ設定
    fun setHandicap(turn: Int, handicap: Handicap) {
        presenter.setHandicap(turn, handicap)
    }

    // endregion

}