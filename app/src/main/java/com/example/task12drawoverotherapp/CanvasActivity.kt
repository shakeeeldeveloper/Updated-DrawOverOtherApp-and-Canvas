package com.example.task12drawoverotherapp

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CanvasActivity : AppCompatActivity() {
    private lateinit var drawView: DrawingCanvasView
    private lateinit var cornerSeek: SeekBar
    private lateinit var cornerLabel: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_canvas)


      /*  val drawingView = findViewById<MyCanvasView>(R.id.drawingView)
        val clearBtn = findViewById<Button>(R.id.clearButton)

        clearBtn.setOnClickListener {
            drawingView.clearCanvas()
        }*/



        // for drawing on Click
       /* val drawingView = findViewById<MyCanvasView>(R.id.drawingView)

        findViewById<Button>(R.id.circleBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.CIRCLE)
        }

        findViewById<Button>(R.id.lineBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.LINE)
        }

        findViewById<Button>(R.id.triangleBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.TRIANGLE)
        }

        findViewById<Button>(R.id.pointBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.POINTS)
        }

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            drawingView.clearCanvas()
        }
        findViewById<Button>(R.id.rectBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.RECTANGLE)
        }

        findViewById<Button>(R.id.roundRectBtn).setOnClickListener {
            drawingView.setShapeMode(ShapeMode.ROUNDED_RECTANGLE)
        }*/


        //for preview


        drawView = findViewById(R.id.drawingView)


        // Shape buttons
        findViewById<Button>(R.id.circleBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.CIRCLE)
        }
        findViewById<Button>(R.id.lineBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.LINE)
        }
        findViewById<Button>(R.id.triangleBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.TRIANGLE)
        }
        findViewById<Button>(R.id.pointBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.POINTS)
        }
        findViewById<Button>(R.id.rectBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.RECTANGLE)
        }
        findViewById<Button>(R.id.roundRectBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.ROUNDED_RECTANGLE)
        }
        findViewById<Button>(R.id.ovalBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.OVAL)
        }
        findViewById<Button>(R.id.arcBtn).setOnClickListener {
            drawView.setShapeMode(ShapeMode.ARC)
        }
        findViewById<Button>(R.id.btnStar).setOnClickListener {
            drawView.setShapeMode(ShapeMode.STAR)
        }
         findViewById<Button>(R.id.btnHeart).setOnClickListener {
            drawView.setShapeMode(ShapeMode.HEART)
        }
        findViewById<Button>(R.id.btnWave).setOnClickListener {
            drawView.setShapeMode(ShapeMode.WAVE)
        }
        findViewById<Button>(R.id.btnPolygon).setOnClickListener {
            drawView.setShapeMode(ShapeMode.POLYGON)
        }
        findViewById<Button>(R.id.btnQuadratic).setOnClickListener {
            drawView.setShapeMode(ShapeMode.QUADRATIC_BEZIER)
        }
        findViewById<Button>(R.id.btnCubic).setOnClickListener {
            drawView.setShapeMode(ShapeMode.CUBIC_BEZIER)
        }


        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            drawView.clearCanvas()
        }

        // Corner radius SeekBar
       /* cornerSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawView.setCornerRadius(progress.toFloat())
                cornerLabel.text = "Corner Radius: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })*/
    }
}