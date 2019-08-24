package com.mredrock.cyxbs.freshman.Util

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.mredrock.cyxbs.freshman.R

var index = -1
@ColorInt
fun getAlbumColor(): Int {
    index++
    return list[index % 5]
}

@ColorInt
fun getAlbumColor(index: Int): Int {
    return list[index % 5]
}

@DrawableRes
fun getAlbumIconRes(index: Int): Int {
    return when (index % 5) {
        1 -> R.drawable.ic_game
        2 -> R.drawable.ic_ball
        3 -> R.drawable.ic_lightling
        4 -> R.drawable.ic_clock
        else -> R.drawable.ic_bick
    }
}

private val list = listOf(0xFF004682.toInt(), 0xFF0370B7.toInt(), 0xFF0295CE.toInt(), 0xFF07B1BF.toInt(), 0xFF60F4A0.toInt())

class ColorUtil