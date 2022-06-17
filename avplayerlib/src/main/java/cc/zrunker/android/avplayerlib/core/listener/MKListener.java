package cc.zrunker.android.avplayerlib.core.listener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: AIChinese
 * @description: 监听器
 * @author: zoufengli01
 * @create: 2021/12/9 3:38 下午
 **/
public class MKListener implements IMKListener {
    private final IMediaPlayer mediaPlayer;
    private IMediaPlayer.OnErrorListener onErrorListener;
    private IMediaPlayer.OnPreparedListener onPreparedListener;
    private IMediaPlayer.OnCompletionListener onCompletionListener;
    private IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener;
    private IMediaPlayer.OnInfoListener onInfoListener;
    private IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener;
    private IMediaPlayer.OnTimedTextListener onTimedTextListener;
    private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener;

    public MKListener(IMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        if (onPreparedListener != null) {
            mediaPlayer.setOnPreparedListener(onPreparedListener);
        }
        if (onErrorListener != null) {
            mediaPlayer.setOnErrorListener(onErrorListener);
        }
        if (onCompletionListener != null) {
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
        if (onBufferingUpdateListener != null) {
            mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
        if (onInfoListener != null) {
            mediaPlayer.setOnInfoListener(onInfoListener);
        }
        if (onSeekCompleteListener != null) {
            mediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        }
        if (onTimedTextListener != null) {
            mediaPlayer.setOnTimedTextListener(onTimedTextListener);
        }
        if (onVideoSizeChangedListener != null) {
            mediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        }
    }

    @Override
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (listener != null) {
            this.onPreparedListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnPreparedListener(onPreparedListener);
            }
        }
    }

    @Override
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        if (listener != null) {
            this.onCompletionListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(onCompletionListener);
            }
        }
    }

    @Override
    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        if (listener != null) {
            this.onBufferingUpdateListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
            }
        }
    }

    @Override
    public void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        if (listener != null) {
            this.onInfoListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnInfoListener(onInfoListener);
            }
        }
    }

    @Override
    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener) {
        if (listener != null) {
            this.onSeekCompleteListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
            }
        }
    }

    @Override
    public void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener) {
        if (listener != null) {
            this.onTimedTextListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnTimedTextListener(onTimedTextListener);
            }
        }
    }

    @Override
    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        if (listener != null) {
            this.onVideoSizeChangedListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            }
        }
    }

    @Override
    public void setOnErrorListener(MKErrorListener listener) {
        if (listener != null) {
            this.onErrorListener = listener;
            if (mediaPlayer != null) {
                mediaPlayer.setOnErrorListener(onErrorListener);
            }
        }
    }
}
