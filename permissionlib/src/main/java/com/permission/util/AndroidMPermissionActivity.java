package com.permission.util;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.permission.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 权限拦截页面
 *
 * @author zhengcf on 2017/7/18.
 */
public class AndroidMPermissionActivity extends AppCompatActivity {

    String[] permissions;
    long code;
    AlertDialog alertDialog = null;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = getIntent().getStringArrayExtra("permissions");
        code = getIntent().getLongExtra("code", 0l);
        requestPermissions(permissions, AndroidMPermissionHelper.REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (alertDialog != null && !alertDialog.isShowing()) {
            onPermissionResult(AndroidMPermissionHelper.isAllPermissionGranted(this, permissions));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (AndroidMPermissionHelper.REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            boolean success = AndroidMPermissionHelper.isAllPermissionGranted(this, permissions, grantResults);
            if (success) {
                onPermissionResult(success);
            } else {
                alertDialog = createPermissionDialog(AndroidMPermissionHelper.getNotGrantedPermission(this, permissions,
                        grantResults));
                alertDialog.show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 当权限返回结果之后的处理
     *
     * @param success
     */
    private void onPermissionResult(boolean success) {
        Intent intent = new Intent(AndroidMPermissionHelper.SECURITY_RESULT_ACTION);
        intent.putExtra("success", success);
        intent.putExtra("code", code);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        finish();
    }

    /**
     * 弹出去设置权限提示框
     *
     * @return
     */
    private AlertDialog createPermissionDialog(ArrayList<String> permissions) {

        return new AlertDialog.Builder(this).setMessage(getString(R.string
                .str_setting_open_permission, getPermissionDialogContent(permissions))).setPositiveButton(R.string.str_go_setting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalUtil.openSystemAppManage(AndroidMPermissionActivity.this, getPackageName());
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onPermissionResult(false);
            }
        }).create();
    }

    /**
     * 获取权限组对应的文字
     *
     * @param permissions
     * @return
     */
    private String getPermissionDialogContent(ArrayList<String> permissions) {
        if (permissions == null) {
            return "";
        }
        HashMap<String, String> permissionContent = initPermissionContent();
        boolean isStorePermission = false;
        if (permissions.size() == 1) {
            isStorePermission = permissions.contains(Constant.PERMISSION_STORAGE) || permissions.contains(Constant.PERMISSION_READ_STORAGE);
        } else if (permissions.size() == 2) {
            isStorePermission = permissions.contains(Constant.PERMISSION_STORAGE) && permissions.contains(Constant.PERMISSION_READ_STORAGE);
        }
        if (isStorePermission) {
            return getString(R.string.str_permission_storage);
        }
        Set<String> permissionNames = new HashSet<>();
        for (String permission : permissions) {
            String permissionName = permissionContent.get(permission);
            if (TextUtils.isEmpty(permissionName)) {
                permissionName = getString(R.string.str_undef);
            }
            permissionNames.add(permissionName);
        }
        List<String> permissionList = new ArrayList<>(permissionNames);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < permissionList.size(); i++) {
            String permissionName = permissionList.get(i);
            builder.append(permissionName);
            if (i < permissionList.size() - 1) {
                builder.append(",");
            }
        }
        return getResources().getString(R.string.str_setting_open_permission, builder.toString());
    }

    /**
     * 初始化权限对应的提示文字
     */
    private HashMap<String, String> initPermissionContent() {
        HashMap<String, String> permissionContent = new HashMap<>();
        permissionContent.put(Constant.PERMISSION_MICROPHONE, getString(R.string.str_permission_microphone));
        permissionContent.put(Constant.PERMISSION_SENSORS, getString(R.string.str_permission_sensors));
        permissionContent.put(Constant.PERMISSION_CALENDAR, getString(R.string.str_permission_calendar));
        permissionContent.put(Constant.PERMISSION_CAMERA, getString(R.string.str_permission_camera));
        permissionContent.put(Constant.PERMISSION_GET_ACCOUNTS, getString(R.string.str_permission_get_accounts));
        permissionContent.put(Constant.PERMISSION_PHONE_STATE, getString(R.string.str_permission_phone_state));
        permissionContent.put(Constant.PERMISSION_LOCATION, getString(R.string.str_permission_location));
        permissionContent.put(Constant.PERMISSION_SMS, getString(R.string.str_permission_sms));
        permissionContent.put(Constant.PERMISSION_STORAGE, getString(R.string.str_permission_storage));
        permissionContent.put(Constant.PERMISSION_BLUETOOTH_SCAN, getString(R.string.str_permission_bluetooth));
        permissionContent.put(Constant.PERMISSION_BLUETOOTH_CONNECT, getString(R.string.str_permission_bluetooth));
        permissionContent.put(Constant.PERMISSION_NEARBY_WIFI_DEVICES, getString(R.string.str_permission_bluetooth));
        permissionContent.put(Constant.PERMISSION_POST_NOTIFICATIONS, getString(R.string.str_permission_notification));
        permissionContent.put(Constant.PERMISSION_READ_MEDIA_AUDIO, getString(R.string.str_read_media_aural));
        permissionContent.put(Constant.PERMISSION_READ_MEDIA_IMAGES, getString(R.string.str_read_media_visual));
        permissionContent.put(Constant.PERMISSION_READ_MEDIA_VIDEO, getString(R.string.str_read_media_visual));
        return permissionContent;
    }
}
