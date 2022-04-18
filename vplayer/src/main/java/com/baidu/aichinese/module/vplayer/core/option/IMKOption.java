package cc.ibooker.maoker.vplayer.core.option;

/**
 * @program: AIChinese
 * @description: 配置信息接口
 * @author: zoufengli01
 * @create: 2021/12/9 3:54 下午
 **/
public interface IMKOption {

    /**
     * 是否开启硬解码
     */
    void isMediacodec(boolean bool);

    /**
     * 是否开启自动旋转
     */
    void isMediacodecAutoRotate(boolean bool);

    /**
     * 是否处理分辨率变化
     */
    void isMediacodecHandleResolutionChange(boolean bool);

    /**
     * 设置播放前的最大探测时间
     */
    void analyzeMaxDuration(long value);

    /**
     * 设置播放前的探测时间，1 - 达到首屏秒开效果
     */
    void analyzeDuration(long value);

    /**
     * 播放前的探测Size，默认是1M, 改小一点会出画面更快
     */
    void probeSize(long value);

    /**
     * 每处理一个packet之后刷新io上下文
     */
    void flushPackets(long value);

    /**
     * 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
     */
    void isPacketBuffering(boolean bool);

    /**
     * 跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
     */
    void frameDrop(long value);

    /**
     * SeekTo设置优化
     */
    void isEnableAccurateSeek(boolean bool);

    /**
     * 最大fps，例如：value = 30
     */
    void maxFps(long value);

    /**
     * 最大缓冲大小,单位kb 例如：value = 102400L
     */
    void maxBufferSize(long value);

    /**
     * 是否开启预处理完成，自动播放
     */
    void isStartOnPrepared(boolean bool);

    /**
     * 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
     */
    void isSkipLoopFilter(boolean bool);

    /**
     * 默认最小帧数
     */
    void minFrames(long value);

    /**
     * 是否开启OpenSL ES - 加速标准
     */
    void openSlEs(boolean bool);

    /**
     * 覆盖格式，例如：value = IjkMediaPlayer.SDL_FCC_RV32
     */
    void overlayFormat(int value);

    /**
     * 域名检测
     */
    void isHttpDetectRangeSupport(boolean bool);

    /**
     * 设置flags，如：nobuffer、fastseek
     * PS: fastseek - 解决m3u8文件拖动问题 比如:一个3个多少小时的音频文件，开始播放几秒中，然后拖动到2小时左右的时间，要loading 10分钟
     */
    void fflags(String value);

    /**
     * 如果是rtsp协议，可以优先用tcp(默认是用udp)
     *
     * @param value "tcp"或者"udp"
     */
    void rtspTransport(String value);

    /**
     * 不额外优化（使能非规范兼容优化，默认值0 ）
     */
    void fast(boolean bool);

    /**
     * 最大缓存时长
     */
    void axCachedDuration(long value);

    /**
     * 是否限制输入缓存数
     */
    void infBuf(boolean bool);

    /**
     * 播放重连次数
     */
    void reConnect(long value);

    /**
     * 因为项目中多次调用播放器，有网络视频，resp，本地视频，还有wifi上http视频，所以得清空DNS才能播放WIFI上的视频
     * PS: 如果项目无法播放远程视频,可以试试这句话
     * Server returned 4XX Client Error, but not one of 40{0,1,3,4}报这个错误也可以试试
     */
    void dnsCacheClear(boolean bool);

    /**
     * 跳过帧
     */
    void skipFrame(long value);

    /**
     * 超时时间，timeout参数只对http设置有效，若果你用rtmp设置timeout，ijkplayer内部会忽略timeout参数。
     * rtmp的timeout参数含义和http的不一样。例如：value = 10000000
     */
    void timeout(long value);

    /**
     * 是否开启变调
     */
    void soundTouch(boolean bool);

    /**
     * 支持Mpeg4
     */
    void mediacodecMpeg4(boolean bool);
}
