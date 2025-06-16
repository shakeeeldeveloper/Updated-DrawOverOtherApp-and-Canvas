package com.example.task12drawoverotherapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DrawingOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private var imageBounds: RectF? = null

    fun setImageBounds(bounds: RectF) {
        imageBounds = bounds
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        imageBounds?.let { bounds ->
            val cx = bounds.right - 40f
            val cy = bounds.top + 40f
            canvas.drawCircle(cx, cy, 30f, paint)
        }
    }
}
