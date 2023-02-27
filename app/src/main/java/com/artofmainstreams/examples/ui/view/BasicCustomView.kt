package com.artofmainstreams.examples.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.graphics.applyCanvas
import com.artofmainstreams.examples.R
import com.artofmainstreams.examples.ui.toPx
import java.lang.Integer.min

class BasicCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val dstPaint = Paint().apply {
        color = Color.GREEN
    }
    private val srcPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        color = Color.BLUE
    }
    /**
     * Config - какую информацию может хранить и, соответственно, вес
     */
    private val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888).applyCanvas {
        drawCircle(100f, 100f, 100f, dstPaint)
        drawCircle(200f, 200f, 100f, srcPaint)
    }
    private val canvas = Canvas(bitmap)
    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        textSize = 32f.toPx
        textAlign = Paint.Align.LEFT
    }
    private val rectF: RectF
    private val type: Type


    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BasicCustomView, defStyleAttr, 0)
        type = Type.values()[typedArray.getInt(R.styleable.BasicCustomView_type, 0)]
        typedArray.recycle()
        rectF = RectF(0f, 0f, 256f, 128f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 200 // Предполагаемая ширина View
        val desiredHeight = 200 // Предполагаемая высота View

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Задан конкретный размер для ширины
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize) // Размер не должен превышать заданный размер
            else -> desiredWidth // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize // Задан конкретный размер для высоты
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize) // Размер не должен превышать заданный размер
            else -> desiredHeight // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        setMeasuredDimension(width, height) // Устанавливаем фактический размер View
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (type) {
            Type.FILL -> {
                canvas.drawRGB(200, 100, 100)
            }
            Type.CIRCLE -> {
                drawBackground(canvas)
                canvas.drawCircle(width / 2f, height / 2f, height / 2f, paint)
            }
            Type.OVAL -> {
                drawBackground(canvas)
                canvas.drawOval(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
            Type.LINE -> {
                drawBackground(canvas)
                canvas.drawLine(0f, 0f, width / 2f, height / 2f, paint)
            }
            Type.POINT -> {
                drawBackground(canvas)
                canvas.drawPoint(width / 2f, height / 2f, paint)
            }
            Type.RECTANGLE -> {
                drawBackground(canvas)
                canvas.drawRect(20f, 20f, width / 2f, height / 2f, paint)
            }
            Type.ROUND_RECTANGLE -> {
                drawBackground(canvas)
                canvas.drawRoundRect(20f, 20f, width / 2f, height / 2f, 40f, 40f, paint)
            }
            Type.ROUND_ARC_FROM_CENTER -> {
                drawBackground(canvas)
                canvas.drawArc(rectF, 90f, 270f, true, paint)
            }
            Type.ROUND_ARC_NOT_FROM_CENTER -> {
                drawBackground(canvas)
                canvas.drawArc(rectF, 90f, 270f, false, paint)
            }
            Type.TEXT -> {
                // тут я игрался с текстом
                canvas.drawColor(Color.GRAY)
                val text = "Привет"
                canvas.drawText(text, 0, text.length, width / 2f, height / 2f, paint)
                val width = paint.measureText(text)
                val rect = Rect()
                paint.getTextBounds(text, 0, text.lastIndex, rect)
                paint.color = Color.YELLOW
                canvas.drawRect(width / 2f, height / 2f + paint.ascent(), width.toPx, height / 2f + paint.descent(), paint)
                paint.textAlign = Paint.Align.LEFT
                paint.color = Color.CYAN
                canvas.drawText(text, 0, text.length, width / 2f, height / 2f, paint)
                paint.descent() // положительный
                paint.ascent() // отрицательный
            }
            Type.PORTER_DUFF -> {
                canvas.drawBitmap(bitmap, 0f, 0f, dstPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when(event?.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                performClick()
                true
            }
            MotionEvent.ACTION_MOVE -> true
            MotionEvent.ACTION_OUTSIDE -> true
            else -> super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        return true
    }

    /**
     * Метод вызывается если ViewGroup переопределяем
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.RED)
    }

    enum class Type {
        FILL,
        CIRCLE,
        OVAL,
        LINE,
        POINT,
        RECTANGLE,
        ROUND_RECTANGLE,
        ROUND_ARC_FROM_CENTER,
        ROUND_ARC_NOT_FROM_CENTER,
        TEXT,
        PORTER_DUFF,
    }
}