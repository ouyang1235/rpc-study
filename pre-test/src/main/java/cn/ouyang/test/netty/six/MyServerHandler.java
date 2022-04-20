package cn.ouyang.test.netty.six;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\r\n[client msg] " + msg);
        String str = "服务端收到:" + new Date() + " " +msg + "\r\n";
//        ByteBuf buffer = Unpooled.buffer(str.getBytes().length);
//        buffer.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(str);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        //加入群组
        ChannelHandler.channelGroup.add(channel);

        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.remoteAddress().getHostString());
        System.out.println("链接报告Port:" + channel.remoteAddress().getPort());
        System.out.println("链接报告channelID:" + channel.id());
        System.out.println("链接报告完毕");
        //群发通知客户端链接建立成功
        String str = "一个新的客户端链接建立成功" + " [" + new Date() + "] " + channel.remoteAddress().getHostString() + "\r\n";
//        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//        buf.writeBytes(str.getBytes("GBK"));
        ChannelHandler.channelGroup.writeAndFlush(str);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开连接" + ctx.channel().localAddress().toString());
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息: \r\n" + cause.getMessage());
    }
}
