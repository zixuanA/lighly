package com.mredrock.cyxbs.freshman.Util

import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.mredrock.cyxbs.freshman.R
var index = -1
@ColorInt
fun getAlbumColor():Int{
    index++
    return list[index%5]
}
@ColorInt
fun getAlbumColor(index:Int):Int{
    return list[index%5]
}
@DrawableRes
fun getAlbumIconRes(index:Int): Int{
    return when(index%5){
        1-> R.drawable.ic_lighting
        2-> R.drawable.ic_ball
        3-> R.drawable.ic_podium
        4-> R.drawable.ic_safe
        else-> R.drawable.ic_globe
    }
}
private val list = listOf<Int>(0x004682,0x0370b7,0x0295ce,0x07b1bf,0x60f4a0)
class ColorUtil {
}