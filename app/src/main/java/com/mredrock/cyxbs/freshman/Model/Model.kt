package com.mredrock.cyxbs.freshman.Model

import com.mredrock.cyxbs.common.BuildConfig
import com.mredrock.cyxbs.common.network.ApiGenerator
import com.mredrock.cyxbs.common.network.converter.QualifiedTypeConverterFactory
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Util.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIME_OUT = 30
class Model (val callback:ModelCallback){

    private var retrofit: Retrofit
    private var okHttpClient: OkHttpClient

    init {
        okHttpClient = configureOkHttp(OkHttpClient.Builder())
        retrofit = Retrofit.Builder()
                .baseUrl("https://api.apiopen.top/")
                .client(okHttpClient)
                .addConverterFactory(QualifiedTypeConverterFactory(GsonConverterFactory.create(), SimpleXmlConverterFactory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
    val apiService = getApiService(Api::class.java)
    fun getBean(name:String){
        apiService.getBean(name).enqueue(object :Callback<MessageBean>{
            override fun onFailure(call: Call<MessageBean>, t: Throwable) {
                callback.onFailure()
            }

            override fun onResponse(call: Call<MessageBean>, response: Response<MessageBean>) {

                callback.onSuccess(response.body())
            }

        })

    }
    private fun configureOkHttp(builder: OkHttpClient.Builder): OkHttpClient {
        builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        return builder.build()
    }
    fun <T> getApiService(clazz: Class<T>) = retrofit.create(clazz)
}