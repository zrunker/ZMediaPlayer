package com.baidu.aichinese.module.vplayer.view.base;

import android.view.SurfaceHolder;

import com.baidu.aichinese.module.vplayer.core.IMKPlayer;
import com.baidu.aichinese.module.vplayer.core.listener.IMKListener;
import com.baidu.aichinese.module.vplayer.view.controller.IMKController;

/**
 * @program: AIChinese
 * @description: 媒体View接口
 * @author: zoufengli01
 * @create: 2021/12/6 3:08 下午
 **/
public interface IMediaView extends IMKPlayer, IMKListener {

    void setHolderCallBack(SurfaceHolder.Callback holderCallBack);

    void bindController(IMKController controller);
}
