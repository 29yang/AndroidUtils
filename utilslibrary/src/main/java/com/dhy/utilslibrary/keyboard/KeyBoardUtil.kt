package com.dhy.utilslibrary.keyboard

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by dhy
 * Date: 2020/11/12
 * Time: 14:36
 * describe:
 */
class KeyBoardUtil {
    companion object {
        /**
         * 显示键盘
         *
         * @param et 输入焦点
         */
        @JvmStatic
        fun showInput(context: Context, et: EditText) {
            et.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        /**
         * 隐藏键盘
         */
        @JvmStatic
        fun hideInput(context: Activity) {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val v = context.window.peekDecorView()
            if (null != v) {
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
}