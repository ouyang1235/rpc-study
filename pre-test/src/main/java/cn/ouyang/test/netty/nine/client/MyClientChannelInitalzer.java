package cn.ouyang.test.netty.nine.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class MyClientChannelInitalzer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //基于换行符号
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //解码转String,注意调整自己的编码格式GBK、UTF-8
        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        //编码转String
        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        //自己的处理器
        ch.pipeline().addLast(new MyClientOutHandler());
        ch.pipeline().addLast(new MyClientHandler());
    }
}
