package cc.ibooker.maoker.vplayer.view.base;

import android.view.SurfaceHolder;

import cc.ibooker.maoker.vplayer.core.IMKPlayer;
import cc.ibooker.maoker.vplayer.core.listener.IMKListener;
import cc.ibooker.maoker.vplayer.view.controller.IMKController;

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
