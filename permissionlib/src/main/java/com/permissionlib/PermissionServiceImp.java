package com.permissionlib;

import android.content.Context;

import com.caifu.common.permission.IPermissionService;
import com.caifu.common.permission.PermissionCallBack;
import com.google.auto.service.AutoService;
import com.permissionlib.util.AndroidMPermissionHelper;

@AutoService(IPermissionService.class)
public class PermissionServiceImp implements IPermissionService {

    @Override
    public void checkPermission(Context context, PermissionCallBack callBack, String... permissions) {
        AndroidMPermissionHelper.checkPermission(context, callBack, permissions);
    }

}
