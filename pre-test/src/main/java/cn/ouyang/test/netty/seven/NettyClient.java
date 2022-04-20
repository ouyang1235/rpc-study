package cn.ouyang.test.netty.seven;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1",7398);
    }

    private void connect(String host,int port){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.AUTO_READ,true)
                    .handler(new MyClientChannelInitalzer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("this client start done");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }

    }
}
