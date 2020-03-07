package com.example.syogibase.view

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
        mas = (w / 9)
        super.onDraw(canvas)
        val paint = Paint()
        paint.textSize = mas * 3


        if (turn == 2) {
            canvas.save()

            var text_Width = paint.measureText("Win")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(255, 0, 0)
            canvas.rotate(180f, (w / 2), mas * 4)
            canvas.drawText("Win", w / 2 - text_Width / 2, h / 2 - mas * 4, paint)
            canvas.restore()

            text_Width = paint.measureText("Lose")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(0, 0, 255)
            canvas.drawText("Lose", w / 2 - text_Width / 2, h / 2 + mas * 4, paint)
        } else {
            canvas.save()

            var text_Width = paint.measureText("Lose")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(0, 0, 255)
            canvas.rotate(180f, (w / 2), h / 2 - mas * 4)
            canvas.drawText("Lose", w / 2 - text_Width / 2, h / 2 - mas * 4, paint)
            canvas.restore()

            text_Width = paint.measureText("Win")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(255, 0, 0)
            canvas.drawText("Win", w / 2 - text_Width / 2, h / 2 + mas * 4 , paint)
        }
    }

    //指した時の動作
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
}
