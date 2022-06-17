package cc.ibooker.zmediaplayer;

import android.os.Binder;

/**
 * 自定义Binder对象
 * Created by 邹峰立 on 2017/10/17.
 */

public class MediaPlayerBinder extends Binder {
    private MediaPlayerBinderService service;

    public MediaPlayerBinder(MediaPlayerBinderService service) {
        this.service = service;
    }

    public MediaPlayerBinderService getService() {
        return service;
    }

    public void setService(MediaPlayerBinderService service) {
        this.service = service;
    }
}
