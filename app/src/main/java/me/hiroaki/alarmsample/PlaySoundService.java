package me.hiroaki.alarmsample;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import java.io.IOException;

public class PlaySoundService extends Service implements MediaPlayer.OnCompletionListener{

    MediaPlayer mediaPlayer;
    float volume = 0.3f;

    public PlaySoundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        play();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 再生
    private void play() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, getVideoUri(R.raw.sample));
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 停止
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private Uri getVideoUri(@RawRes int resId){
        return Uri.parse("android.resource://" + getPackageName() +"/" + resId);
    }

    // 再生が終わる度に音量を上げてループ再生
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        volume += 0.1f;
        play();
    }
}
