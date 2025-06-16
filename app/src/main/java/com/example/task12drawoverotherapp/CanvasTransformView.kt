package com.example.task12drawoverotherapp
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasTransformView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL_AND_STROKE
    }

    // Transformation variables
    private var translateX = 0f
    private var translateY = 0f
    private var scale = 1f
    private var rotation = 0f
    private var skewX = 0f
    private var skewY = 0f

    fun translate(dx: Float, dy: Float) {
        translateX += dx
        translateY += dy
        invalidate()
    }

    fun scaleUp(factor: Float) {
        scale *= factor
        invalidate()
    }

    fun rotate(angle: Float) {
        rotation += angle
        invalidate()
    }

    fun skew(sx: Float, sy: Float) {
        skewX += sx
        skewY += sy
        invalidate()
    }

    fun resetTransformations() {
        translateX = 0f
        translateY = 0f
        scale = 1f
        rotation = 0f
        skewX = 0f
        skewY = 0f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        canvas.translate(translateX, translateY)
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.rotate(rotation, width / 2f, height / 2f)
        canvas.skew(skewX, skewY)

        val left = width / 2f - 100
        val top = height / 2f - 100
        val right = width / 2f + 100
        val bottom = height / 2f + 100

        canvas.drawRect(left, top, right, bottom, paint)
       canvas.restore()
        canvas.drawCircle(left, top, 50f, paint2)

    }
}



