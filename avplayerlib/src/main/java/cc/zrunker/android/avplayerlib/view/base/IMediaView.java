package cc.zrunker.android.avplayerlib.view.base;

import android.view.SurfaceHolder;

import cc.zrunker.android.avplayerlib.core.IMKPlayer;
import cc.zrunker.android.avplayerlib.core.listener.IMKListener;
import cc.zrunker.android.avplayerlib.view.controller.IMKController;

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
