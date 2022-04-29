package cn.ouyang.test.netty2.demo3.coder;

import cn.ouyang.test.netty2.demo3.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ObjEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public ObjEncoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(genericClass.isInstance(msg)){
            byte[] data = SerializationUtil.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
