package cn.ouyang.test.netty.nine.coder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NineServer {

    public static void main(String[] args) {
        new NineServer().bing(7397);
    }

    private void bing(int port){
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup,childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_RCVBUF,1)
                    .childHandler(new MyChannelInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("netty server has started,let`s enjoy!");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }

}
