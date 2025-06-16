package com.example.task12drawoverotherapp

import android.graphics.RectF
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class CanvasAdjustActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var drawingView: DrawingOverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_adjust)

        imageView = findViewById(R.id.imageView)
        drawingView = findViewById(R.id.drawingView)

        // Wait until ImageView is laid out
        imageView.post {
            val bounds = getImageBounds(imageView)
            drawingView.setImageBounds(bounds)
        }
    }

    private fun getImageBounds(imageView: ImageView): RectF {
        val drawable = imageView.drawable ?: return RectF()
        val matrix = imageView.imageMatrix
        val bounds = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        matrix.mapRect(bounds)
        return bounds
    }
}

