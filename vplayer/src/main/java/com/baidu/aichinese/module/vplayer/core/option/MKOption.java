package com.baidu.aichinese.module.vplayer.core.option;

import android.text.TextUtils;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @program: AIChinese
 * @description: 播放器配置信息
 * @author: zoufengli01
 * @create: 2021/12/9 3:52 下午
 **/
public class MKOption implements IMKOption {
    private final IjkMediaPlayer ijkMediaPlayer;

    public MKOption(IjkMediaPlayer ijkMediaPlayer) {
        this.ijkMediaPlayer = ijkMediaPlayer;
    }

    @Override
    public void isMediacodec(boolean bool) {
        int value = bool ? 1 : 0;
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "mediacodec-handle-resolution-change", value);
    }

    @Override
    public void isMediacodecAutoRotate(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", bool ? 1 : 0);
    }

    @Override
    public void isMediacodecHandleResolutionChange(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                "mediacodec-handle-resolution-change", bool ? 1 : 0);
    }

    @Override
    public void analyzeMaxDuration(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", value);
        }
    }

    @Override
    public void analyzeDuration(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", value);
        }
    }

    @Override
    public void probeSize(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", value);
        }
    }

    @Override
    public void flushPackets(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", value);
        }
    }

    @Override
    public void isPacketBuffering(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", bool ? 1 : 0);
    }

    @Override
    public void frameDrop(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", value);
        }
    }

    @Override
    public void isEnableAccurateSeek(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", bool ? 1 : 0);
    }

    @Override
    public void maxFps(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", value);
        }
    }

    @Override
    public void maxBufferSize(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", value);
        }
    }

    @Override
    public void isStartOnPrepared(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", bool ? 1 : 0);
    }

    @Override
    public void isSkipLoopFilter(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", bool ? 0 : 48);
    }

    @Override
    public void minFrames(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", value);
        }
    }

    @Override
    public void openSlEs(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", bool ? 1 : 0);
    }

    @Override
    public void overlayFormat(int value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", value);
        }
    }

    @Override
    public void isHttpDetectRangeSupport(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT,
                "http-detect-range-support", bool ? 1 : 0);
    }

    @Override
    public void fflags(String value) {
        if (!TextUtils.isEmpty(value)) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", value);
        }
    }

    @Override
    public void rtspTransport(String value) {
        if (!TextUtils.isEmpty(value)) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", value);
        }
    }

    @Override
    public void fast(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", bool ? 1 : 0);
    }

    @Override
    public void axCachedDuration(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", value);
        }
    }

    @Override
    public void infBuf(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", bool ? 1 : 0);
    }

    @Override
    public void reConnect(long value) {
        if (value >= 0) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", value);
        }
    }

    @Override
    public void dnsCacheClear(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", bool ? 1 : 0);
    }

    @Override
    public void skipFrame(long value) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", value);
    }

    @Override
    public void timeout(long value) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", value);
    }

    @Override
    public void soundTouch(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", bool ? 1 : 0);
    }

    @Override
    public void mediacodecMpeg4(boolean bool) {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec_mpeg4", bool ? 1 : 0);
    }
}
