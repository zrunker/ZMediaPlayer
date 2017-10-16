package cc.ibooker.zmediaplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.IOException;

/**
 * 前台服务
 * created by 邹峰立
 */
public class MediaPlayerService extends Service {
    private static final int requestCode = 1023;
    private static final int flags = 1;
    private static final int id = 1;// 是用来标记Notifaction，可用于更新，删除Notifition
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();


        // 前台服务
        // 设置点击通知结果
        Intent intent = new Intent(this, MainActivity.class);
        /*
         * Context context：上下文对象
         * int requestCode：请求码
         * Intent intent：intent桥梁
         * int flags：
         * FLAG_CANCEL_CURRENT：如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，然后重新生成一个 PendingIntent 对象。
         * FLAG_NO_CREATE：如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
         * FLAG_ONE_SHOT：该 PendingIntent 只作用一次。
         * FLAG_UPDATE_CURRENT：如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
         */
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent delIntent = new Intent(this, MediaPlayerService.class);
        PendingIntent delPendingIntent = PendingIntent.getService(this, requestCode, delIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.test_bg);
        // 初始化Notification，Notification三要素：小图标、标题、内容
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                // 设置小图标
//                .setSmallIcon(R.mipmap.ic_launcher)
//                // 设置状态栏的显示的信息
//                .setTicker("这是一个音频播放器")
//                // 设置标题
//                .setContentTitle("ZMediaPlayer")
//                // 设置内容
//                .setContentText("内容")
//                // 设置通知时间，默认为系统发出通知的时间，通常不用设置
//                .setWhen(System.currentTimeMillis())
//                // 设置是否显示时间
//                .setShowWhen(true)
//                // 设置大图标-展开时一些手机上大图标会替换掉小图标
//                .setLargeIcon(largeIcon)
//                // 点击通知后自动清除
//                .setAutoCancel(false)
//                // 设置点击通知效果
//                .setContentIntent(contentPendingIntent)
//                // 设置删除时候出发的动作
//                .setDeleteIntent(delPendingIntent)
////                // 自定义视图
////                .setContent(RemoteViews views)
//                // 设置额外信息，一般显示在右下角
//                .setContentInfo("额外信息");



        // 自定义布局
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.layout_mediaplayer);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // 设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                // 设置标题
                .setContentTitle("ZMediaPlayer")
                // 设置内容
                .setContentText("内容")
//                // 设置状态栏的显示的信息
//                .setTicker("这是一个音频播放器")
//                // 设置通知时间，默认为系统发出通知的时间，通常不用设置
//                .setWhen(System.currentTimeMillis())
//                // 设置是否显示时间
//                .setShowWhen(true)
//                // 设置大图标-展开时一些手机上大图标会替换掉小图标
//                .setLargeIcon(largeIcon)
//                // 设置额外信息，一般显示在右下角
//                .setContentInfo("额外信息")
                // 点击通知后自动清除
                .setAutoCancel(false)
                // 设置点击通知效果
                .setContentIntent(contentPendingIntent)
                // 设置删除时候出发的动作
                .setDeleteIntent(delPendingIntent)
//                .setCustomBigContentView(views)
//                .setCustomContentView(views)
//                .setCustomHeadsUpContentView(views)
//                // 自定义视图
                .setContent(views);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        Notification notification = new Notification();
//        if(android.os.Build.VERSION.SDK_INT >= 16) {
//            notification = builder.build();
//            notification.bigContentView = views;
//        }
//        notification.contentView = views;


        Notification notification = builder.build();
        // 获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 发送通知，并设置id
        notificationManager.notify(id, notification);


//        // 更新Notification
//        notification = builder.setContentTitle("ZMediaPlayer111")
//                // 设置内容
//                .setContentText("内容111")
//                .build();
//        notificationManager.notify(id, notification);


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

        // 如果是放在raw文件中
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_music);
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
