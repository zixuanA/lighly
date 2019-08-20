package com.mredrock.cyxbs.freshman.Util

import android.R
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query


interface Api {
    @GET("searchMusic/")
    fun getBean(@Query("name") name:String):Call<MessageBean>
}