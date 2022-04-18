package com.baidu.aichinese.module.vplayer.view.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.aichinese.module.vplayer.R;
import com.baidu.aichinese.module.vplayer.view.base.MediaView;

import java.lang.ref.WeakReference;

/**
 * @program: AIChinese
 * @description: 媒体控制器
 * @author: zoufengli01
 * @create: 2021/12/10 11:11 上午
 **/
public class MKController extends FrameLayout
        implements IMKController, View.OnClickListener {
    private MediaView mediaView;
    private ImageView ivPausePlay;
    private ImageView ivRepeat;
    private ImageView ivSkipPrevious;
    private ImageView ivFastRewind;
    private ImageView ivFastForward;
    private ImageView ivSkipNext;
    private TextView tvCurrentD;
    private TextView tvTotalD;
    private SeekBar sbProgress;

    private ZDelayHandler mHandler;
    private final int delayWhat = 11;

    private TextView tvBrightnessVolume;

    private static class ZDelayHandler extends Handler {
        WeakReference<MKController> mWeakRef;

        ZDelayHandler(MKController mkController) {
            mWeakRef = new WeakReference<>(mkController);
        }

        @Override
        public void handleMessage(Message msg) {
            MKController mkController = mWeakRef.get();
            if (mkController != null
                    && mkController.mediaView != null
                    && msg.what == mkController.delayWhat) {
                long cPosition = mkController.mediaView.getCurrentPosition();
                mkController.tvCurrentD.setText(mkController.longToTime(cPosition));
                mkController.sbProgress.setProgress((int) cPosition);
                if (mkController.mediaView.isPlaying()
                        && cPosition < mkController.sbProgress.getMax()) {
                    mkController.openUpdateUiHandler();
                }
            }
        }
    }

    public MKController(Context context) {
        this(context, null);
    }

    public MKController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MKController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.MKController);
        boolean isShowSkipPrevious = typedArr.getBoolean(R.styleable.MKController_isShowSkipPrevious, true);
        setShowSkipPrevious(isShowSkipPrevious);
        boolean isShowSkipNext = typedArr.getBoolean(R.styleable.MKController_isShowSkipNext, true);
        setShowSkipNext(isShowSkipNext);
        boolean isFastRewind = typedArr.getBoolean(R.styleable.MKController_isShowFastRewind, true);
        setShowFastRewind(isFastRewind);
        boolean isFastForward = typedArr.getBoolean(R.styleable.MKController_isShowFastForward, true);
        setShowFastForward(isFastForward);
        typedArr.recycle();
    }

    public void setShowSkipPrevious(boolean isShowSkipPrevious) {
        ivSkipPrevious.setVisibility(isShowSkipPrevious ? VISIBLE : GONE);
    }

    public void setShowSkipNext(boolean isShowSkipNext) {
        ivSkipNext.setVisibility(isShowSkipNext ? VISIBLE : GONE);
    }

    public void setShowFastRewind(boolean isShowFastRewind) {
        ivFastRewind.setVisibility(isShowFastRewind ? VISIBLE : GONE);
    }

    public void setShowFastForward(boolean isShowFastForward) {
        ivFastForward.setVisibility(isShowFastForward ? VISIBLE : GONE);
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     */
    private void init(Context context) {
        // 控制按钮
        View bottomView = LayoutInflater.from(context).inflate(R.layout.mk_controller, this, false);
        addView(bottomView, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
        ivSkipPrevious = findViewById(R.id.iv_skip_previous);
        ivSkipPrevious.setOnClickListener(this);
        ivFastRewind = findViewById(R.id.iv_fast_rewind);
        ivFastRewind.setOnClickListener(this);
        ivPausePlay = findViewById(R.id.iv_pause_play);
        ivPausePlay.setOnClickListener(this);
        ivFastForward = findViewById(R.id.iv_fast_forward);
        ivFastForward.setOnClickListener(this);
        ivSkipNext = findViewById(R.id.iv_skip_next);
        ivSkipNext.setOnClickListener(this);
        ivRepeat = findViewById(R.id.iv_repeat);
        ivRepeat.setOnClickListener(this);
        tvCurrentD = findViewById(R.id.tv_current_d);
        tvTotalD = findViewById(R.id.tv_total_d);
        sbProgress = findViewById(R.id.sb_progress);
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaView != null && fromUser) {
                    mediaView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        resetCurrentD();

        // 添加更改提示
        View toolTipView = LayoutInflater.from(context).inflate(R.layout.mk_controller_tooltip, this, false);
        addView(toolTipView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        tvBrightnessVolume = findViewById(R.id.tv_brightness_volume);
    }

    // 将毫秒转成时间格式 00:00:00
    private String longToTime(long millisecond) {
        long tSeconds = millisecond / 1000;
        long tMinutes = tSeconds / 60;
        int cSeconds = (int) (tSeconds % 60);
        int cMinute = (int) (tMinutes % 60);
        int cHour = (int) (tMinutes / 60);
        String fSeconds = cSeconds >= 10 ? cSeconds + "" : "0" + cSeconds;
        String fMinute = cMinute >= 10 ? cMinute + "" : "0" + cMinute;
        String fHour = cHour >= 10 ? cHour + "" : "0" + cHour;
        return fHour + ":" + fMinute + ":" + fSeconds;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void register(MediaView mediaView) {
        this.mediaView = mediaView;
        if (mediaView != null) {
            ViewParent vParent = mediaView.getParent();
            if (vParent != null) {
                ViewGroup vGroup = (ViewGroup) vParent;
                int index = vGroup.indexOfChild(mediaView);
                vGroup.removeView(mediaView);
                addView(mediaView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                vGroup.addView(this, index);
            }
            mediaView.setOnTouchListener(new MKTouchListener());
            mediaView.setHolderCallBack(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    resetCurrentD();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    pause();
                }
            });
        }
    }

    @Override
    public void onPrepared() {
        if (mediaView != null) {
            updatePausePlay(mediaView.isPlaying());
            long duration = mediaView.getDuration();
            tvTotalD.setText(longToTime(duration));
            sbProgress.setMax((int) duration);

            // 开启计时器，每隔1s获取一次播放进度
            openUpdateUiHandler();
        }
    }

    @Override
    public void onCompletion() {
        updatePausePlay(false);
    }

    @Override
    public void onError() {
        updatePausePlay(false);
    }

    /**
     * 重置tvCurrentD
     */
    private void resetCurrentD() {
        tvCurrentD.setText("00:00:00");
        sbProgress.setProgress(0);
    }

    /**
     * 开启更新UI-Handler
     */
    private void openUpdateUiHandler() {
        if (mHandler == null) {
            mHandler = new ZDelayHandler(this);
        }
        Message message = Message.obtain();
        message.what = delayWhat;
        mHandler.sendMessageDelayed(message, 1000);
    }

    /**
     * 修改ivPausePlay
     */
    private void updatePausePlay(boolean isPlaying) {
        ivPausePlay.setImageResource(
                isPlaying ? R.drawable.icon_mk_pause_24 : R.drawable.icon_mk_play_arrow_24);
    }

    /**
     * 执行播放或暂停
     */
    private void pauseOrPlay() {
        if (mediaView != null) {
            if (mediaView.isPlaying()) {
                pause();
            } else {
                start();
            }
        }
    }

    @Override
    public void start() {
        if (mediaView != null) {
            mediaView.start();
            updatePausePlay(true);
            openUpdateUiHandler();
        }
    }

    @Override
    public void pause() {
        if (mediaView != null) {
            mediaView.pause();
            updatePausePlay(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_skip_previous) {
            if (onControllerListener != null && mediaView != null) {
                onControllerListener.onSkipPrevious(mediaView);
            }
        } else if (id == R.id.iv_fast_rewind) {
            if (mediaView != null) {
                mediaView.seekTo(mediaView.getCurrentPosition() - 5);
                if (onControllerListener != null) {
                    onControllerListener.onFastRewind();
                }
            }
        } else if (id == R.id.iv_pause_play) {
            pauseOrPlay();
            if (onControllerListener != null && mediaView != null) {
                onControllerListener.onPlay(mediaView.isPlaying());
            }
        } else if (id == R.id.iv_fast_forward) {
            if (mediaView != null) {
                mediaView.seekTo(mediaView.getCurrentPosition() + 5);
                if (onControllerListener != null) {
                    onControllerListener.onFastForward();
                }
            }
        } else if (id == R.id.iv_skip_next) {
            if (onControllerListener != null && mediaView != null) {
                onControllerListener.onSkipNext(mediaView);
            }
        } else if (id == R.id.iv_repeat) {
            if (mediaView != null) {
                boolean isLooping = !mediaView.isLooping();
                mediaView.setLooping(isLooping);
                ivRepeat.setImageResource(isLooping ? R.drawable.icon_mk_repeat_one_24 : R.drawable.icon_mk_repeat_24);
                if (onControllerListener != null) {
                    onControllerListener.onRepeat(isLooping);
                }
            }
        }
    }

    /**
     * 对外监听回调
     */
    public interface OnControllerListener {
        // 上一个
        void onSkipPrevious(MediaView mediaView);

        // 下一个
        void onSkipNext(MediaView mediaView);

        // 快退
        void onFastRewind();

        // 快进
        void onFastForward();

        // 循环
        void onRepeat(boolean isLooping);

        // 播放或暂停
        void onPlay(boolean isPlay);
    }

    private OnControllerListener onControllerListener;

    public MKController setOnControllerListener(OnControllerListener onControllerListener) {
        this.onControllerListener = onControllerListener;
        return this;
    }

    /**
     * 内部类-触摸事件监听
     */
    private class MKTouchListener implements View.OnTouchListener {
        private int mMaxVolume;
        private AudioManager mAudioManager;

        private float eventStartX = 0;
        private float eventStartY = 0;
        private int mediaViewWidth;
        private int mediaViewHeight;
        private int yLimitStart;
        private int yLimitEnd;
        private int volume;
        private int brightness;

        /**
         * 获取系统默认屏幕亮度值 屏幕亮度值范围（0-255）
         */
        private int getScreenBrightness() {
            ContentResolver contentResolver = getContext().getContentResolver();
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 125);
        }

        /**
         * 设置APP界面屏幕亮度值方法
         */
        private void setScreenBrightness(int brightnessValue) {
            Window window = ((Activity) getContext()).getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                if (brightnessValue >= 255) {
                    brightnessValue = 255;
                } else if (brightnessValue <= 0) {
                    brightnessValue = 1;
                }
                lp.screenBrightness = brightnessValue / 255.0f;
                window.setAttributes(lp);
            }
        }

        /**
         * 修改tvBrightnessVolume屏幕亮度
         */
        private void updateTvBrightness(int brightnessValue) {
            if (brightnessValue >= 255) {
                brightnessValue = 255;
            } else if (brightnessValue <= 0) {
                brightnessValue = 1;
            }
            int format = brightnessValue * 100 / 255;
            tvBrightnessVolume.setText("亮度：" + format + "%");
        }

        /**
         * 获取当前音量
         */
        private int getVolume() {
            initAudioManager();
            if (mAudioManager != null) {
                return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            }
            return 0;
        }

        /**
         * 设置音量
         *
         * @param volume 0-mMaxVolume
         */
        private void setVolume(int volume) {
            initAudioManager();
            if (mAudioManager != null && mMaxVolume > 0) {
                if (volume > mMaxVolume) {
                    volume = mMaxVolume;
                } else if (volume < 0) {
                    volume = 0;
                }
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }
        }

        /**
         * 初始化AudioManager
         */
        private void initAudioManager() {
            if (mAudioManager == null) {
                mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                if (mAudioManager != null) {
                    mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                }
            }
        }

        /**
         * 修改tvBrightnessVolume音量
         */
        private void updateTvVolume(int volume) {
            if (mMaxVolume > 0) {
                if (volume > mMaxVolume) {
                    volume = mMaxVolume;
                } else if (volume < 0) {
                    volume = 0;
                }
                int format = volume * 100 / mMaxVolume;
                tvBrightnessVolume.setText("音量：" + format + "%");
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: // 按下
                    eventStartX = event.getX();
                    eventStartY = event.getY();

                    mediaViewWidth = mediaView.getWidth();
                    mediaViewHeight = mediaView.getHeight();
                    int[] locations = new int[2];
                    mediaView.getLocationOnScreen(locations);
                    yLimitStart = locations[1];
                    yLimitEnd = yLimitStart + mediaViewHeight;

                    brightness = getScreenBrightness();
                    volume = getVolume();
                    return true;
                case MotionEvent.ACTION_MOVE: // 移动
                    float endX = event.getX();
                    float endY = event.getY();
                    if (endY >= yLimitStart && endY < yLimitEnd) {
                        float difY = endY - eventStartY;
                        float difX = endX - eventStartX;
                        if (Math.abs(difY) > Math.abs(difX)) {
                            tvBrightnessVolume.setVisibility(VISIBLE);
                            int width2 = mediaViewWidth / 2;
                            if (eventStartX > width2) { // 右侧 - 声音
                                int newVolume = (int) (volume - difY * mMaxVolume / mediaViewHeight);
                                setVolume(newVolume);
                                updateTvVolume(newVolume);
                            } else { // 左侧 - 亮度
                                int newBrightness = (int) (brightness - difY * 255 / mediaViewHeight);
                                setScreenBrightness(newBrightness);
                                updateTvBrightness(newBrightness);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL: // 取消
                case MotionEvent.ACTION_UP: // 抬起
                    tvBrightnessVolume.setVisibility(GONE);
                    break;
            }
            return false;
        }
    }
}
