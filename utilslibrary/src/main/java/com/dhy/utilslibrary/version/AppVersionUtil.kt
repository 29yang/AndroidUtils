package com.dhy.utilslibrary.version

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by dhy
 * Date: 2020/10/26
 * Time: 17:45
 * describe:
 */
class AppVersionUtil {
    companion object {
        /**
         * 获取当前本地apk的版本号
         *
         * @param context
         * @return
         */
        fun getVersionCode(context: Context): Int {
            var versionCode = 0
            try {
                // 获取apk版本号
                versionCode =
                    context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return versionCode
        }

        /**
         * 获取版本号名称
         *
         * @param context
         * @return
         */
        fun getVerName(context: Context): String {
            var verName = ""
            try {
                verName =
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return verName
        }
    }
}