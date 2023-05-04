package com.cf.permissionmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cf.common.ServiceManager;
import com.permission.util.AndroidMPermissionHelper;
import com.cf.common.permission.IPermissionService;
import com.cf.common.permission.PermissionCallBack;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View view) {
        IPermissionService permissionService = ServiceManager.getServices(IPermissionService.class);
        if (permissionService == null) {
            return;
        }
        permissionService.checkPermission(this, new PermissionCallBack() {
                    @Override
                    public void onGranted(boolean alreadyExist) {
                        Toast.makeText(MainActivity.this, "权限请求成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(MainActivity.this, "权限请求失败", Toast.LENGTH_SHORT).show();
                    }
                }, AndroidMPermissionHelper.PERMISSION_CAMERA,
                AndroidMPermissionHelper.PERMISSION_PHONE_STATE,
                AndroidMPermissionHelper.PERMISSION_GET_ACCOUNTS,
                AndroidMPermissionHelper.PERMISSION_SMS);
    }
}
