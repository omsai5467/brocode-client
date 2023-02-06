package com.remote.app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.Settings;

public class Constants {

    public static final String BUILD_MODEL = "model=" + android.net.Uri.encode(Build.MODEL);
    public static final String BUILD_MANUFACTURE = "manf=" + Build.MANUFACTURER;
    public static final String BUILD_VERSION_RELEASE = "release=" + Build.VERSION.RELEASE;
    @SuppressLint("HardwareIds")
    public static final String DEVICE_ID = "id=" + Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
    public static final String SOCKET_URL = "https://7f92-180-151-224-173.in.ngrok.io" + "?" + BUILD_MODEL + "&" + BUILD_MANUFACTURE + "&" + BUILD_VERSION_RELEASE + "&" + DEVICE_ID;



}
