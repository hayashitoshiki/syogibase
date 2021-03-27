package com.example.syogibase.presentation.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.syogibase.R
import com.example.syogibase.data.entity.Board.Companion.COLS
import com.example.syogibase.data.entity.GameLog
import com.example.syogibase.data.value.BoardMode
import com.example.syogibase.data.value.Handicap
import com.example.syogibase.data.value.Turn
import com.example.syogibase.presentation.contact.GameViewContact
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

    private var bmp = BitmapFactory.decodeResource(resources, R.drawable.syogi_board)
    private var holdStand = BitmapFactory.decodeResource(resources, R.drawable.syogi_stand)
    private lateinit var soundPool: SoundPool
    private var soundOne = 0
    private var hintColor = Color.argb(200, 255, 255, 0)
    private var listener: GameViewContact.GameEndListener? = null

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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 音声設定
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        soundOne = soundPool.load(context, R.raw.sound_japanese_chess, 1)
    }

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
                presenter.onTouchEventByGameMode(x, y)
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
        val rect1 = Rect(0, 0, holdStand.width, holdStand.height)
        val whiteStandStartX = 0
        val whiteStandStartY = 0
        val whiteStandEndX = (boardWidth - cellWidth * 2).toInt()
        val whiteStandEndY = verticalStandSpace.toInt()
        val rect2 = Rect(whiteStandStartX, whiteStandStartY, whiteStandEndX, whiteStandEndY)
        canvas.drawBitmap(holdStand, rect1, rect2, paint)
        val blackStandStartX = (cellWidth * 2).toInt()
        val blackStandStartY = (boardHeight + verticalStandSpace).toInt()
        val blackStandEndX = boardWidth.toInt()
        val blackStandEndY = (boardHeight + verticalStandSpace * 2).toInt()
        val rect3 = Rect(blackStandStartX, blackStandStartY, blackStandEndX, blackStandEndY)
        canvas.drawBitmap(holdStand, rect1, rect3, paint)
    }

    // 盤面の左右に駒台描画
    override fun drawVerticalStand() {
        val paint = Paint()
        val rect1 = Rect(0, 0, holdStand.width, holdStand.height)
        val whiteStandStartX = 0
        val whiteStandStartY = 0
        val whiteStandEndX = horizontalStandSpace.toInt()
        val whiteStandEndY = (boardHeight - cellHeight * 2).toInt()
        val rect2 = Rect(whiteStandStartX, whiteStandStartY, whiteStandEndX, whiteStandEndY)
        canvas.drawBitmap(holdStand, rect1, rect2, paint)
        val blackStandStartX = (boardHeight + horizontalStandSpace).toInt()
        val blackStandStartY = (cellHeight * 2).toInt()
        val blackStandEndX = (boardWidth + horizontalStandSpace * 2).toInt()
        val blackStandEndY = boardHeight.toInt()
        val rect3 = Rect(blackStandStartX, blackStandStartY, blackStandEndX, blackStandEndY)
        canvas.drawBitmap(holdStand, rect1, rect3, paint)
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
        paint.color = hintColor
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
            presenter.checkGameEnd()
        }
        alertBuilder.setNegativeButton("いいえ") { _, _ ->
            invalidate()
            presenter.checkGameEnd()
        }
        alertBuilder.create().show()
    }

    // 終了ダイアログ表示
    override fun gameEnd(turn: Turn?) {
        setBoardMove(BoardMode.REPLAY)
        listener?.onGameEnd(turn)
    }
    // endregion

    // region 設定

    // マスのサイズ設定
    override fun setCellSize(size: Float) {
        cellWidth = size
        cellHeight = size
    }

    // endregion

    // region その他

    // 駒音再生
    override fun playbackEffect() {
        // play(ロードしたID, 左音量, 右音量, 優先度, ループ, 再生速度)
        soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1.0f)
    }

    // endregion

    // region 外部設定用メソッド

    // Viewのモード設定
    fun setBoardMove(mode: BoardMode) {
        this.mode = mode
    }

    // リセット
    fun reset() {
        presenter.reset()
    }

    // 対局終了を知らせるリスナー設定
    fun setGameEndListener(listener: GameViewContact.GameEndListener) {
        this.listener = listener
    }

    // ヒントの表示設定
    fun enableHint(enable: Boolean) {
        presenter.setEnableHint(enable)
    }

    // 駒音有効設定
    fun enableTouchSound(enable: Boolean) {
        presenter.setEnableTouchSound(enable)
    }

    // ハンデ設定
    fun setHandicap(turn: Turn, handicap: Handicap) {
        presenter.setHandicap(turn, handicap)
    }

    // 盤面画像設定
    fun setBoardImage(resourceId: Int) {
        bmp = BitmapFactory.decodeResource(resources, resourceId)
    }

    // 持ち駒台画像設定
    fun setHoldStandImage(resourceId: Int) {
        bmp = BitmapFactory.decodeResource(resources, resourceId)
    }

    // ヒント色設定
    fun setHintColor(color: Int) {
        hintColor = color
    }

    // 駒音設定
    fun setMoveSound(resourceId: Int) {
        soundOne = soundPool.load(context, resourceId, 1)
    }

    // 棋譜設定
    fun setLog(logList: List<GameLog>) {
        presenter.setGameLog(logList)
    }

    // 棋譜取得
    fun getLog() {
        presenter.getGameLog()
    }

    // endregion

}