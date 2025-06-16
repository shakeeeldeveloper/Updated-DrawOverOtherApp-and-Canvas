package com.example.task12drawoverotherapp
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

//for signature
/*class MyCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val paths = mutableListOf<Pair<Path, Paint>>()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw all saved paths
        for ((p, paintObj) in paths) {
            canvas.drawPath(p, paintObj)
        }

        // Draw current active path
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // Save the completed path and start a new one
                paths.add(Pair(Path(path), Paint(paint)))
                path.reset()
                invalidate()
            }
        }
        return true
    }

    fun clearCanvas() {
        paths.clear()
        path.reset()
        invalidate()
    }
}*/


//for drawing click shape
/*
class MyCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var shapeMode = ShapeMode.NONE
    private val currentPoints = mutableListOf<PointF>()

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    private val shapes = mutableListOf<Pair<() -> Unit, Paint>>()

    fun setShapeMode(mode: ShapeMode) {
        shapeMode = mode
        currentPoints.clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val point = PointF(event.x, event.y)
            currentPoints.add(point)

            when (shapeMode) {
                ShapeMode.CIRCLE -> {
                    if (currentPoints.size == 2) {
                        val center = currentPoints[0]
                        val edge = currentPoints[1]
                        val radius = distance(center, edge)
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            canvas?.drawCircle(center.x, center.y, radius, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
                }

                ShapeMode.LINE -> {
                    if (currentPoints.size == 2) {
                        val start = currentPoints[0]
                        val end = currentPoints[1]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            canvas?.drawLine(start.x, start.y, end.x, end.y, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
                }

                ShapeMode.TRIANGLE -> {
                    if (currentPoints.size == 3) {
                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]
                        val p3 = currentPoints[2]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            val path = Path()
                            path.moveTo(p1.x, p1.y)
                            path.lineTo(p2.x, p2.y)
                            path.lineTo(p3.x, p3.y)
                            path.close()
                            canvas?.drawPath(path, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
                }

                ShapeMode.POINTS -> {
                    val paintCopy = Paint(paint)
                    paintCopy.strokeWidth = 15f
                    shapes.add(Pair({
                        canvas?.drawPoint(point.x, point.y, paintCopy)
                    }, paintCopy))
                    invalidate()
                }
                ShapeMode.RECTANGLE -> {
                    if (currentPoints.size == 2) {
                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            canvas?.drawRect(p1.x, p1.y, p2.x, p2.y, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
                }
                ShapeMode.ROUNDED_RECTANGLE -> {
                    if (currentPoints.size == 2) {
                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            canvas?.drawRoundRect(
                                RectF(p1.x, p1.y, p2.x, p2.y),
                                50f, 50f, // Corner radius X and Y
                                paintCopy
                            )
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
                }

                else -> {}
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        shapes.forEach { it.first() }
    }

    fun clearCanvas() {
        shapes.clear()
        currentPoints.clear()
        invalidate()
    }

    private fun distance(p1: PointF, p2: PointF): Float {
        return Math.hypot((p2.x - p1.x).toDouble(), (p2.y - p1.y).toDouble()).toFloat()
    }

    private var canvas: Canvas? = null
}
*/



// for preview

class MyCanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var shapeMode: ShapeMode = ShapeMode.NONE
    private val currentPoints = mutableListOf<PointF>()
    private val shapes = mutableListOf<Pair<(Canvas) -> Unit, Paint>>()
    private var previewPoint: PointF? = null
    private var cornerRadius: Float = 50f

    private val paint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 6f
        style = Paint.Style.FILL_AND_STROKE
        isAntiAlias = true
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

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPoints.clear()
                currentPoints.add(point)
                previewPoint = point
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
                } else if (currentPoints.isNotEmpty()) {
                    currentPoints.add(point)
                    val paintCopy = Paint(paint)

                    when (shapeMode) {
                        ShapeMode.CIRCLE -> {
                            if (currentPoints.size >= 2) {
                                val center = currentPoints[0]
                                val edge = currentPoints[1]
                                val radius = distance(center, edge)
                                shapes.add(Pair({
                                    it.drawCircle(center.x, center.y, radius, paintCopy)
                                }, paintCopy))
                            }
                        }

                        ShapeMode.LINE -> {
                            if (currentPoints.size >= 2) {
                                val start = currentPoints[0]
                                val end = currentPoints[1]
                                shapes.add(Pair({
                                    it.drawLine(start.x, start.y, end.x, end.y, paintCopy)
                                }, paintCopy))
                            }
                        }

                        ShapeMode.RECTANGLE -> {
                            if (currentPoints.size >= 2) {
                                val p1 = currentPoints[0]
                                val p2 = currentPoints[1]
                                shapes.add(Pair({
                                    it.drawRect(p1.x, p1.y, p2.x, p2.y, paintCopy)
                                }, paintCopy))
                            }
                        }

                        ShapeMode.ROUNDED_RECTANGLE -> {
                            if (currentPoints.size >= 2) {
                                val p1 = currentPoints[0]
                                val p2 = currentPoints[1]
                                shapes.add(Pair({
                                    it.drawRoundRect(RectF(p1.x, p1.y, p2.x, p2.y), cornerRadius, cornerRadius, paintCopy)
                                }, paintCopy))
                            }
                        }
                        ShapeMode.TRIANGLE -> {
                            if (currentPoints.size == 3) {
                                val p1 = currentPoints[0]
                                val p2 = currentPoints[1]
                                val p3 = currentPoints[2]
                                val paintCopy = Paint(paint)
                                shapes.add(Pair({
                                    val path = Path()
                                    path.moveTo(p1.x, p1.y)
                                    path.lineTo(p2.x, p2.y)
                                    path.lineTo(p3.x, p3.y)
                                    path.close()
                                    it.drawPath(path, paintCopy)
                                }, paintCopy))
                                currentPoints.clear()
                                invalidate()
                            }
                        }
                      /*  ShapeMode.TRIANGLE -> {
                            if (currentPoints.size == 3) {
                                val p1 = currentPoints[0]
                                val p2 = currentPoints[1]
                                val p3 = currentPoints[2]
                                shapes.add(Pair({
                                    val path = Path()
                                    path.moveTo(p1.x, p1.y)
                                    path.lineTo(p2.x, p2.y)
                                    path.lineTo(p3.x, p3.y)
                                    path.close()
                                    it.drawPath(path, paintCopy)
                                }, paintCopy))
                                currentPoints.clear()
                                previewPoint = null
                            } else {
                                return true // wait for 3rd touch
                            }
                        }*/

                       /* ShapeMode.TRIANGLE -> {
                            if (currentPoints.size == 3) {
                                val p1 = currentPoints[0]
                                val p2 = currentPoints[1]
                                val p3 = currentPoints[2]
                                shapes.add(Pair({
                                    val path = Path()
                                    path.moveTo(p1.x, p1.y)
                                    path.lineTo(p2.x, p2.y)
                                    path.lineTo(p3.x, p3.y)
                                    path.close()
                                    it.drawPath(path, paintCopy)
                                }, paintCopy))
                            }
                        }*/

                        else -> {}
                    }

                    currentPoints.clear()
                    previewPoint = null
                }

                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Finalized shapes
        shapes.forEach { it.first(canvas) }

        // Preview shape
        if (currentPoints.isNotEmpty() && previewPoint != null) {
            val previewPaint = Paint(paint).apply {
                color = Color.GRAY
                pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
            }

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
                    canvas.drawRoundRect(RectF(p1.x, p1.y, p2.x, p2.y), cornerRadius, cornerRadius, previewPaint)
                }

/*
                ShapeMode.TRIANGLE -> {
                    if (currentPoints.size == 3) {
                        val path = Path().apply {
                            moveTo(p1.x, p1.y)
                            lineTo(p2.x, p2.y)
                            lineTo(previewPoint!!.x, previewPoint!!.y)
                            close()
                        }
                        canvas.drawPath(path, previewPaint)
                    }
                }
*/
                ShapeMode.TRIANGLE -> {
                    if (currentPoints.size == 3) {
                        val p1 = currentPoints[0]
                        val p2 = currentPoints[1]
                        val p3 = currentPoints[2]
                        val paintCopy = Paint(paint)
                        shapes.add(Pair({
                            val path = Path()
                            path.moveTo(p1.x, p1.y)
                            path.lineTo(p2.x, p2.y)
                            path.lineTo(p3.x, p3.y)
                            path.close()
                            canvas?.drawPath(path, paintCopy)
                        }, paintCopy))
                        currentPoints.clear()
                        invalidate()
                    }
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
    private fun distance(p1: PointF, p2: PointF): Float {
        return Math.hypot((p2.x - p1.x).toDouble(), (p2.y - p1.y).toDouble()).toFloat()
    }
}


/*enum class ShapeMode {
    NONE,
    CIRCLE,
    LINE,
    TRIANGLE,
    POINTS,
    RECTANGLE,
    ROUNDED_RECTANGLE
}*/


