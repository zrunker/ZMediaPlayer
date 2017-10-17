package cc.ibooker.zmediaplayer;

import android.os.Binder;

/**
 * 自定义Binder对象
 * Created by 邹峰立 on 2017/10/17.
 */

public class MediaplayerBinder extends Binder {
    private MediaplayerBinderService service;

    public  MediaplayerBinder(MediaplayerBinderService service) {
        this.service = service;
    }

    public MediaplayerBinderService getService() {
        return service;
    }

    public void setService(MediaplayerBinderService service) {
        this.service = service;
    }
}
