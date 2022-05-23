package cn.ouyang.test.netty2.demo4websocket.web;

import cn.ouyang.test.netty2.demo4websocket.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@SpringBootApplication
@ComponentScan("cn.ouyang.test.netty2.demo4websocket")
public class NettyApplication implements CommandLineRunner {

    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    @Resource
    private NettyServer nettyServerWebSocket;


    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class,args);
    }


    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        ChannelFuture channelFuture = nettyServerWebSocket.bing(inetSocketAddress);
        Runtime.getRuntime().addShutdownHook(new Thread(()->nettyServerWebSocket.destroy()));
        channelFuture.channel().closeFuture().syncUninterruptibly();

    }
}
