package com.baidu.aichinese.module.vplayer.view.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.baidu.aichinese.module.vplayer.R;
import com.baidu.aichinese.module.vplayer.core.MKPlayer;
import com.baidu.aichinese.module.vplayer.core.listener.MKErrorListener;
import com.baidu.aichinese.module.vplayer.view.controller.IMKController;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: AIChinese
 * @description: 媒体View
 * @author: zoufengli01
 * @create: 2021/12/6 11:38 上午
 **/
public class MediaView extends SurfaceView
        implements SurfaceHolder.Callback, IMediaView {
    private MKPlayer vPlayer;
    private boolean isCanPlay;
    private String mediaPath;
    private SurfaceHolder.Callback holderCallBack;
    private MKErrorListener onErrorListener;
    private IMKController imkController;
    private boolean isAutoSize;

    public MediaView(Context context) {
        this(context, null);
    }

    public MediaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.MediaView);
        boolean isZOrderOnTop = typedArr.getBoolean(R.styleable.MediaView_isZOrderOnTop, false);
        isAutoSize = typedArr.getBoolean(R.styleable.MediaView_isAutoSize, false);
        typedArr.recycle();

        // 初始化
        init(isZOrderOnTop);
    }

    private void init(boolean isZOrderOnTop) {
        if (isZOrderOnTop) {
            // 置顶支持透明
            setZOrderOnTop(true);
        } else {
            // 背景色跟主题一致
            setZOrderMediaOverlay(true);
        }
        // 设置SurfaceHolder
        SurfaceHolder sHolder = getHolder();
        sHolder.addCallback(this);
        sHolder.setKeepScreenOn(true);
        sHolder.setFormat(PixelFormat.TRANSPARENT);
        // 处理焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        // 初始化MKPlayer
        vPlayer = new MKPlayer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCanPlay = true;
        requestFocus();
        setDisplay(holder);
        if (!TextUtils.isEmpty(mediaPath) && !isPlaying()) {
            if (!mediaPath.equals(getDataSource())) {
                prepareAsync(mediaPath);
            } else {
                start();
            }
        }
        if (holderCallBack != null) {
            holderCallBack.surfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holderCallBack != null) {
            holderCallBack.surfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCanPlay = false;
        if (isPlaying()) {
            pause();
        }
        if (holderCallBack != null) {
            holderCallBack.surfaceDestroyed(holder);
        }
    }

    @Override
    public void setHolderCallBack(SurfaceHolder.Callback holderCallBack) {
        this.holderCallBack = holderCallBack;
    }

    @Override
    public void bindController(IMKController imkController) {
        this.imkController = imkController;
        if (imkController != null) {
            imkController.register(this);
        }
    }

    @Override
    public boolean isLooping() {
        return vPlayer.isLooping();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean bool) {
        vPlayer.setScreenOnWhilePlaying(bool);
    }

    @Override
    public void setAudioStreamType(int streamType) {
        vPlayer.setAudioStreamType(streamType);
    }

    @Override
    public void setVolume(float left, float right) {
        vPlayer.setVolume(left, right);
    }

    @Override
    public void setLooping(boolean looping) {
        vPlayer.setLooping(looping);
    }

    @Override
    public boolean isPlaying() {
        return vPlayer.isPlaying();
    }

    @Override
    public void prepareAsync(String path) {
        String error = null;
        if (TextUtils.isEmpty(path)) {
            error = "播放地址不能为空！";
        } else {
            this.mediaPath = path;
            if (isCanPlay) {
                try {
                    vPlayer.prepareAsync(path);
                } catch (Exception e) {
                    error = e.getMessage();
                }
            }
        }
        if (onErrorListener != null && !TextUtils.isEmpty(error)) {
            onErrorListener.onError(null, 0, 0, error);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder sHolder) {
        vPlayer.setDisplay(sHolder);
    }

    @Override
    public void start() {
        vPlayer.start();
    }

    @Override
    public void pause() {
        vPlayer.pause();
    }

    @Override
    public void stop() {
        vPlayer.stop();
    }

    @Override
    public void release() {
        vPlayer.release();
    }

    @Override
    public void reset() {
        vPlayer.reset();
    }

    @Override
    public long getCurrentPosition() {
        return vPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long position) {
        vPlayer.seekTo(position);
    }

    @Override
    public long getDuration() {
        return vPlayer.getDuration();
    }

    @Override
    public String getDataSource() {
        return vPlayer.getDataSource();
    }

    @Override
    public int getVideoWidth() {
        return vPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return vPlayer.getVideoHeight();
    }

    @Override
    public int getVideoSarNum() {
        return vPlayer.getVideoSarNum();
    }

    @Override
    public int getVideoSarDen() {
        return vPlayer.getVideoSarDen();
    }

    @Override
    public void setOnErrorListener(MKErrorListener listener) {
        if (vPlayer.getMkListener() != null) {
            onErrorListener = new MKErrorListener() {
                @Override
                public void onError(IMediaPlayer iMediaPlayer, int what, int extra, String error) {
                    if (imkController != null) {
                        imkController.onError();
                    }
                    if (listener != null) {
                        listener.onError(iMediaPlayer, what, extra, error);
                    }
                }
            };
            vPlayer.getMkListener().setOnErrorListener(onErrorListener);
        }
    }

    @Override
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    // 设置SurfaceView大小
                    if (isAutoSize) {
                        int width = vPlayer.getVideoWidth();
                        int height = vPlayer.getVideoHeight();
                        getHolder().setFixedSize(width, height);
                    }
                    if (imkController != null) {
                        imkController.onPrepared();
                    }
                    if (listener != null) {
                        listener.onPrepared(iMediaPlayer);
                    }
                }
            });
        }
    }

    @Override
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    if (imkController != null) {
                        imkController.onCompletion();
                    }
                    if (listener != null) {
                        listener.onCompletion(iMediaPlayer);
                    }
                }
            });
        }
    }

    @Override
    public void setOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnBufferingUpdateListener(listener);
        }
    }

    @Override
    public void setOnInfoListener(IMediaPlayer.OnInfoListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnInfoListener(listener);
        }
    }

    @Override
    public void setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnSeekCompleteListener(listener);
        }
    }

    @Override
    public void setOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnTimedTextListener(listener);
        }
    }

    @Override
    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener) {
        if (vPlayer.getMkListener() != null) {
            vPlayer.getMkListener().setOnVideoSizeChangedListener(listener);
        }
    }
}
