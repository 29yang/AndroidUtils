package com.dhy.utilslibrary.net

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by dhy
 * Date: 2020/10/28
 * Time: 16:33
 * describe:
 */
interface ApiServices {
    @POST(Constant.Start_Parking)
    fun getStartParkingAsync(@Body request: ReQuestBean): Deferred<ResultBean<Any>>

    @POST(Constant.Start_Calling)
    fun getStartCallingAsync(@Body request: ReQuestBean): Deferred<ResultBean<Any>>

    @POST(Constant.Start_Stop)
    fun getStartStopAsync(@Body request: ReQuestBean): Deferred<ResultBean<Any>>

    @GET("${Constant.Query_State}?vin=${Constant.vId}")
    fun getQueryStateAsync(): Deferred<ResultBean<CarStateBean>>

    @GET(Constant.Text)
    fun getText(): Deferred<ResultBean<Any>>
}