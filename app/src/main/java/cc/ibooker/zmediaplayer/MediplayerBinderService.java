package cc.ibooker.zmediaplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 绑定服务
 * Created by 邹峰立 on 2017/10/17.
 */
public class MediplayerBinderService extends Service {
    private static final int DELETE_PENDINGINTENT_REQUESTCODE = 1022;
    private static final int CONTENT_PENDINGINTENT_REQUESTCODE = 1023;
    private static final int NEXT_PENDINGINTENT_REQUESTCODE = 1024;
    private static final int PLAY_PENDINGINTENT_REQUESTCODE = 1025;
    private static final int STOP_PENDINGINTENT_REQUESTCODE = 1026;
    private static final int NOTIFICATION_PENDINGINTENT_ID = 1;// 是用来标记Notifaction，可用于更新，删除Notifition

    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private RemoteViews views;
    private BroadcastReceiver playerReceiver;

    private String[] musics = {"http://ibooker.cc/ibooker/file_packet/musics/1234.mp3",
            "http://ibooker.cc/ibooker/file_packet/musics/2345.mp3"}; // 设置音频资源（网络）
    private int current_item = 0;
    private boolean isPause = false;

    @Override
    public void onCreate() {
        super.onCreate();

        initMediaPlayer();

        // 设置点击通知结果
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, CONTENT_PENDINGINTENT_REQUESTCODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent delIntent = new Intent(this, MediaPlayerService.class);
        PendingIntent delPendingIntent = PendingIntent.getService(this, DELETE_PENDINGINTENT_REQUESTCODE, delIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 自定义布局
        views = new RemoteViews(getPackageName(), R.layout.layout_mediaplayer);

        // 下一首
        Intent intentNext = new Intent("nextMusic");
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, NEXT_PENDINGINTENT_REQUESTCODE, intentNext, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_next, nextPendingIntent);

        // 暂停/播放
        Intent intentPlay = new Intent("playMusic");
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, PLAY_PENDINGINTENT_REQUESTCODE, intentPlay, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_pause, playPendingIntent);

        // 停止
        Intent intentStop = new Intent("stopMusic");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, STOP_PENDINGINTENT_REQUESTCODE, intentStop, PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_cancel, stopPendingIntent);

        builder = new NotificationCompat.Builder(this)
                // 设置小图标
                .setSmallIcon(R.drawable.test_bg)
                // 设置标题
                .setContentTitle("ZMediaPlayer")
                // 设置内容
                .setContentText("内容")
                // 点击通知后自动清除
                .setAutoCancel(false)
                // 设置点击通知效果
                .setContentIntent(contentPendingIntent)
                // 设置删除时候出发的动作
                .setDeleteIntent(delPendingIntent)
                // 自定义视图
                .setContent(views);

        // 获取NotificationManager实例
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 前台服务
        startForeground(NOTIFICATION_PENDINGINTENT_ID, builder.build());

        // 注册广播
        playerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("action", intent.getAction());
                switch (intent.getAction()) {
                    case "nextMusic":// 下一首
                        nextMusic();
                        break;
                    case "playMusic":// 暂停/播放
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            } else {
                                mediaPlayer.start();
                            }
                        }
                        break;
                    case "stopMusic":// 停止
                        onDestroy();
                        break;
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("nextMusic");
        intentFilter.addAction("playMusic");
        intentFilter.addAction("stopMusic");
        registerReceiver(playerReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 得到键值对
        boolean playing = intent.getBooleanExtra("playing", false);
        if (playing)
            play();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mediaplayerBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (wifiLock != null && wifiLock.isHeld())
            wifiLock.release();
        try {
            if (playerReceiver != null)
                unregisterReceiver(playerReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 取消Notification
        if (notificationManager != null)
            notificationManager.cancel(NOTIFICATION_PENDINGINTENT_ID);
        stopForeground(true);
        // 停止服务
        stopSelf();
    }

    // 初始化MediaPlayer
    private void initMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();

        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
        mediaPlayer.setVolume(0.5f, 0.5f);

        // 设置是否循环播放
        mediaPlayer.setLooping(false);

        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
//        // 当播放的时候一直让屏幕变亮
//        mediaPlayer.setScreenOnWhilePlaying(true);

        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
        wifiLock = ((WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
        wifiLock.acquire();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                isPause = false;
            }
        });

        // 设置播放错误监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.reset();
                return false;
            }
        });

        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextMusic();
            }
        });
    }

    // 播发
    private void play() {
        try {
            // 重置mediaPlayer
            mediaPlayer.reset();
            // 重新加载音频资源
            mediaPlayer.setDataSource(musics[current_item]);
            // 准备播放（异步）
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 暂停
    private void pause() {
        mediaPlayer.pause();
    }

    // 下一首
    private void nextMusic() {
        current_item++;
        if (current_item >= musics.length)
            current_item = 0;
        play();
    }

    // 上一首
    private void preMusic() {
        current_item--;
        if (current_item < 0)
            current_item = musics.length - 1;
        play();
    }

    // 停止
    private void stop() {
        mediaPlayer.stop();
    }

    // 更新Notification
    private void updateNotification() {
        if (views != null) {
            views.setTextViewText(R.id.tv_name, "音乐名" + current_item);
            views.setTextViewText(R.id.tv_author, "作者" + current_item);
            if (!isPause) {
                views.setTextViewText(R.id.tv_pause, "暂停");
            } else {
                views.setTextViewText(R.id.tv_pause, "播放");
            }
        }

        // 刷新notification
        notificationManager.notify(NOTIFICATION_PENDINGINTENT_ID, builder.build());
    }

    public class MediaplayerBinder extends Binder {

        public Service getService() {
            return MediplayerBinderService.this;
        }
    }

    private MediaplayerBinder mediaplayerBinder = new MediaplayerBinder();
}
