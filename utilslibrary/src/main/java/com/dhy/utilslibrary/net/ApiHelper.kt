package com.dhy.utilslibrary.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by dhy
 * Date: 2020/10/28
 * Time: 16:39
 * describe:
 */
object ApiHelper {
    private var api: ApiServices? = null

    fun api(): ApiServices? {
        if (api == null)
            initApi()
        return api
    }

    /**
     * 初始化api
     */
    fun initApi() {
        val mOkHttpClient = OkHttpClient()
            .newBuilder()
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor(HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .retryOnConnectionFailure(true)
//            .addInterceptor(headerInter)
//            .addInterceptor(LoggingInterceptor())
            .build()
        //网络接口配置
        api = null
        api = Retrofit.Builder()
            .baseUrl(Constant.BaseUrl)
//            .addConverterFactory(ScalarsConverterFactory.create())       //添加字符串的转换器
            .addConverterFactory(GsonConverterFactory.create())          //添加gson的转换器
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())   //添加携程的请求适配器            .client(mOkHttpClient)
            .client(mOkHttpClient)
            .build()
            .create(ApiServices::class.java)
    }

}