# ZMediaPlayer
Android利用MediaPlayer实现播放音频功能，前置服务音乐播放器等。

## 需求背景：

- 丰富NA端业务功能，支持更小格式视频。
- 从WebView视频播放转移到NA视频播放，提供应用性能。
- 解决视频播放掉帧卡顿等问题。



## 方案对比：

| **方案**         | **优点**                                                     | **缺点（已发现）**                           | **开发难度** |
| ---------------- | ------------------------------------------------------------ | -------------------------------------------- | ------------ |
| Mediaplayer      | Android系统自带组件，开发简单，兼容性好，流程由程序自身控制等。 | 支持视频格式较少，目前对MP4和MP3兼容比较好。 | 初           |
| ExoPlayer        | Google开源Android播放器，开发简单只需要简单的API调用即可，支持格式较多等。 | 退出后台再次进入黑屏。                       | 中           |
| ijkplayer/FFmpeg | 开发者可自己编译配置，可支持任意格式视频，支持硬解码和软解码等。 | SeekTo只支持I帧。                            | 高           |



## ijkPlayer开发流程



1. 编译，支持Https和全格式视频编解码。

#### 加载源码：

```
git clone https://gitee.com/ibooker/ijkplayer.git ijkplayer-android

cd ijkplayer-android

git checkout -b latest k0.8.8
```

#### 

#### **修改配置文件：**

- init-android-libyuv.sh

```
# IJK_LIBYUV_UPSTREAM=https://github.com/Bilibili/libyuv.git
IJK_LIBYUV_UPSTREAM=
https://gitee.com/ibooker/libyuv.git

# IJK_LIBYUV_FORK=https://github.com/Bilibili/libyuv.git
IJK_LIBYUV_FORK=
https://gitee.com/ibooker/libyuv.git
```

- init-android-openssl.sh

```
# IJK_OPENSSL_UPSTREAM=https://github.com/Bilibili/openssl.git
IJK_OPENSSL_UPSTREAM=
https://gitee.com/ibooker/openssl.git

# IJK_OPENSSL_FORK=https://github.com/Bilibili/openssl.git
IJK_OPENSSL_FORK=
https://gitee.com/ibooker/openssl.git
```

- init-android-soundtouch.sh

```
# IJK_SOUNDTOUCH_UPSTREAM=https://github.com/Bilibili/soundtouch.git
IJK_SOUNDTOUCH_UPSTREAM=
https://gitee.com/ibooker/soundtouch.git

# IJK_SOUNDTOUCH_FORK=https://github.com/Bilibili/soundtouch.git
IJK_SOUNDTOUCH_FORK=
https://gitee.com/ibooker/soundtouch.git
```

- init-android.sh

```
# IJK_FFMPEG_UPSTREAM=https://github.com/Bilibili/FFmpeg.git
IJK_FFMPEG_UPSTREAM=
https://gitee.com/ibooker/FFmpeg.git

# IJK_FFMPEG_FORK=https://github.com/Bilibili/FFmpeg.git
IJK_FFMPEG_FORK=
https://gitee.com/ibooker/FFmpeg.git
```



#### NDK编译：

```
./init-android.sh

./init-android-openssl.sh

cd android/contrib
./compile-openssl.sh clean
./compile-openssl.sh all

cd ../..
cd config
rm module.sh
ln -s module-default.sh module.sh

cd ..
cd android/contrib
./compile-ffmpeg.sh clean
./compile-ffmpeg.sh all

cd ..
./compile-ijk.sh all
```

#### 

#### 注意NDK：

ijkplayer脚本里代码限制NDK版本是 11-14。

例如：.bash_profile配置

```
export ANDROID_SDK=/Users/zoufengli01/Library/Android/sdk
export ANDROID_NDK=/Users/zoufengli01/Library/Android/android-ndk-r14b
export PATH=$PATH:$ANDROID_SDK
export PATH=$PATH:$ANDROID_NDK
```



1. 封装MediaView对外提供API。

```
public interface IMKPlayer {

    /**
     * 是否循环
     */
    boolean isLooping();

    /**
     * 屏幕常亮
     */
    void setScreenOnWhilePlaying(boolean bool);

    /**
     * 设置流的类型
     *
     * @param streamtype 类型，如：AudioManager.STREAM_MUSIC
     */
    void setAudioStreamType(int streamtype);

    /**
     * 设置音量
     */
    void setVolume(float left, float right);

    /**
     * 是否循环播放
     *
     * @param looping 是否循环
     */
    void setLooping(boolean looping);

    /**
     * 是否正在播放
     */
    boolean isPlaying();

    /**
     * 播放指定路径视频
     *
     * @param path 资源路径
     */
    void prepareAsync(String path) throws IOException;

    /**
     * 设置呈现
     *
     * @param sHolder Holder显示
     */
    void setDisplay(SurfaceHolder sHolder);

    /**
     * 开始播放
     */
    void start();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 回收
     */
    void release();

    /**
     * 重置
     */
    void reset();

    /**
     * 获取当前进度
     */
    long getCurrentPosition();

    /**
     * 设置进度
     *
     * @param position 进度值
     */
    void seekTo(long position);

    /**
     * 获取资源总长度
     */
    long getDuration();

    /**
     * 获取播放资源
     */
    String getDataSource();

    int getVideoWidth();

    int getVideoHeight();

    int getVideoSarNum();

    int getVideoSarDen();
}
```



1. 包装MKVideoView绑定控制器，并使用。

MKVideoView就是将MediaView进行一次包装，并设置控制器，过程不是很难不在提示，简单说下使用：

- MKVideoView引入布局：

```
<com.baidu.aichinese.module.vplayer.view.MKVideoView
        android:id="@+id/mk_video_view"
        android:layout_width="match_parent"
        android:layout_height="300dp" />
```

- Activity/Fragment中使用：

```
MKVideoView mkVideoView = findViewById(R.id.mk_video_view);
mkVideoView.play("网络地址或本地地址");
```

