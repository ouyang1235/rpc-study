package cn.ouyang.test.netty.nine.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {

    private final int BASE_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if(in.readableBytes() < BASE_LENGTH){
            return;
        }

        int beginIdx;

        while(true){
            beginIdx = in.readerIndex();
            in.markReaderIndex();

            if(in.readByte() == 0x02){
                break;
            }

            in.resetReaderIndex();
            in.readByte();

            if(in.readableBytes() < BASE_LENGTH){
                return;
            }

        }

        int readableCount = in.readableBytes();
        if(readableCount <= 1){
            in.readerIndex(beginIdx);
            return;
        }

        //读取标记头
        ByteBuf byteBuf = in.readBytes(1);
        String msgLengthStr = byteBuf.toString(Charset.forName("GBK"));
        int msgLength = Integer.parseInt(msgLengthStr);

        readableCount = in.readableBytes();
        if(readableCount < msgLength + 1){
            in.readerIndex(beginIdx);
            return;
        }

        ByteBuf msgContent = in.readBytes(msgLength);

        //如果没有结尾标识，还原指针位置[其他标识结尾]
        byte end = in.readByte();
        if (end != 0x03) {
            in.readerIndex(beginIdx);
            return;
        }

        out.add(msgContent.toString(Charset.forName("GBK")));

    }


}
