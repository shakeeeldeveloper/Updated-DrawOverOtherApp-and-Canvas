package com.example.task12drawoverotherapp

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

class FilterImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.devsky_logo)

    fun setFilter(type: String) {
        when (type) {
            "porterduff" -> {
                paint.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            }
            "lighting" -> {
                paint.colorFilter = LightingColorFilter(0xFFFF00, 0x000000) // Yellow tone
            }
            "blendmode" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    paint.colorFilter = BlendModeColorFilter(Color.GREEN, BlendMode.COLOR)
                } else {
                    Toast.makeText(context, "BlendMode requires API 29+", Toast.LENGTH_SHORT).show()
                }
            }
            else -> paint.colorFilter = null
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}


