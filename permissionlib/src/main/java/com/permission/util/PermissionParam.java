package com.permission.util;

import android.content.Context;

import com.cf.common.permission.PermissionCallBack;

public class PermissionParam {
    Context context;
    boolean showDialogWhenDenied;
    boolean forceRequest;
    PermissionCallBack callBack;

    public PermissionParam(Context context, boolean showDialogWhenDenied, boolean forceRequest, PermissionCallBack callBack) {
        this.context = context;
        this.showDialogWhenDenied = showDialogWhenDenied;
        this.forceRequest = forceRequest;
        this.callBack = callBack;
    }

    public Context getContext() {
        return context;
    }

    public boolean isShowDialogWhenDenied() {
        return showDialogWhenDenied;
    }

    public boolean isForceRequest() {
        return forceRequest;
    }

    public PermissionCallBack getCallBack() {
        return callBack;
    }
}
