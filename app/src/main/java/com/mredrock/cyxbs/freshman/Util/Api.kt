package com.mredrock.cyxbs.freshman.Util

import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Bean.RawMessageBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {
    @GET("searchMusic/")
    fun getBean(@Query("name") name: String): Call<MessageBean>

    @GET("search")
    fun doSearch(@Query("keyword") name: String, @Query("pageSize") size: Int = 50, @Query("page") page: Int = 0, @Query("type") type: String = "song"): Call<RawMessageBean>

}