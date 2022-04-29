package cn.ouyang.test.netty2.demo4websocket.server;

import cn.ouyang.test.netty2.demo3.doamin.*;
import cn.ouyang.test.netty2.demo3.util.CacheUtil;
import cn.ouyang.test.netty2.demo3.util.FileUtil;
import cn.ouyang.test.netty2.demo4websocket.domain.ClientMsgProtocol;
import cn.ouyang.test.netty2.demo4websocket.util.ChannelHandler;
import cn.ouyang.test.netty2.demo4websocket.util.MsgUtil;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    private WebSocketServerHandshaker handshaker;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        logger.info("---------链接报告开始---------");
        logger.info("链接报告信息：有一客户端链接到本服务端,客户端信息:IP-{},Port-{}",channel.remoteAddress().getHostString(),channel.remoteAddress().getPort());
        logger.info("---------链接报告完毕---------");
        ChannelHandler.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端断开连接{}",ctx.channel().remoteAddress().toString());
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http
        if(msg instanceof FullHttpRequest){

            FullHttpRequest httpRequest = (FullHttpRequest) msg;

            if(!httpRequest.decoderResult().isSuccess()){
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);

                //返回应答给客户端
                if(httpResponse.status().code() != 200){
                    ByteBuf buf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(buf);
                    buf.release();
                }

                //如果是非Keep-Alive，关闭连接
                ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);
                if(httpResponse.status().code() != 200){
                    f.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }

            WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            handshaker = factory.newHandshaker(httpRequest);

            if(null == handshaker){
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            }else{
                handshaker.handshake(ctx.channel(),httpRequest);
            }

            return;
        }

        if(msg instanceof WebSocketFrame){
            WebSocketFrame webSocketFrame = (WebSocketFrame) msg;

            //关闭请求
            if(webSocketFrame instanceof CloseWebSocketFrame){
                handshaker.close(ctx.channel(),(CloseWebSocketFrame) webSocketFrame.retain());
                return;
            }

            //ping请求
            if(webSocketFrame instanceof PingWebSocketFrame){
                ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
                return;
            }

            //只支持文本格式，不支持二进制消息
            if(!(webSocketFrame instanceof TextWebSocketFrame)){
                throw  new Exception("仅支持文本格式");
            }

            String request = ((TextWebSocketFrame) webSocketFrame).text();
            System.out.println("服务器收到:" + request);

            ClientMsgProtocol clientMsgProtocol = JSON.parseObject(request, ClientMsgProtocol.class);
            //1.请求个人信息
            if(1 == clientMsgProtocol.getType()){
                ctx.channel().writeAndFlush(MsgUtil.buildMsgOwner(ctx.channel().id().toString()));
                return;
            }

            //群发消息
            if (2 == clientMsgProtocol.getType()) {
                TextWebSocketFrame textWebSocketFrame = MsgUtil.buildMsgAll(ctx.channel().id().toString(), clientMsgProtocol.getMsgInfo());
                ChannelHandler.channelGroup.writeAndFlush(textWebSocketFrame);
            }



        }



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());
    }
}
