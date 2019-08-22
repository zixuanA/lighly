package com.mredrock.cyxbs.freshman.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Util.dp2px

class SmallMusicProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var viewWidth = 0f
    private var viewHeight = 0f
    private val framePaint = Paint()
    private val lightPaint = Paint()
    private var radius = 0f
    private var degree = 0f

    init {
        lightPaint.color = Color.WHITE
        lightPaint.isAntiAlias = true
        lightPaint.strokeWidth = dp2px(context, 3f)
        framePaint.color = Color.argb(40,255,255,255)
        framePaint.strokeWidth = dp2px(context, 3f)
        framePaint.isAntiAlias = true
        framePaint.style = Paint.Style.STROKE
        lightPaint.style = Paint.Style.STROKE
        lightPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        viewHeight = height.toFloat()
        viewWidth = width.toFloat()
        radius = if (viewHeight > viewWidth) {
            viewWidth / 2 - dp2px(context,3)
        } else {
            viewHeight / 2- dp2px(context,3)
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas?) {
        canvas?.drawCircle(viewWidth / 2, viewHeight / 2, radius, framePaint)
//        canvas?.translate(viewWidth / 2, viewHeight / 2)
//        canvas?.rotate(270f)
        canvas?.drawArc(dp2px(context, 3f), dp2px(context, 3f), viewWidth - dp2px(context, 3f), viewHeight - dp2px(context, 3f),
                -90f, degree, false, lightPaint)
    }

    //0_1
    fun setDegree(degree: Float) {
        this.degree = degree * 360
        LogUtils.d("MyTag","${this.degree}")
        invalidate()
    }
}