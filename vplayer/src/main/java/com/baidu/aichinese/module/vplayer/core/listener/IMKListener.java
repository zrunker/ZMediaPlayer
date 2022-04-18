package com.baidu.aichinese.module.vplayer.core.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: AIChinese
 * @description: 监听接口
 * @author: zoufengli01
 * @create: 2021/12/9 2:38 下午
 **/
public interface IMKListener {

    /**
     * 设置播放异常监听
     */
    void setOnErrorListener(MKErrorListener listener);

    /**
     * 设置预播放监听
     */
    void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener);

    /**
     * 设置播放完成监听
     */
    void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener);

    /**
     * 设置Buffer更新监听
     */
    void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener);

    /**
     * 媒体信息回调
     */
    void setOnInfoListener(IMediaPlayer.OnInfoListener listener);

    /**
     * Seek完成回调
     */
    void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener);

    /**
     * 可用定时文本回调 - 字幕
     */
    void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener);

    /**
     * 视频大小改变监听
     */
    void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener);
}
