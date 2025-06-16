package com.example.task12drawoverotherapp
import android.graphics.*


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin



import kotlin.math.*

/*enum class ShapeMode {
    NONE, CIRCLE, LINE, TRIANGLE, POINTS, RECTANGLE, ROUNDED_RECTANGLE, OVAL, ARC,
    POLYGON, STAR, HEART, WAVE, QUADRATIC_BEZIER, CUBIC_BEZIER
}*/

class DrawingCanvasView1(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val previewPaint = Paint(paint).apply {
        color = Color.GRAY
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private var shapeMode = ShapeMode.NONE
    private var startPoint = PointF()
    private var currentPoint = PointF()
    private var bezierPoints = mutableListOf<PointF>()

    private val shapes = mutableListOf<Pair<(Canvas) -> Unit, Paint>>()

    fun setShapeMode(mode: ShapeMode) {
        shapeMode = mode
        bezierPoints.clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startPoint = PointF(x, y)
                currentPoint = PointF(x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                currentPoint = PointF(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                val endPoint = PointF(x, y)
                val p1 = startPoint
                val p2 = endPoint
                val paintCopy = Paint(paint)

                when (shapeMode) {
                    ShapeMode.CIRCLE -> {
                        val radius = hypot(p2.x - p1.x, p2.y - p1.y) / 2
                        val cx = (p1.x + p2.x) / 2
                        val cy = (p1.y + p2.y) / 2
                        shapes.add(Pair({ it.drawCircle(cx, cy, radius, paintCopy) }, paintCopy))
                    }

                    ShapeMode.RECTANGLE -> {
                        shapes.add(Pair({ it.drawRect(p1.x, p1.y, p2.x, p2.y, paintCopy) }, paintCopy))
                    }

                    ShapeMode.ROUNDED_RECTANGLE -> {
                        shapes.add(Pair({
                            it.drawRoundRect(RectF(p1.x, p1.y, p2.x, p2.y), 40f, 40f, paintCopy)
                        }, paintCopy))
                    }

                    ShapeMode.LINE -> {
                        shapes.add(Pair({ it.drawLine(p1.x, p1.y, p2.x, p2.y, paintCopy) }, paintCopy))
                    }

                    ShapeMode.TRIANGLE -> {
                        val path = Path().apply {
                            moveTo(p1.x, p2.y)
                            lineTo((p1.x + p2.x) / 2, p1.y)
                            lineTo(p2.x, p2.y)
                            close()
                        }
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                    }

                    ShapeMode.POINTS -> {
                        val points = floatArrayOf(p1.x, p1.y, p2.x, p2.y)
                        shapes.add(Pair({ it.drawPoints(points, paintCopy) }, paintCopy))
                    }

                    ShapeMode.OVAL -> {
                        shapes.add(Pair({ it.drawOval(RectF(p1.x, p1.y, p2.x, p2.y), paintCopy) }, paintCopy))
                    }

                    ShapeMode.ARC -> {
                        val rect = RectF(p1.x, p1.y, p2.x, p2.y)
                        shapes.add(Pair({ it.drawArc(rect, 0f, 180f, true, paintCopy) }, paintCopy))
                    }

                    ShapeMode.POLYGON -> {
                        val cx = (p1.x + p2.x) / 2
                        val cy = (p1.y + p2.y) / 2
                        val radius = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                        val path = createRegularPolygon(cx, cy, radius, 6)
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                    }

                    ShapeMode.STAR -> {
                        val cx = (p1.x + p2.x) / 2
                        val cy = (p1.y + p2.y) / 2
                        val outer = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                        val inner = outer / 2.5f
                        val path = createStar(cx, cy, outer, inner)
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                    }

                    ShapeMode.HEART -> {
                        val cx = (p1.x + p2.x) / 2
                        val cy = (p1.y + p2.y) / 2
                        val size = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                        val path = createHeart(cx, cy, size)
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                    }

                    ShapeMode.WAVE -> {
                        val startX = min(p1.x, p2.x)
                        val startY = (p1.y + p2.y) / 2
                        val length = abs(p2.x - p1.x) / 4
                        val amp = abs(p2.y - p1.y) / 3
                        val path = createWave(startX, startY, length, amp, 4)
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                    }

                    ShapeMode.QUADRATIC_BEZIER -> {
                        bezierPoints.add(PointF(x, y))
                        if (bezierPoints.size == 3) {
                            val (p0, p1, p2) = bezierPoints
                            val path = Path().apply {
                                moveTo(p0.x, p0.y)
                                quadTo(p1.x, p1.y, p2.x, p2.y)
                            }
                            Log.d("Buz",""+bezierPoints)

                            shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                            bezierPoints.clear()
                        }
                    }

                    ShapeMode.CUBIC_BEZIER -> {
                        bezierPoints.add(PointF(x, y))
                        if (bezierPoints.size == 4) {
                            val (p0, p1, p2, p3) = bezierPoints
                            val path = Path().apply {
                                moveTo(p0.x, p0.y)
                                cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
                            }
                            shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                            bezierPoints.clear()
                        }
                    }

                    else -> {}
                }

                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw finalized shapes
        for ((shapeDraw, paint) in shapes) {
            shapeDraw(canvas)
        }

        // Preview shape
        val p1 = startPoint
        val p2 = currentPoint

        val previewPath = when (shapeMode) {
            ShapeMode.TRIANGLE -> Path().apply {
                moveTo(p1.x, p2.y)
                lineTo((p1.x + p2.x) / 2, p1.y)
                lineTo(p2.x, p2.y)
                close()
            }

            ShapeMode.POLYGON -> createRegularPolygon((p1.x + p2.x) / 2, (p1.y + p2.y) / 2,
                min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2, 6)

            ShapeMode.STAR -> createStar((p1.x + p2.x) / 2, (p1.y + p2.y) / 2,
                min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2,
                min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 5f)

            ShapeMode.HEART -> createHeart((p1.x + p2.x) / 2, (p1.y + p2.y) / 2,
                min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2)

            ShapeMode.WAVE -> createWave(min(p1.x, p2.x), (p1.y + p2.y) / 2,
                abs(p2.x - p1.x) / 4, abs(p2.y - p1.y) / 3, 4)

            else -> null
        }

        if (previewPath != null) {
            canvas.drawPath(previewPath, previewPaint)
        }

        if (shapeMode == ShapeMode.QUADRATIC_BEZIER && bezierPoints.size == 2) {
            val tempPath = Path().apply {
                moveTo(bezierPoints[0].x, bezierPoints[0].y)
                quadTo(bezierPoints[1].x, bezierPoints[1].y, currentPoint.x, currentPoint.y)
            }
            canvas.drawPath(tempPath, previewPaint)
        }

        if (shapeMode == ShapeMode.CUBIC_BEZIER && bezierPoints.size == 3) {
            val tempPath = Path().apply {
                moveTo(bezierPoints[0].x, bezierPoints[0].y)
                cubicTo(
                    bezierPoints[1].x, bezierPoints[1].y,
                    bezierPoints[2].x, bezierPoints[2].y,
                    currentPoint.x, currentPoint.y
                )
            }
            canvas.drawPath(tempPath, previewPaint)
        }
    }

    private fun createRegularPolygon(cx: Float, cy: Float, r: Float, sides: Int): Path {
        val path = Path()
        val angle = (2.0 * Math.PI / sides)
        path.moveTo((cx + r * cos(0.0)).toFloat(), (cy + r * sin(0.0)).toFloat())
        for (i in 1 until sides) {
            path.lineTo((cx + r * cos(angle * i)).toFloat(), (cy + r * sin(angle * i)).toFloat())
        }
        path.close()
        return path
    }

    private fun createStar(cx: Float, cy: Float, outer: Float, inner: Float): Path {
        val path = Path()
        val angle = Math.PI / 5
        for (i in 0..9) {
            val r = if (i % 2 == 0) outer else inner
            val x = (cx + cos(i * angle) * r).toFloat()
            val y = (cy + sin(i * angle) * r).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return path
    }

    private fun createHeart(cx: Float, cy: Float, size: Float): Path {
        val path = Path()
        path.moveTo(cx, cy)
        path.cubicTo(cx - size, cy - size, cx - size, cy + size / 2, cx, cy + size)
        path.cubicTo(cx + size, cy + size / 2, cx + size, cy - size, cx, cy)
        return path
    }

    private fun createWave(startX: Float, startY: Float, waveLength: Float, amplitude: Float, cycles: Int): Path {
        val path = Path()
        path.moveTo(startX, startY)
        for (i in 0 until cycles) {
            val controlX = startX + waveLength / 2 + i * waveLength
            val controlY = startY + if (i % 2 == 0) amplitude else -amplitude
            val endX = startX + (i + 1) * waveLength
            path.quadTo(controlX, controlY, endX, startY)
        }
        return path
    }
    fun clearCanvas() {
        shapes.clear()

        invalidate()
    }
}



