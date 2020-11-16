package com.dhy.utilslibrary.sputil

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

/**
 * Created by dhy
 * Date: 2020/9/28
 * Time: 11:49
 * describe:
 */
class SpUtil(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("v2x", Context.MODE_PRIVATE)
    private val editor: Editor

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     * @param key
     * @param value
     */
    fun putString(key: String?, value: String?) {
        editor.putString(key, value)
        editor.commit()
    }

    fun putBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun putFloat(key: String?, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun putLong(key: String?, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     * 清空SP里所以数据
     */
    fun clear() {
        editor.clear()
        editor.commit()
    }

    /**
     * 删除SP里指定key对应的数据项
     * @param key
     */
    fun remove(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     * @param key
     * @param defValue
     * @return
     */
    fun getString(key: String?, defValue: String?): String? {
        return preferences.getString(key, defValue)
    }

    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getFloat(key: String?, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    fun getInt(key: String?, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getLong(key: String?, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    /**
     * 判断SP是否包含特定key的数据
     * @param key
     * @return
     */
    operator fun contains(key: String?): Boolean {
        return preferences.contains(key)
    }

    init {
        editor = preferences.edit()
    }
}