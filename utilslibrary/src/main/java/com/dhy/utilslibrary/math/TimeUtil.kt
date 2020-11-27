package com.dhy.utilslibrary.math

import java.text.ParseException
import java.text.ParsePosition
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
        fun convertToTime(time: Long, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
            val df = SimpleDateFormat(pattern)
            val date = Date(time)
            return df.format(date)
        }

        /**
         * 格式时间转时间戳
         * @param time 字符串时间,注意:格式要与pattern定义的一样
         * @param pattern 要格式化的格式:如time为09:21:12那么pattern为"HH:mm:ss"
         */
        @JvmStatic
        fun formatToLong(time: String, pattern: String): Long {
            return try {
                val df = SimpleDateFormat(pattern)
                val parse = df.parse(time)
                val calendar = Calendar.getInstance()
                calendar.time = parse
                calendar.timeInMillis
            } catch (e: ParseException) {
                e.printStackTrace()
                0
            }
        }

        /**
         * 获取现在时间
         *
         * @return 返回时间类型 默认yyyy-MM-dd HH:mm:ss 格式，可传入类型
         */
        fun getStringToday(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
            val currentTime = Date()
            val formatter =
                SimpleDateFormat(pattern)
            return formatter.format(currentTime)
        }

        /**
         * 将短时间格式字符串转换为时间 yyyy-MM-dd
         *
         * @param strDate
         * @return
         */
        fun strToDate(strDate: String?): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val pos = ParsePosition(0)
            return formatter.parse(strDate, pos)
        }

        /**
         * 得到现在时间
         *
         * @return
         */
        fun getNow(): Date {
            return Date()
        }

        /**
         * 得到现在小时
         */
        fun getHour(): String {
            val currentTime = Date()
            val formatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateString = formatter.format(currentTime)
            return dateString.substring(11, 13)
        }

        /**
         * 得到现在分钟
         *
         * @return
         */
        fun getTime(): String {
            val currentTime = Date()
            val formatter =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateString = formatter.format(currentTime)
            return dateString.substring(14, 16)
        }

        /**
         * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
         */
        fun getTwoHour(st1: String, st2: String): String {
            var kk: Array<String>? = null
            var jj: Array<String>? = null
            kk = st1.split(":").toTypedArray()
            jj = st2.split(":").toTypedArray()
            return if (kk[0].toInt() < jj[0].toInt()) "0" else {
                val y = kk[0].toDouble() + kk[1].toDouble() / 60
                val u = jj[0].toDouble() + jj[1].toDouble() / 60
                if (y - u > 0) (y - u).toString() + "" else "0"
            }
        }

        /**
         * 得到二个日期间的间隔天数
         */
        fun getTwoDay(sj1: String, sj2: String): String? {
            val myFormatter = SimpleDateFormat("yyyy-MM-dd")
            var day: Long = 0
            day = try {
                val date = myFormatter.parse(sj1)
                val mydate = myFormatter.parse(sj2)
                (date.time - mydate.time) / (24 * 60 * 60 * 1000)
            } catch (e: Exception) {
                return ""
            }
            return day.toString() + ""
        }

        /**
         * 时间前推或后推分钟,其中JJ表示分钟.
         */
        fun getPreTime(sj1: String?, jj: String): String {
            val format =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var mydate1 = ""
            try {
                val date1 = format.parse(sj1)
                val Time = date1.time / 1000 + jj.toInt() * 60
                date1.time = Time * 1000
                mydate1 = format.format(date1)
            } catch (e: Exception) {
            }
            return mydate1
        }

        /**
         * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
         */
        fun getNextDay(nowdate: String, delay: String): String? {
            return try {
                val format = SimpleDateFormat("yyyy-MM-dd")
                var mdate: String? = ""
                val d = strToDate(nowdate)
                val myTime = d.time / 1000 + delay.toInt() * 24 * 60 * 60
                d.time = myTime * 1000
                mdate = format.format(d)
                mdate
            } catch (e: Exception) {
                ""
            }
        }

        /**
         * 判断是否润年
         *
         * @param ddate
         * @return
         */
        fun isLeapYear(ddate: String): Boolean {
            /**
             * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
             * 3.能被4整除同时能被100整除则不是闰年
             */
            val d = strToDate(ddate)
            val gc = Calendar.getInstance() as GregorianCalendar
            gc.time = d
            val year = gc[Calendar.YEAR]
            return when {
                year % 400 == 0 -> true
                year % 4 == 0 -> {
                    year % 100 != 0
                }
                else -> false
            }
        }

        /**
         * 返回美国时间格式 26 Apr 06
         *
         * @param str 格式必须是 yyyy-MM-dd
         * @return
         */
        fun getEDate(str: String): String? {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val pos = ParsePosition(0)
            val strtodate = formatter.parse(str, pos)
            val j = strtodate.toString()
            val k = j.split(" ").toTypedArray()
            return k[2] + " " + k[1].toUpperCase() + " " + k[5].substring(2, 4)
        }

    }
}