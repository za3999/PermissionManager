package com.cf.common.permission;

public interface PermissionCallBack {

    void onGranted(boolean alreadyExist);

    void onDenied();

}