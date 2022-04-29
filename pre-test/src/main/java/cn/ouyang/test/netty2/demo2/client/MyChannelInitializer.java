package cn.ouyang.test.netty2.demo2.client;

import cn.ouyang.test.netty2.demo2.coder.ObjDecoder;
import cn.ouyang.test.netty2.demo2.coder.ObjEncoder;
import cn.ouyang.test.netty2.demo2.doamin.MsgInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new ObjDecoder(MsgInfo.class))
                .addLast(new ObjEncoder(MsgInfo.class))
                .addLast(new MyClientHandler());
    }
}
