package com.baidu.aichinese.module.vplayer.view.controller;

import com.baidu.aichinese.module.vplayer.view.base.MediaView;

/**
 * @program: AIChinese
 * @description: 控制器接口
 * @author: zoufengli01
 * @create: 2021/12/10 2:58 下午
 **/
public interface IMKController {

    void register(MediaView mediaView);

    void onPrepared();

    void onCompletion();

    void onError();

    void start();

    void pause();

}
