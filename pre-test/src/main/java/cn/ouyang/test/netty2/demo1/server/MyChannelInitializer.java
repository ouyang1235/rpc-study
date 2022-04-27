package cn.ouyang.test.netty2.demo1.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                //基于换行符
                .addLast(new LineBasedFrameDecoder(1024))
                //解码转string
                .addLast(new StringDecoder(Charset.forName("GBK")))
                //string转编码
                .addLast(new StringEncoder(Charset.forName("GBK")))
                //接收实现
                .addLast(new MyServerHandler());
    }
}
