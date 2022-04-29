package cn.ouyang.test.netty2.demo3.server;

import cn.ouyang.test.netty2.demo3.doamin.*;
import cn.ouyang.test.netty2.demo3.util.CacheUtil;
import cn.ouyang.test.netty2.demo3.util.FileUtil;
import cn.ouyang.test.netty2.demo3.util.MsgUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("---------链接报告开始---------");
        logger.info("链接报告信息：有一客户端链接到本服务端");
        logger.info("链接报告IP:{}", channel.localAddress().getHostString());
        logger.info("链接报告Port:{}", channel.localAddress().getPort());
        logger.info("---------链接报告完毕---------");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端断开连接{}",ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只允许传输文件
        if(!(msg instanceof FileTransferProtocol)){
            return;
        }

        FileTransferProtocol protocol = (FileTransferProtocol) msg;
        switch (protocol.getTransferType()){
            case 0: //desc
                FileDescInfo descInfo = (FileDescInfo) protocol.getTransferObj();

                String fileName = descInfo.getFileName();
                FileBurstInstruct fileBurstInstruct = CacheUtil.burstDataMap.get(fileName);
                if(null != fileBurstInstruct){
                    if(fileBurstInstruct.getStatus() == Constants.FileStatus.COMPLETE){
                        CacheUtil.burstDataMap.remove(fileName);
                    }

                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " netty服务端，接收客户端传输文件请求[断点续传]。" + JSON.toJSONString(fileBurstInstruct));
                    ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
                    return;
                }

                //发送消息
                FileTransferProtocol sendProtocol = MsgUtil.buildTransferInstruct(Constants.FileStatus.BEGIN, descInfo.getFileUrl(), 0);
                ctx.writeAndFlush(sendProtocol);
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " netty服务端，接收客户端传输文件请求。" + JSON.toJSONString(descInfo));
                break;
            case 2: //data
                FileBurstData burstData = (FileBurstData) protocol.getTransferObj();
                FileBurstInstruct burstInstruct = FileUtil.writeFile("D:\\", burstData);

                //保存断点续传
                CacheUtil.burstDataMap.put(burstData.getFileName(),burstInstruct);

                ctx.writeAndFlush(MsgUtil.buildTransferInstruct(burstInstruct));
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " netty服务端，接收客户端传输文件数据。" + JSON.toJSONString(burstData));

                //传输完成删除缓存
                if(burstInstruct.getStatus() == Constants.FileStatus.COMPLETE){
                    CacheUtil.burstDataMap.remove(burstData.getFileName());
                }
                break;
            default:
                break;

        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());
    }
}
