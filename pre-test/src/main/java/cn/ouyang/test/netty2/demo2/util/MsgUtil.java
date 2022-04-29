package cn.ouyang.test.netty2.demo2.util;

import cn.ouyang.test.netty2.demo2.doamin.MsgInfo;

public class MsgUtil {

    public static MsgInfo buildMsg(String channelId,String msgContent){
        return new MsgInfo(channelId,msgContent);
    }

}
