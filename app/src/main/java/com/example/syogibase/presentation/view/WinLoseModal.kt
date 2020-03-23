package com.example.syogibase.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

//勝った時の勝敗表示View
class WinLoseModal(context: Context, private val turn: Int,private val w:Float, private val h:Float) : View(context) {

    private var mas: Float = 0.toFloat()//ひとまず分の大きさメモ

    init {
        isFocusable = true
    }

    override fun onDraw(canvas: Canvas) {
        mas = (width / 9).toFloat()
        super.onDraw(canvas)
        val paint = Paint()
        var textWidth: Float?
        canvas.save()
        paint.textSize = mas * 3
        paint.textSkewX = -0.25f

        when (turn) {
            2 -> {
                // 後手が勝った場合
                textWidth = paint.measureText("Win")
                paint.color = Color.rgb(255, 0, 0)
                canvas.rotate(180f, (width / 2).toFloat(), mas * 2)
                canvas.drawText("Win", width / 2 - textWidth / 2, mas * 2, paint)
                canvas.restore()
                textWidth = paint.measureText("Lose")
                paint.color = Color.rgb(0, 0, 255)
                canvas.drawText("Lose", width / 2 - textWidth / 2, mas * 15, paint)
            }
            1 -> {
                // 先手が勝った場合
                textWidth = paint.measureText("Lose")
                paint.color = Color.rgb(0, 0, 255)
                canvas.rotate(180f, (width / 2).toFloat(), mas * 2)
                canvas.drawText("Lose", width / 2 - textWidth / 2, mas * 2, paint)
                canvas.restore()
                textWidth = paint.measureText("Win")
                paint.color = Color.rgb(255, 0, 0)
                canvas.drawText("Win", width / 2 - textWidth / 2, mas * 15, paint)
            }
            else -> {
                // 引き分けの場合
                paint.textSize = mas * 2
                paint.color = Color.rgb(0, 0, 0)
                textWidth = paint.measureText("引き分け")
                canvas.rotate(180f, (width / 2).toFloat(), mas * 2)
                canvas.drawText("引き分け", width / 2 - textWidth / 2, mas * 2, paint)
                canvas.restore()
                canvas.drawText("引き分け", width / 2 - textWidth / 2, mas * 15, paint)
            }
        }
    }

    //指した時の動作
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
}
