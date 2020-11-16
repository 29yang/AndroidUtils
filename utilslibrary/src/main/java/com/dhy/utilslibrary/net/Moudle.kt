package com.dhy.utilslibrary.net

/**
 * Created by dhy
 * Date: 2020/10/28
 * Time: 14:19
 * describe:
 */

data class ResultBean<T>(val code: Int, val msg: String, val data: T)

data class ReQuestBean(val vin: String)

data class CarStateBean(val state: String, val position: String, val finishTime: String)
