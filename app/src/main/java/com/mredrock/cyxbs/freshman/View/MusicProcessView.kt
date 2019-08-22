package com.mredrock.cyxbs.freshman.View

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Util.dp2px
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class MusicProcessView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var listener: ProgressChangedListener? = null
    private val outRectF = RectF()
    private val animationSet = AnimatorSet()
    private var pointOutPaints = listOf(Paint(), Paint(), Paint())

    init {

        repeat(3) {

            pointOutPaints[it].strokeCap = Paint.Cap.ROUND
        }


        val animation1 = ValueAnimator.ofFloat(0f, 1f)
        animation1.interpolator = LinearInterpolator()
        animation1.addUpdateListener {
            pointOutPaints[0].alpha = (155 - 155 * (it.animatedValue as Float)).toInt()
            pointOutPaints[0].strokeWidth = dp2px(context, 10 + 30 * it.animatedValue as Float)
            postInvalidate()
        }
        animation1.duration = 3000
        animation1.repeatCount = INFINITE
        val animation2 = ValueAnimator.ofFloat(0f, 1f)
        animation2.addUpdateListener {
            pointOutPaints[1].alpha = (155 - 155 * (it.animatedValue as Float)).toInt()
            pointOutPaints[1].strokeWidth = dp2px(context, 10 + 30 * it.animatedValue as Float)
            postInvalidate()
        }
        animation2.duration = 3000
        animation2.interpolator = LinearInterpolator()
        animation2.repeatCount = INFINITE
        animation2.startDelay = 1000
        val animation3 = ValueAnimator.ofFloat(0f, 1f)
        animation3.addUpdateListener {
            pointOutPaints[2].alpha = (155 - 155 * (it.animatedValue as Float)).toInt()
            pointOutPaints[2].strokeWidth = dp2px(context, 10 + 30 * it.animatedValue as Float)
            postInvalidate()
        }
        animation3.duration = 3000

        animation3.repeatCount = INFINITE
        animation3.startDelay = 2000
        animationSet.playTogether(animation1, animation2, animation3)

    }

    fun setColor(color: Int?) {
        if(color!=null){
        repeat(pointOutPaints.size) {
            pointOutPaints[it].color = color
        }
        outPathPaint.color = color
        }
    }


    private val framePaint = Paint()
    private val outPathPaint = Paint()
    private val whitePaint = Paint()

    init {
        whitePaint.color = Color.WHITE
        whitePaint.strokeWidth = 15f
        whitePaint.strokeCap = Paint.Cap.ROUND

        outPathPaint.color = Color.BLUE
        outPathPaint.strokeCap = Paint.Cap.ROUND
//        outPathPaint.color = Color.rgb(207,207,207)
        outPathPaint.style = Paint.Style.STROKE
        outPathPaint.strokeWidth = dp2px(context, 10f)
        outPathPaint.isAntiAlias = true

//        framePaint.color = Color.RED
        framePaint.color = Color.rgb(207, 207, 207)
        framePaint.style = Paint.Style.STROKE
    }

    private var outRadius = 0f
        set(value) {
            field = value
            insideRadius = (insideRadiusProportion * value)
        }
    private var insideRadius = 0f
    private var insideRadiusProportion = 0.75f
    private var viewRadius = 0f
    //一个圆的几分之几 最大为1
    private var outPathDegree = 0.1f
    private var pointCanMove = false

    //0_1
    fun setOutPathDegree(degree: Float) {

        outPathDegree = 0.1f + 0.3f * degree
        invalidate()
    }

    fun setOutPathWidth(width: Float) {
        outPathPaint.strokeWidth = width
    }

    fun setOnProgressChangedListener(listener: ProgressChangedListener) {
        this.listener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthModel = MeasureSpec.getMode(widthMeasureSpec)
        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        if (width < height) {
            outRadius = width / 2 - dp2px(context, 20f)
            viewRadius = width / 2f
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, widthModel), MeasureSpec.makeMeasureSpec(width, heightModel))
        } else {
            outRadius = height/2 - dp2px(context, 20f)
            viewRadius = height/2f
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(height, widthModel), MeasureSpec.makeMeasureSpec(height, heightModel))
        }
    }

    override fun onDraw(canvas: Canvas?) {

        drawOutCircle(canvas)
        drawPoint(canvas)

    }

    private fun drawOutCircle(canvas: Canvas?) {
        canvas?.save()


        canvas?.drawCircle(viewRadius, viewRadius, outRadius, framePaint)

        outRectF.bottom = viewRadius + outRadius
        outRectF.left = viewRadius - outRadius
        outRectF.right = viewRadius + outRadius
        outRectF.top = viewRadius - outRadius


//        canvas?.drawArc(outRectF,0f,outPathDegree,true,outPathPaint)
        canvas?.rotate(270f, viewRadius, viewRadius)
//        canvas?.translate(viewRadius,viewRadius)
        canvas?.drawArc(viewRadius - outRadius, viewRadius - outRadius, viewRadius + outRadius, viewRadius + outRadius, 36f, outPathDegree * 360 - 36, false, outPathPaint)

        canvas?.restore()


    }

    private fun drawPoint(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(viewRadius, viewRadius)
        canvas?.rotate(270f)
        canvas?.drawPoint(outRadius * cos(2 * 3.14f * outPathDegree), outRadius * sin(2 * 3.14f * outPathDegree), pointOutPaints[0])
        canvas?.drawPoint(outRadius * cos(2 * 3.14f * outPathDegree), outRadius * sin(2 * 3.14f * outPathDegree), pointOutPaints[1])
        canvas?.drawPoint(outRadius * cos(2 * 3.14f * outPathDegree), outRadius * sin(2 * 3.14f * outPathDegree), pointOutPaints[2])
        canvas?.drawPoint(outRadius * cos(2 * 3.14f * outPathDegree), outRadius * sin(2 * 3.14f * outPathDegree), whitePaint)

    }

    fun startAnimation() {
        animationSet.start()

    }

    fun pauseAnimation() {
        animationSet.pause()

    }

    fun destroyAnimation() {
        animationSet.end()

    }
    private var function:((pointCanTouch:Boolean)->Unit)?=null
    fun toucheventCallback(function:(pointCanTouch:Boolean)->Unit){
        this.function = function
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN

                && outRadius * cos(2 * 3.14f * outPathDegree) - dp2px(context, 30) < -event.y + viewRadius
                && -event.y + viewRadius < outRadius * cos(2 * 3.14f * outPathDegree) + dp2px(context, 30)
                && outRadius * sin(2 * 3.14f * outPathDegree) - dp2px(context, 30) < event.x - viewRadius
                && event.x - viewRadius < outRadius * sin(2 * 3.14f * outPathDegree) + dp2px(context, 30)) {


            pointCanMove = true
            function?.invoke(pointCanMove)
            return true
        }
        if (event?.action == MotionEvent.ACTION_MOVE && pointCanMove) {
//            val x =event.x - viewRadius
//            val y = event.y - viewRadius
            val x = -event.y + viewRadius
            val y = event.x - viewRadius
            if (outRadius * cos(2 * 3.14f * outPathDegree) - dp2px(context, 30) < -event.y + viewRadius
                    && -event.y + viewRadius < outRadius * cos(2 * 3.14f * outPathDegree) + dp2px(context, 30)
                    && outRadius * sin(2 * 3.14f * outPathDegree) - dp2px(context, 30) < event.x - viewRadius
                    && event.x - viewRadius < outRadius * sin(2 * 3.14f * outPathDegree) + dp2px(context, 30)) {


//                        outPathDegree = acos(((outRadius * outRadius - y * y + x * x).toDouble() / (2 * x * outRadius))).toFloat() / (2 * 3.14).toFloat()
//                       if(acos((2*outRadius*outRadius-(y*y+(outRadius-x)*(outRadius-x)))/(2*outRadius*outRadius))/ (2 * 3.14).toFloat()>0
//                               &&acos((2*outRadius*outRadius-(y*y+(outRadius-x)*(outRadius-x)))/(2*outRadius*outRadius))/ (2 * 3.14).toFloat()<1
//                               )

                //下次我一定好好学数学
                if (abs(acos((2 * outRadius * outRadius - (y * y + (outRadius - x) * (outRadius - x))) / (2 * outRadius * outRadius))) / (2 * 3.14).toFloat() < 0.4
                        && abs(acos((2 * outRadius * outRadius - (y * y + (outRadius - x) * (outRadius - x))) / (2 * outRadius * outRadius))) / (2 * 3.14).toFloat() > 0.1)
                    outPathDegree = abs(acos((2 * outRadius * outRadius - (y * y + (outRadius - x) * (outRadius - x))) / (2 * outRadius * outRadius))) / (2 * 3.14).toFloat()
                invalidate()
                listener?.onProgressChanged((outPathDegree - 0.1f) / 0.3f)
                return true
            } else {
                LogUtils.d("eMyTag","do function")
                pointCanMove = false
                function?.invoke(pointCanMove)
            }
        }
        if(event?.action==MotionEvent.ACTION_UP){
            pointCanMove = false
            function?.invoke(pointCanMove)
        }

        return false
    }

    interface ProgressChangedListener {
        fun onProgressChanged(percentage: Float)
    }
}