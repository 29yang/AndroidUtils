package com.dhy.utilslibrary.regular

import android.text.TextUtils
import java.util.regex.Pattern


/**
 * Created by dhy
 * Date: 2020/11/27
 * Time: 17:07
 * describe:
 */
class RegularUtils {
    companion object {
        /**
         * 验证手机号（简单）
         */
        private const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"

        /**
         * 验证手机号（精确）
         * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
         * 联通：130、131、132、145、155、156、175、176、185、186
         *
         * 电信：133、153、173、177、180、181、189
         *
         * 全球星：1349
         *
         * 虚拟运营商：170
         */
        private const val REGEX_MOBILE_EXACT =
            "^((13[0-9])|(14[5,7])|(15[0-3,5-8])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"

        /**
         * 验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx/
         */
        private const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"

        /**
         * 验证邮箱
         */
        private const val REGEX_EMAIL =
            "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

        /**
         * 验证url
         */
        private const val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?"

        /**
         * 验证汉字
         */
        private const val REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$"

        /**
         * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
         */
        private const val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$"

        /**
         * 验证IP地址
         */
        private const val REGEX_IP =
            "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

        //If u want more please visit http://toutiao.com/i6231678548520731137/

        //If u want more please visit http://toutiao.com/i6231678548520731137/
        /**
         * @param string 待验证文本
         * @return 是否符合手机号（简单）格式
         */
        fun isMobileSimple(string: String): Boolean {
            return isMatch(REGEX_MOBILE_SIMPLE, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合手机号（精确）格式
         */
        fun isMobileExact(string: String): Boolean {
            return isMatch(REGEX_MOBILE_EXACT, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合座机号码格式
         */
        fun isTel(string: String): Boolean {
            return isMatch(REGEX_TEL, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合邮箱格式
         */
        fun isEmail(string: String): Boolean {
            return isMatch(REGEX_EMAIL, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合网址格式
         */
        fun isURL(string: String): Boolean {
            return isMatch(REGEX_URL, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合汉字
         */
        fun isChz(string: String): Boolean {
            return isMatch(REGEX_CHZ, string)
        }

        /**
         * @param string 待验证文本
         * @return 是否符合用户名
         */
        fun isUsername(string: String): Boolean {
            return isMatch(REGEX_USERNAME, string)
        }

        /**
         * @param regex  正则表达式字符串
         * @param string 要匹配的字符串
         * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
         */
        private fun isMatch(regex: String, string: String): Boolean {
            return !TextUtils.isEmpty(string) && Pattern.matches(regex, string)
        }
    }
}