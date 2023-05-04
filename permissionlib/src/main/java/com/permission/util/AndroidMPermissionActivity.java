package com.permission.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
    boolean forceRequest;
    boolean showDialogWhenDenied = true;
    AlertDialog alertDialog = null;
    volatile boolean isFirstTimeResume = true;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = getIntent().getStringArrayExtra(AndroidMPermissionHelper.PERMISSIONS);
        code = getIntent().getLongExtra(AndroidMPermissionHelper.CODE, 0l);
        showDialogWhenDenied = getIntent().getBooleanExtra(AndroidMPermissionHelper.SHOW_DIALOG_WHEN_DENIED, true);
        forceRequest = getIntent().getBooleanExtra(AndroidMPermissionHelper.FORCE_REQUEST, false);
        requestPermissions(permissions, AndroidMPermissionHelper.REQUEST_CODE_ASK_PERMISSIONS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        if (forceRequest) {
            if (AndroidMPermissionHelper.isAllPermissionGranted(this, permissions)) {
                if (alertDialog != null && !alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
                onPermissionResult(true);
            } else {
                if (!isFirstTimeResume && (alertDialog == null || !alertDialog.isShowing())) {
                    requestPermissions(permissions, AndroidMPermissionHelper.REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        } else if (alertDialog != null && !alertDialog.isShowing()) {
            onPermissionResult(AndroidMPermissionHelper.isAllPermissionGranted(this, permissions));
        }

        isFirstTimeResume = false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (AndroidMPermissionHelper.REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            boolean success = AndroidMPermissionHelper.isAllPermissionGranted(permissions, grantResults);
            if (success) {
                onPermissionResult(true);
            } else if (showDialogWhenDenied) {
                alertDialog = createPermissionDialog(AndroidMPermissionHelper.getNotGrantedPermission(permissions,
                        grantResults));
                alertDialog.show();
            } else {
                onPermissionResult(false);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onPermissionResult(boolean success) {
        Intent intent = new Intent(AndroidMPermissionHelper.SECURITY_RESULT_ACTION);
        intent.putExtra("success", success);
        intent.putExtra("code", code);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        finish();
    }

    private AlertDialog createPermissionDialog(ArrayList<String> permissions) {

        return new AlertDialog.Builder(this).setMessage(getString(R.string
                .str_setting_open_permission, AndroidMPermissionHelper.getPermissionDialogContent(this, permissions))).setPositiveButton(R.string.str_go_setting, new DialogInterface.OnClickListener() {
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


}
