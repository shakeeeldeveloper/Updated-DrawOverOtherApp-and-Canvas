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

enum class ShapeMode {
    NONE, CIRCLE, LINE, TRIANGLE, POINTS, RECTANGLE, ROUNDED_RECTANGLE, OVAL,ARC,
    POLYGON, STAR, HEART, WAVE, QUADRATIC_BEZIER, CUBIC_BEZIER
}

class DrawingCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var shapeMode: ShapeMode = ShapeMode.NONE
    private val currentPoints = mutableListOf<PointF>()
    private val shapes = mutableListOf<Pair<(Canvas) -> Unit, Paint>>()
    private var previewPoint: PointF? = null
    private var cornerRadius: Float = 50f
    private var currentPoint = PointF()

    private val bezierPoints = mutableListOf<PointF>()


   /* private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }*/

    val paint = Paint().apply {
       color = Color.RED
       strokeWidth = 30f
       strokeCap = Paint.Cap.ROUND  // Round end on lines
       style = Paint.Style.STROKE
       pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
       /*shader = LinearGradient(
           0f, 0f, 400f, 400f,
           Color.RED, Color.YELLOW, Shader.TileMode.MIRROR
       )*/

       /*shader = SweepGradient(
           400f, 400f,
           Color.CYAN, Color.MAGENTA
       )*/
       /*shader = RadialGradient(
           300f, 300f, 150f,
           Color.GREEN, Color.BLUE, Shader.TileMode.CLAMP
       )*/
       // setShadowLayer(10f, 15f, 15f, Color.YELLOW)
        colorFilter = PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.SRC_OVER)



    }


    fun setShapeMode(mode: ShapeMode) {
        shapeMode = mode
        currentPoints.clear()
        previewPoint = null
        invalidate()
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = PointF(event.x, event.y)
        val paintCopy = Paint(paint)


        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (shapeMode != ShapeMode.TRIANGLE) {
                    currentPoints.clear()
                }
                currentPoints.add(point)
                previewPoint = point
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                previewPoint = point
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (shapeMode == ShapeMode.POINTS) {
                    shapes.add(Pair({
                        it.drawPoint(point.x, point.y, Paint(paint))
                    }, Paint(paint)))
                    currentPoints.clear()
                    previewPoint = null
                }
                else if (shapeMode == ShapeMode.TRIANGLE) {
                    // Wait for 3 points to form triangle
                    if (currentPoints.size == 3) {
                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]
                        val p3 = currentPoints[2]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            val path = Path().apply {
                                moveTo(p1.x, p1.y)
                                lineTo(p2.x, p2.y)
                                lineTo(p3.x, p3.y)
                                close()
                            }
                            it.drawPath(path, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        previewPoint = null
                    }
                }
                else if (shapeMode == ShapeMode.QUADRATIC_BEZIER){
                        bezierPoints.add(PointF(x, y))
                    if (bezierPoints.size == 3) {
                        val (p0, p1, p2) = bezierPoints
                        val path = Path().apply {
                                moveTo(p0.x, p0.y)
                                quadTo(p1.x, p1.y, p2.x, p2.y)
                            }
                        Log.d("Buz",""+path)
                        shapes.add(Pair({ it.drawPath(path, paintCopy) }, paintCopy))
                        //   bezierPoints.clear()
                    }

                }
                else {
                    if (currentPoints.isNotEmpty()) {
                        currentPoints.add(point)
                        val paintCopy = Paint(paint)

                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]

                        when (shapeMode) {
                            ShapeMode.CIRCLE -> {
                                val radius = distance(p1, p2)
                                shapes.add(Pair({
                                    it.drawCircle(p1.x, p1.y, radius, paintCopy)
                                }, paintCopy))
                            }

                            ShapeMode.LINE -> {
                                shapes.add(Pair({
                                    it.drawLine(p1.x, p1.y, p2.x, p2.y, paintCopy)
                                }, paintCopy))
                            }

                            ShapeMode.RECTANGLE -> {
                                shapes.add(Pair({
                                    it.drawRect(p1.x, p1.y, p2.x, p2.y, paintCopy)
                                }, paintCopy))
                            }

                            /*ShapeMode.ROUNDED_RECTANGLE -> {
                                val rect = RectF(
                                    minOf(p1.x, p2.x),
                                    minOf(p1.y, p2.y),
                                    maxOf(p1.x, p2.x),
                                    maxOf(p1.y, p2.y)
                                )
                                shapes.add(Pair({
                                    it.drawRoundRect(rect, cornerRadius, cornerRadius, paintCopy)
                                }, paintCopy))
                            }*/
                            ShapeMode.ROUNDED_RECTANGLE -> {
                                val left = minOf(p1.x, p2.x)
                                val top = minOf(p1.y, p2.y)
                                val right = maxOf(p1.x, p2.x)
                                val bottom = maxOf(p1.y, p2.y)

                                val rectF = RectF(left, top, right, bottom)
                                shapes.add(Pair({
                                    it.drawRoundRect(rectF, cornerRadius, cornerRadius, paintCopy)
                                }, paintCopy))
                            }
                            ShapeMode.OVAL -> {
                                val left = minOf(p1.x, p2.x)
                                val top = minOf(p1.y, p2.y)
                                val right = maxOf(p1.x, p2.x)
                                val bottom = maxOf(p1.y, p2.y)
                                val rectF = RectF(left, top, right, bottom)
                                shapes.add(Pair({
                                    it.drawOval(rectF, paintCopy)
                                }, paintCopy))
                            }

                            ShapeMode.ARC -> {
                                val left = minOf(p1.x, p2.x)
                                val top = minOf(p1.y, p2.y)
                                val right = maxOf(p1.x, p2.x)
                                val bottom = maxOf(p1.y, p2.y)
                                val rectF = RectF(left, top, right, bottom)
                                shapes.add(Pair({
                                    // startAngle = 0, sweep = 180 for semicircle
                                    it.drawArc(rectF, 0f, 90f, true, paintCopy) // sector/pie slice
                                }, paintCopy))
                            }
                            ShapeMode.POLYGON -> {
                                val centerX = (p1.x + p2.x) / 2
                                val centerY = (p1.y + p2.y) / 2
                                val radius = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                                val path = createRegularPolygon(centerX, centerY, radius, sides = 6)
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

                        currentPoints.clear()
                        previewPoint = null
                    }
                }

                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Finalized shapes
        for ((drawFunc, paint) in shapes) {
            drawFunc(canvas)
        }

        // Preview shape
        val previewPaint = Paint(paint).apply {
            color = Color.GRAY
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        }

         if (shapeMode == ShapeMode.QUADRATIC_BEZIER && bezierPoints.size == 2) {
                val tempPath = Path().apply {
                    moveTo(bezierPoints[0].x, bezierPoints[0].y)
                    quadTo(bezierPoints[1].x, bezierPoints[1].y, x, y)
                }
                canvas.drawPath(tempPath, previewPaint)
            }

            if (shapeMode == ShapeMode.CUBIC_BEZIER && bezierPoints.size == 3) {
                val tempPath = Path().apply {
                    moveTo(bezierPoints[0].x, bezierPoints[0].y)
                    cubicTo(bezierPoints[1].x, bezierPoints[1].y,
                        bezierPoints[2].x, bezierPoints[2].y,
                        x, y)
                }
                canvas.drawPath(tempPath, previewPaint)
            }
        if (currentPoints.isNotEmpty() && previewPoint != null) {
            val p1 = currentPoints[0]
            val p2 = previewPoint!!




            when (shapeMode) {
                ShapeMode.CIRCLE -> {
                    val radius = distance(p1, p2)
                    canvas.drawCircle(p1.x, p1.y, radius, previewPaint)
                }

                ShapeMode.LINE -> {
                    canvas.drawLine(p1.x, p1.y, p2.x, p2.y, previewPaint)
                }

                ShapeMode.RECTANGLE -> {
                    canvas.drawRect(p1.x, p1.y, p2.x, p2.y, previewPaint)
                }

                ShapeMode.ROUNDED_RECTANGLE -> {
                    val left = minOf(p1.x, p2.x)
                    val top = minOf(p1.y, p2.y)
                    val right = maxOf(p1.x, p2.x)
                    val bottom = maxOf(p1.y, p2.y)

                    val rectF = RectF(left, top, right, bottom)
                    canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, previewPaint)
                }

                /*ShapeMode.ROUNDED_RECTANGLE -> {
                    val rect = RectF(
                        minOf(p1.x, p2.x),
                        minOf(p1.y, p2.y),
                        maxOf(p1.x, p2.x),
                        maxOf(p1.y, p2.y)
                    )
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, previewPaint)
                }
*/
                ShapeMode.TRIANGLE -> {
                    if (currentPoints.size == 2) {
                        val path = Path().apply {
                            moveTo(currentPoints[0].x, currentPoints[0].y)
                            lineTo(currentPoints[1].x, currentPoints[1].y)
                            lineTo(previewPoint!!.x, previewPoint!!.y)
                            close()
                        }
                        canvas.drawPath(path, previewPaint)
                    }
                }
                ShapeMode.OVAL -> {
                    val left = minOf(p1.x, p2.x)
                    val top = minOf(p1.y, p2.y)
                    val right = maxOf(p1.x, p2.x)
                    val bottom = maxOf(p1.y, p2.y)
                    val rectF = RectF(left, top, right, bottom)
                    canvas.drawOval(rectF, previewPaint)
                }

                ShapeMode.ARC -> {
                    val left = minOf(p1.x, p2.x)
                    val top = minOf(p1.y, p2.y)
                    val right = maxOf(p1.x, p2.x)
                    val bottom = maxOf(p1.y, p2.y)
                    val rectF = RectF(left, top, right, bottom)
                    canvas.drawArc(rectF, 0f, 90f, true, previewPaint)
                }
                ShapeMode.POLYGON -> {
                    val centerX = (p1.x + p2.x) / 2
                    val centerY = (p1.y + p2.y) / 2
                    val radius = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                    val path = createRegularPolygon(centerX, centerY, radius, sides = 6)
                    canvas.drawPath(path, previewPaint)
                }

                ShapeMode.STAR -> {
                    val centerX = (p1.x + p2.x) / 2
                    val centerY = (p1.y + p2.y) / 2
                    val outer = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                    val inner = outer / 2.5f
                    val path = createStar(centerX, centerY, outer, inner)
                    canvas.drawPath(path, previewPaint)
                }

                ShapeMode.HEART -> {
                    val centerX = (p1.x + p2.x) / 2
                    val centerY = (p1.y + p2.y) / 2
                    val size = min(abs(p2.x - p1.x), abs(p2.y - p1.y)) / 2
                    val path = createHeart(centerX, centerY, size)
                    canvas.drawPath(path, previewPaint)
                }

                ShapeMode.WAVE -> {
                    val startX = min(p1.x, p2.x)
                    val startY = (p1.y + p2.y) / 2
                    val length = abs(p2.x - p1.x) / 4
                    val amp = abs(p2.y - p1.y) / 3
                    val path = createWave(startX, startY, length, amp, cycles = 4)
                    canvas.drawPath(path, previewPaint)
                }





                else -> {}
            }
        }
    }
    fun clearCanvas() {
        shapes.clear()
        cornerRadius=0F
        previewPoint=null

        currentPoints.clear()
        invalidate()
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
        path.cubicTo(cx - size, cy - size, cx - size, cy + size / 2, cx, cy + size) // for left side
        path.cubicTo(cx + size, cy + size / 2, cx + size, cy - size, cx, cy)        // for right side
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


    private fun distance(p1: PointF, p2: PointF): Float {
        return Math.hypot((p2.x - p1.x).toDouble(), (p2.y - p1.y).toDouble()).toFloat()
    }
}



