package com.dhy.utilslibrary.math

import androidx.annotation.Nullable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dhy
 * Date: 2020/11/12
 * Time: 14:39
 * describe:
 */
class TimeUtil {
    companion object {
        /**
         * long类型时间格式化
         * @param pattern 日期格式 类似 "yyyy-MM-dd HH:mm:ss"
         */
        @JvmStatic
        fun convertToTime(time: Long, @Nullable pattern: String): String {
            val df =
                if (pattern.isNullOrEmpty())
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                else SimpleDateFormat(pattern)
            val date = Date(time)
            return df.format(date)
        }
    }
}