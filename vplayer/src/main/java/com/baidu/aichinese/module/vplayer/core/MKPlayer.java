package com.baidu.aichinese.module.vplayer.core;

import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.baidu.aichinese.module.vplayer.core.listener.IMKListener;
import com.baidu.aichinese.module.vplayer.core.listener.MKListener;
import com.baidu.aichinese.module.vplayer.core.option.IMKOption;
import com.baidu.aichinese.module.vplayer.core.option.MKOption;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @program: AIChinese
 * @description: 媒体播放实现类
 * @author: zoufengli01
 * @create: 2021/12/6 11:03 上午
 **/
public class MKPlayer implements IMKPlayer {
    private IMediaPlayer mediaPlayer;
    private IMKListener mkListener;
    private IMKOption mkOption;

    public IMKListener getMkListener() {
        return mkListener;
    }

    public IMKOption getMkOption() {
        return mkOption;
    }

    public MKPlayer() {
        createPlayer();
    }

    private void createPlayer() {
//        // 回收旧Player
//        this.release();
        // 创建新的Player
        if (mediaPlayer == null) {
            IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
            // 设置日志类型
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
            // 配置信息
            initOption(ijkMediaPlayer);
            // 重新赋值
            mediaPlayer = ijkMediaPlayer;
            // 设置监听
            mkListener = new MKListener(mediaPlayer);
        }
    }

    /**
     * 初始化配置
     */
    private void initOption(IjkMediaPlayer ijkMediaPlayer) {
        if (ijkMediaPlayer != null) {
            mkOption = new MKOption(ijkMediaPlayer);
            mkOption.isMediacodec(true);
            mkOption.analyzeMaxDuration(100L);
            mkOption.analyzeDuration(1L);
            mkOption.probeSize(512L);
            mkOption.flushPackets(1L);
            mkOption.frameDrop(1L);
            mkOption.isEnableAccurateSeek(true);
            mkOption.isPacketBuffering(false);
            mkOption.maxBufferSize(10240L);
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public boolean isLooping() {
        if (mediaPlayer != null) {
            return mediaPlayer.isLooping();
        }
        return false;
    }

    @Override
    public void setScreenOnWhilePlaying(boolean bool) {
        if (mediaPlayer != null) {
            mediaPlayer.setScreenOnWhilePlaying(bool);
        }
    }

    @Override
    public void setAudioStreamType(int streamType) {
        if (mediaPlayer != null) {
            mediaPlayer.setAudioStreamType(streamType);
        }
    }

    @Override
    public void setVolume(float left, float right) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(left, right);
        }
    }

    @Override
    public void setLooping(boolean looping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }

    @Override
    public void prepareAsync(@NonNull String path) throws IOException {
        this.createPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        }
    }

    @Override
    public void setDisplay(SurfaceHolder sHolder) {
        this.createPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(sHolder);
        }
    }

    @Override
    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(long position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public String getDataSource() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDataSource();
        }
        return null;
    }

    @Override
    public int getVideoWidth() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override
    public int getVideoSarNum() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoSarNum();
        }
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        if (mediaPlayer != null) {
            return mediaPlayer.getVideoSarDen();
        }
        return 0;
    }
}
