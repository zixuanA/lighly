package com.mredrock.cyxbs.freshman.View

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class AlbumView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var viewHeight = 0
    private var viewWidth = 0
    private var color = Color.rgb(29, 0, 129)
    private var paint = Paint()

    init {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
    }

    fun setBackColor(color: Int) {
        paint.color = color
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        viewHeight = top - bottom
        viewWidth = right - left
    }

}