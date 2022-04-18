package cc.ibooker.maoker.vplayer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cc.ibooker.maoker.vplayer.R;
import cc.ibooker.maoker.vplayer.core.listener.MKErrorListener;
import cc.ibooker.maoker.vplayer.utils.ErrorTrans;
import cc.ibooker.maoker.vplayer.view.base.MediaView;
import cc.ibooker.maoker.vplayer.view.controller.MKController;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: AIChinese
 * @description: 视频View
 * @author: zoufengli01
 * @create: 2021/12/6 11:05 上午
 **/
public class MKVideoView extends FrameLayout {
    private final MediaView mediaView;
    private final ProgressBar progressCircular;
    private MKController defaultController;

    public MKVideoView(Context context) {
        this(context, null);
    }

    public MKVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.MKVideoView);
        boolean isBindController = typedArr.getBoolean(R.styleable.MKVideoView_isBindController, true);
        typedArr.recycle();

        LayoutInflater.from(context).inflate(R.layout.mk_video_view, this, true);
        progressCircular = findViewById(R.id.progress_circular);
        mediaView = findViewById(R.id.media_view);
        if (isBindController) {
            defaultController = new MKController(context);
            mediaView.bindController(defaultController);
        }
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        // 视频预备监听
        setOnPreparedListener(null);
        // 视频错误异常监听
        setOnErrorListener(null);
        // 视频播放完成
        setOnCompletionListener(null);
    }

    /**
     * 设置控制器监听
     */
    public void setControllerListener(MKController.OnControllerListener onControllerListener) {
        if (onControllerListener != null && defaultController != null) {
            defaultController.setOnControllerListener(onControllerListener);
        }
    }

    /**
     * 播放视频
     *
     * @param path 视频地址
     */
    public void play(String path) {
        progressCircular.setVisibility(VISIBLE);
        mediaView.prepareAsync(path);
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener listener) {
        mediaView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onPrepared(iMediaPlayer);
                }
            }
        });
    }

    public void setOnErrorListener(MKErrorListener listener) {
        mediaView.setOnErrorListener(new MKErrorListener() {
            @Override
            public void onError(IMediaPlayer iMediaPlayer, int what, int extra, String error) {
                Log.d("MKPlayerZZ onError ", "what = " + what + " extra = " + extra);
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onError(iMediaPlayer, what, extra, ErrorTrans.trans(what));
                }
            }
        });
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener listener) {
        mediaView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onCompletion(iMediaPlayer);
                }
            }
        });
    }
}
