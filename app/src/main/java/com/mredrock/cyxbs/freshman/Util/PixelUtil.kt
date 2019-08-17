package com.mredrock.cyxbs.freshman.Util

import android.content.Context
import android.util.TypedValue


fun dp2px(context: Context, dpVal: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(), context.getResources().getDisplayMetrics()).toInt()
}
fun dp2px(context: Context, dpVal: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.getResources().getDisplayMetrics())
}

class PixelUtil {
}