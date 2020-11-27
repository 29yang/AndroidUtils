package com.dhy.utilslibrary.dptool

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View


/**
 * Created by dhy
 * Date: 2020/10/12
 * Time: 13:37
 * describe:
 */
object DpTools {
    /**
     * dp转px
     * @return int类型px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * dp转px
     * @return float类型px
     */
    fun dp2pxF(context: Context, dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        )
    }

    /**
     * PX转DP
     */
    fun px2dp(context: Activity, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取运行屏幕宽度
     */
    fun getScreenWidth(context: Activity): Int {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels
    }

    /**
     * 获取运行屏幕高度
     */
    fun getScreenHeight(context: Activity): Int {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.heightPixels
    }

    /**
     * 获取控件宽
     */
    fun getWidth(view: View): Int {
        val w: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val h: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(w, h)
        return view.measuredWidth
    }
}