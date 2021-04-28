package com.dhy.utilslibrary.sputil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.dhy.utilslibrary.CC;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SpUtils {
    private static final String TAG = "SpUtils";
    private static SpUtils INSTANCE;
    private ConcurrentMap<String, SoftReference<Object>> mCache = new ConcurrentHashMap<>();
    private String mPrefFileName = "SpUtils";
    private Context mContext;
    public static String isCopyResKey = "isCopyResKey";//用来判断是否启动复制aar里的res文件夹
    public static String isCopyAllKey = "isCopyAllKey";//用来判断是否启动复制文件，覆盖影响Res

    /**
     * 构造方法
     */
    public SpUtils() {
        this.mContext = CC.getApplication();
    }

    /**
     * 获取单例
     */
    public static SpUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (SpUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SpUtils();
                }
            }
        }
        return INSTANCE;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE);
    }


    /***
     *   ============================================================contains，remove，clear=================================================================================
     */

//contains
    public boolean contains(String key) {
        return mCache.get(key).get() != null ? true : getSharedPreferences().contains(key);
    }

    //remove
    public static SpUtils remove(String key) {
        return INSTANCE._remove(key);
    }

    private SpUtils _remove(String key) {
        mCache.remove(key);
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
        return INSTANCE;
    }

    //clear
    public static SpUtils clear() {
        return INSTANCE._clear();
    }

    /**
     * 清除缓存
     */
    private SpUtils _clear() {
        mCache.clear();
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
        return INSTANCE;
    }


    /***
     *   ============================================================get,put通用抽取=================================================================================
     */
    private Object readDisk(String key, Object defaultObject) {
        SharedPreferences sp = getSharedPreferences();
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        Log.e(TAG, "you can not read object , which class is " + defaultObject.getClass().getSimpleName());
        return null;
    }


    public <T> SpUtils put(String key, T t) {
        mCache.put(key, new SoftReference<Object>(t));
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        if (t instanceof String) {
            editor.putString(key, (String) t);
        } else if (t instanceof Integer) {
            editor.putInt(key, (Integer) t);
        } else if (t instanceof Boolean) {
            editor.putBoolean(key, (Boolean) t);
        } else if (t instanceof Float) {
            editor.putFloat(key, (Float) t);
        } else if (t instanceof Long) {
            editor.putLong(key, (Long) t);
        } else {
            Log.d(TAG, "you may be put a invalid object :" + t);
            editor.putString(key, t.toString());
        }

        SharedPreferencesCompat.apply(editor);
        return INSTANCE;
    }

    public Object get(String key, Object defval) {   //缓存
        SoftReference reference = mCache.get(key);
        Object val = null;
        //缓存为空从SP读取，并存入缓存
        if (null == reference || null == reference.get()) {
            val = readDisk(key, defval);
            mCache.put(key, new SoftReference<Object>(val));
        }
        //缓存不为空直接取
        val = mCache.get(key).get();
        Log.i(TAG, "sp获取key =" + key + ",get = " + val);
        return val;
    }

/***
 *   ============================================================反射调用=================================================================================
 */


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(final SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    Log.d(TAG, "============apply");
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    editor.commit();
                    Log.d(TAG, "============commit");
                    return null;
                }
            }.execute();
        }
    }

}
