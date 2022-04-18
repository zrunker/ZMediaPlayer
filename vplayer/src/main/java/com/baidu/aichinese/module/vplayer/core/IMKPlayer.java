package cc.ibooker.maoker.vplayer.core;

import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @program: AIChinese
 * @description: 媒体播放对外接口
 * @author: zoufengli01
 * @create: 2021/12/6 11:12 上午
 **/
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
