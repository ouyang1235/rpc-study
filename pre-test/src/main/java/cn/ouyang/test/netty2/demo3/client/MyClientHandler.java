package cn.ouyang.test.netty2.demo3.client;

import cn.ouyang.test.netty2.demo3.doamin.Constants;
import cn.ouyang.test.netty2.demo3.doamin.FileBurstData;
import cn.ouyang.test.netty2.demo3.doamin.FileBurstInstruct;
import cn.ouyang.test.netty2.demo3.doamin.FileTransferProtocol;
import cn.ouyang.test.netty2.demo3.util.FileUtil;
import cn.ouyang.test.netty2.demo3.util.MsgUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("----------链接报告开始----------");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("----------链接报告完毕----------");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接" + ctx.channel().localAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof FileTransferProtocol)){
            return;
        }
        FileTransferProtocol protocol = (FileTransferProtocol) msg;
        Object transferObj = protocol.getTransferObj();
        switch (protocol.getTransferType()){
            case 1:
                FileBurstInstruct instruct = (FileBurstInstruct) transferObj;
                //Constants.FileStatus ｛0开始、1中间、2结尾、3完成｝
                if (Constants.FileStatus.COMPLETE == instruct.getStatus()) {
                    ctx.flush();
                    ctx.close();
                    System.exit(-1);
                    return;
                }
                FileBurstData fileBurstData = FileUtil.readFile(instruct.getClientFileUrl(), instruct.getReadPosition());
                ctx.writeAndFlush(MsgUtil.buildTransferData(fileBurstData));
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " bugstack虫洞栈客户端传输文件信息。 FILE：" + fileBurstData.getFileName() + " SIZE(byte)：" + (fileBurstData.getEndPos() - fileBurstData.getBeginPos()));
                break;
            default:
                break;
        }
        /**模拟传输过程中断，场景测试可以注释掉
         *
         */
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " bugstack虫洞栈客户端传输文件信息[主动断开链接，模拟断点续传]");
//        ctx.flush();
//        ctx.close();
//        System.exit(-1);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
