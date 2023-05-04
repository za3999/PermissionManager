package com.permission.util; /**
 *
 */

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.StringDef;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cf.common.permission.PermissionCallBack;
import com.permission.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限管理Helper类
 *
 * @author zhengcf on 2017/7/18.
 */
public final class AndroidMPermissionHelper {

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    public static final String SECURITY_RESULT_ACTION = "security_result_action";

    public static final String PERMISSIONS = "permissions";

    public static final String CODE = "code";

    public static final String FORCE_REQUEST = "forceRequest";
    public static final String SHOW_DIALOG_WHEN_DENIED = "show_dialog_when_denied";

    public static final String PERMISSION_MICROPHONE = Manifest.permission.RECORD_AUDIO;

    public static final String PERMISSION_SENSORS = Manifest.permission.BODY_SENSORS;

    public static final String PERMISSION_CALENDAR = Manifest.permission.WRITE_CALENDAR;

    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;

    public static final String PERMISSION_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static final String PERMISSION_SMS = Manifest.permission.READ_SMS;

    public static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final String PERMISSION_BLUETOOTH_SCAN = Manifest.permission.BLUETOOTH_SCAN;

    public static final String PERMISSION_BLUETOOTH_CONNECT = Manifest.permission.BLUETOOTH_CONNECT;

    public static final String PERMISSION_NEARBY_WIFI_DEVICES = Manifest.permission.NEARBY_WIFI_DEVICES;

    public static final String PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;

    public static final String PERMISSION_READ_MEDIA_AUDIO = Manifest.permission.READ_MEDIA_AUDIO;

    public static final String PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;

    public static final String PERMISSION_READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO;

    @StringDef({PERMISSION_MICROPHONE, PERMISSION_SENSORS, PERMISSION_CALENDAR, PERMISSION_CAMERA, PERMISSION_GET_ACCOUNTS,
            PERMISSION_PHONE_STATE, PERMISSION_LOCATION, PERMISSION_SMS, PERMISSION_STORAGE, PERMISSION_READ_STORAGE,
            PERMISSION_BLUETOOTH_SCAN, PERMISSION_BLUETOOTH_CONNECT, PERMISSION_NEARBY_WIFI_DEVICES, PERMISSION_POST_NOTIFICATIONS,
            PERMISSION_READ_MEDIA_AUDIO, PERMISSION_READ_MEDIA_IMAGES, PERMISSION_READ_MEDIA_VIDEO,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.BODY_SENSORS, Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.NEARBY_WIFI_DEVICES, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {
    }

    public static HashMap<String, String> permissionContent;

    private static final String TAG = AndroidMPermissionHelper.class.getSimpleName();


    public static void checkPermission(Context context, PermissionCallBack callBack, @Permission String... permissions) {
        checkPermission(new PermissionParam(context, true, false, callBack), permissions);
    }

    public static void checkPermission(Context context, boolean forceRequest, PermissionCallBack callBack, @Permission String... permissions) {
        checkPermission(new PermissionParam(context, true, forceRequest, callBack), permissions);
    }

    public static void checkPermission(PermissionParam param, @Permission String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new RuntimeException("permissions is null");
        }
        permissions = transPermission(permissions);
        if (isAllPermissionGranted(param.getContext(), permissions)) {
            if (param.getCallBack() != null) {
                param.getCallBack().onGranted(true);
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (param.getCallBack() != null) {
                    param.getCallBack().onDenied();
                }
            } else {
                showPermissionActivity(param.getContext(), param, getNotGrantedPermission(param.getContext(), permissions));
            }
        }
    }

    public static boolean isAllPermissionGranted(Context context, String... permissions) {
        permissions = transPermission(permissions);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_CAMERA)) {
                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            } else if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_MICROPHONE)) {
                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            } else if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static String[] transPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return permissions;
        }
        ArrayList<String> permissionList = new ArrayList<>();
        boolean isAdded = false;
        for (String permission : permissions) {
            if (TextUtils.equals(permission, PERMISSION_READ_STORAGE) || TextUtils.equals(permission, PERMISSION_STORAGE)) {
                if (isAdded) {
                    continue;
                }
                permissionList.add(PERMISSION_READ_MEDIA_AUDIO);
                permissionList.add(PERMISSION_READ_MEDIA_IMAGES);
                permissionList.add(PERMISSION_READ_MEDIA_VIDEO);
                isAdded = true;
            } else {
                permissionList.add(permission);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    protected static ArrayList<String> getNotGrantedPermission(String[] permissions, int[] grantResults) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_CAMERA)) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            } else if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_MICROPHONE)) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            } else if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        return permissionList;
    }

    protected static boolean isAllPermissionGranted(String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_CAMERA)) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            } else if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_MICROPHONE)) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            } else if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static String[] getNotGrantedPermission(Context context, String... permissions) {
        ArrayList<String> permissionList = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_CAMERA)) {
                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            } else if (permissions[i].equals(AndroidMPermissionHelper.PERMISSION_MICROPHONE)) {
                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            } else if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    protected static String getPermissionDialogContent(Context context, ArrayList<String> permissions) {
        if (permissions == null) {
            return "";
        }
        if (permissionContent == null) {
            initPermissionContent(context);
        }
        boolean isStorePermission = false;
        if (permissions.size() == 1) {
            isStorePermission = permissions.contains(PERMISSION_STORAGE) || permissions.contains(PERMISSION_READ_STORAGE);
        } else if (permissions.size() == 2) {
            isStorePermission = permissions.contains(PERMISSION_STORAGE) && permissions.contains(PERMISSION_READ_STORAGE);
        }
        if (isStorePermission) {
            return context.getString(R.string.str_permission_store);
        }
        Set<String> permissionNames = new HashSet<>();
        for (String permission : permissions) {
            String permissionName = permissionContent.get(permission);
            if (TextUtils.isEmpty(permissionName)) {
                permissionName = context.getString(R.string.str_undef);
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
        return context.getResources().getString(R.string.str_setting_open_permission, builder.toString());
    }

    private static void showPermissionActivity(Context context, PermissionParam param,
                                               String... permissions) {
        final long code = System.currentTimeMillis();
        Intent intent = new Intent(context, AndroidMPermissionActivity.class);
        intent.putExtra(PERMISSIONS, permissions);
        intent.putExtra(CODE, code);
        intent.putExtra(SHOW_DIALOG_WHEN_DENIED, param.isShowDialogWhenDenied());
        intent.putExtra(FORCE_REQUEST, param.isForceRequest());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final LocalBroadcastManager mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        mLocalBroadcastManager.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getLongExtra(CODE, 0l) == code) {
                    if (param.getCallBack() != null) {
                        if (intent.getBooleanExtra("success", false)) {
                            param.getCallBack().onGranted(false);
                        } else {
                            param.getCallBack().onDenied();
                        }
                    }
                    mLocalBroadcastManager.unregisterReceiver(this);
                }
            }
        }, new IntentFilter(SECURITY_RESULT_ACTION));
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    private static void initPermissionContent(Context context) {
        permissionContent = new HashMap<>();
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_MICROPHONE, context.getString(R.string.str_permission_microphone));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_SENSORS, context.getString(R.string.str_permission_sensors));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_CALENDAR, context.getString(R.string.str_permission_calendar));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_CAMERA, context.getString(R.string.str_permission_camera));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_GET_ACCOUNTS, context.getString(R.string.str_permission_get_accounts));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_PHONE_STATE, context.getString(R.string.str_permission_phone_state));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_LOCATION, context.getString(R.string.str_permission_location));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_SMS, context.getString(R.string.str_permission_sms));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_STORAGE, context.getString(R.string.str_write_permission_storage));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_READ_STORAGE, context.getString(R.string.str_read_permission_storage));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_BLUETOOTH_SCAN, context.getString(R.string.str_permission_bluetooth));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_BLUETOOTH_CONNECT, context.getString(R.string.str_permission_bluetooth));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_NEARBY_WIFI_DEVICES, context.getString(R.string.str_permission_bluetooth));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_POST_NOTIFICATIONS, context.getString(R.string.str_permission_notification));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_READ_MEDIA_AUDIO, context.getString(R.string.read_media_aural));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_READ_MEDIA_IMAGES, context.getString(R.string.read_media_visual));
        permissionContent.put(AndroidMPermissionHelper.PERMISSION_READ_MEDIA_VIDEO, context.getString(R.string.read_media_visual));
    }

    private static String[] getDeniedPermission(Context context, String... permissions) {
        ArrayList<String> permissionList = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                permissionList.add(permissions[i]);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

//    public static boolean isCameraReady() {
//        boolean isCameraReady = false;
//        Camera camera = null;
//        try {
//            camera = Camera.open();
//            Camera.Parameters mParameters = camera.getParameters();
//            camera.setParameters(mParameters);
//            isCameraReady = true;
//        } catch (Exception e) {
//            LogUtils.d(TAG, e.getLocalizedMessage());
//        } finally {
//            if (camera != null) {
//                camera.release();
//            }
//        }
//
//        return isCameraReady;
//    }

}
