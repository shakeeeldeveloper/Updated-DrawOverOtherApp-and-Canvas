package com.example.task12drawoverotherapp

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CanvasTransfomrationActivity : AppCompatActivity() {

    private lateinit var canvasView: CanvasTransformView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_transfomration)

        canvasView = findViewById(R.id.canvasView)

        findViewById<Button>(R.id.btnTranslateRight).setOnClickListener {
            canvasView.translate(50f, 0f)
        }

        findViewById<Button>(R.id.btnTranslateDown).setOnClickListener {
            canvasView.translate(0f, 50f)
        }

        findViewById<Button>(R.id.btnScaleUp).setOnClickListener {
            canvasView.scaleUp(1.1f)
        }

        findViewById<Button>(R.id.btnRotate).setOnClickListener {
            canvasView.rotate(15f)
        }

        findViewById<Button>(R.id.btnSkewX).setOnClickListener {
            canvasView.skew(0.1f, 0f)
        }

        findViewById<Button>(R.id.btnSkewY).setOnClickListener {
            canvasView.skew(0f, 0.1f)
        }

        findViewById<Button>(R.id.btnReset).setOnClickListener {
            canvasView.resetTransformations()
        }
    }
}
