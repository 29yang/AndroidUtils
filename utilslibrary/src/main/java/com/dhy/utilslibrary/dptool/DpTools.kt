package com.dhy.utilslibrary.dptool

import android.content.Context
import android.util.TypedValue

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
}