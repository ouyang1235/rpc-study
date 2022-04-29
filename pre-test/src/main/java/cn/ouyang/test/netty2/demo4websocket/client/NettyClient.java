package cn.ouyang.test.netty2.demo4websocket.client;

import cn.ouyang.test.netty2.demo3.doamin.FileTransferProtocol;
import cn.ouyang.test.netty2.demo3.util.MsgUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;

public class NettyClient {
    public static void main(String[] args) {
        ChannelFuture connect = new NettyClient().connect("127.0.0.1", 7397);
        File file = new File("C:\\Users\\admin\\Desktop\\pdf.zip");
        FileTransferProtocol protocol = MsgUtil.buildRequestTransferFile(file.getAbsolutePath(), file.getName(), file.length());
        connect.channel().writeAndFlush(protocol);

    }

    EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    private ChannelFuture connect(String inetHost, int inetPort) {
        ChannelFuture channelFuture = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new MyChannelInitializer());
            channelFuture = b.connect(inetHost, inetPort).syncUninterruptibly();

            this.channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != channelFuture && channelFuture.isSuccess()) {
                System.out.println("itstack-demo-netty client start done. {关注公众号：bugstack虫洞栈，获取源码}");
            } else {
                System.out.println("itstack-demo-netty client start error. {关注公众号：bugstack虫洞栈，获取源码}");
            }
        }
        return channelFuture;
    }
}
