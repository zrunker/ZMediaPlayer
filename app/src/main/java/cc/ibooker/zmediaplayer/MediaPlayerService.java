package cc.ibooker.zmediaplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MediaPlayerService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // 如果是放在raw文件中
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_music);

//        // 如果是放在assets文件中
//        try {
//            AssetFileDescriptor fd = getAssets().openFd("sound_music.mp3");
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        // 如果是放在SD卡中
//        try {
//            mediaPlayer = new MediaPlayer();
//            String path = "/sdcard/sound_music.mp3";
//            mediaPlayer.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        mediaPlayer.reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        // 停止服务
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 得到键值对
        boolean playing = intent.getBooleanExtra("playing", false);
        if (playing) {
            try {
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
