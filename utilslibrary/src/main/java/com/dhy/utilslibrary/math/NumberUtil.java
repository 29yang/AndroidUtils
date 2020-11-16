package com.dhy.utilslibrary.math;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

public class NumberUtil {
    /**
     * 字符串转数字
     *
     * @param radix        进制,默认10
     * @param defaultValue 若转换失败,则返回默认值
     */
    public static int str2Int(@Nullable String src, int radix, int defaultValue) {
        if (TextUtils.isEmpty(src)) {
            return defaultValue;
        }

        try {
            assert src != null;
            return Integer.parseInt(src, radix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static long str2Long(@Nullable String src, int radix, long defaultValue) {
        if (TextUtils.isEmpty(src)) {
            return defaultValue;
        }

        try {
            assert src != null;
            return Long.parseLong(src, radix);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static float str2Float(@Nullable String src, float defaultValue) {
        if (TextUtils.isEmpty(src)) {
            return defaultValue;
        }

        try {
            assert src != null;
            return Float.parseFloat(src);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static double str2Double(@Nullable String src, double defaultValue) {
        if (TextUtils.isEmpty(src)) {
            return defaultValue;
        }

        try {
            assert src != null;
            return Double.parseDouble(src);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    //保留几位小数
    public static double double2Digit(@Nullable double f, int scale) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
