package com.dhy.utilslibrary.math

import android.text.TextUtils
import java.math.BigDecimal
import java.math.BigInteger

object NumberUtil {
    /**
     * 字符串转数字
     *
     * @param radix        进制,默认10
     * @param defaultValue 若src isEmpty或者转换失败,则返回默认值
     */
    fun str2Int(src: String, radix: Int, defaultValue: Int = 0): Int {
        return if (TextUtils.isEmpty(src)) {
            defaultValue
        } else try {
            src.toInt(radix)
        } catch (e: NumberFormatException) {
            throw NumberFormatException("String 只能包含数字")
        }
    }

    fun str2Long(src: String, radix: Int, defaultValue: Long): Long {
        return if (TextUtils.isEmpty(src)) {
            defaultValue
        } else try {
            src.toLong(radix)
        } catch (e: NumberFormatException) {
            throw NumberFormatException("String 只能包含数字")
        }
    }

    fun str2Float(src: String, defaultValue: Float): Float {
        return if (TextUtils.isEmpty(src)) {
            defaultValue
        } else try {
            src.toFloat()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("String 只能包含数字")
        }
    }

    fun str2Double(src: String, defaultValue: Double = 0.0): Double {
        return if (TextUtils.isEmpty(src)) {
            defaultValue
        } else try {
            src.toDouble()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("String 只能包含数字")
        }
    }

    /**
     * 小数保留几位，例：2 -> 2.0 ; 2.1223 -> 2.12 ; 2.1 -> 2.1
     */
    fun any2Digit(f: Any, scale: Int): Double {
        var bg = when (f) {
            is Double -> BigDecimal(f)
            is Int -> BigDecimal(f)
            is String -> BigDecimal(str2Double(f))
            is Long -> BigDecimal(f)
            is BigInteger -> BigDecimal(f)
            else -> throw java.lang.NumberFormatException("f 类型只能是 Double、Int、String、Long、BigInteger")
        }
        return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

}