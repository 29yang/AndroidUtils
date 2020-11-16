package com.dhy.utilslibrary.permission;

import android.app.Activity;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    //在此添加所有配置文件中的权限 根据需求修改
    static String[] permission = new String[]{
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.READ_PHONE_STATE",
            "android.permission.WAKE_LOCK",
            "android.permission.INTERNET",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.CALL_PHONE",
            "android.permission.PROCESS_OUTGOING_CALLS",
            "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
            "android.permission.CHANGE_NETWORK_STATE",
            "android.permission.RECEIVE_BOOT_COMPLETED",
            "android.permission.GET_TASKS",
            "android.permission.CAMERA",
            "android.permission.WRITE_SETTINGS"};

    public static boolean checkPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0
                && ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0
                && ContextCompat.checkSelfPermission(context, "android.permission.CAMERA") == 0
                && ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {

            return true;
        }

        return false;
    }

    /**
     * 查找未获取权限
     *
     * @param activity
     * @return 权限数组
     */
    public static String[] requestPermission(Activity activity) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < permission.length; ++i) {
            int checkPermission = ContextCompat.checkSelfPermission(activity, permission[i]);
            if (checkPermission != 0) {
                list.add(permission[i]);
            }
        }
        if (list.size() > 0) {
            String[] temp = new String[list.size()];
            list.toArray(temp);
            return temp;
        } else {
            return null;
        }
    }
}
