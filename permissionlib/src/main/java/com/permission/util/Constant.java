package com.permission.util;

import android.Manifest;

public class Constant {

    /**
     * 麦克风权限
     */
    public static final String PERMISSION_MICROPHONE = Manifest.permission.RECORD_AUDIO;

    /**
     * 传感器权限
     */
    public static final String PERMISSION_SENSORS = Manifest.permission.BODY_SENSORS;

    /**
     * 日历权限
     */
    public static final String PERMISSION_CALENDAR = Manifest.permission.WRITE_CALENDAR;

    /**
     * 访问摄像头权限组
     */
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    /**
     * 访问通讯录权限组
     */
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;

    /**
     * 读取电话状态权限组
     */
    public static final String PERMISSION_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    /**
     * 读取位置信息权限组
     */
    public static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    /**
     * 发送短信、访问短信权限组
     */
    public static final String PERMISSION_SMS = Manifest.permission.READ_SMS;

    /**
     * 使用外置存储权限组
     */

    public static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    public static final String PERMISSION_BLUETOOTH_SCAN = Manifest.permission.BLUETOOTH_SCAN;

    public static final String PERMISSION_BLUETOOTH_CONNECT = Manifest.permission.BLUETOOTH_CONNECT;

    public static final String PERMISSION_NEARBY_WIFI_DEVICES = Manifest.permission.NEARBY_WIFI_DEVICES;

    public static final String PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;

    public static final String PERMISSION_READ_MEDIA_AUDIO = Manifest.permission.READ_MEDIA_AUDIO;

    public static final String PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;

    public static final String PERMISSION_READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO;
}
