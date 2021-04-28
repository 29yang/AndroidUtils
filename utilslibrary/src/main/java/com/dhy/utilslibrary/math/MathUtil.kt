package com.dhy.utilslibrary.math

/**
 * Created by dhy
 * Date: 2020/9/21
 * Time: 12:53
 * describe:
 */
class MathUtil {
    companion object {
        //已知角度和斜边，求直角边
        fun hypotenuseA(long: Long, angle: Int): Double {
            //获得弧度
            var radian = 2 * Math.PI / 360 * angle
            return Math.sin(radian) * long//邻边
        }

        //已知角度和斜边，求直角边
        fun hypotenuseB(long: Long, angle: Int): Double {
            //获得弧度
            var radian = 2 * Math.PI / 360 * angle
            return Math.cos(radian) * long//邻边
        }
    }
}