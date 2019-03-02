package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Listing 10.8 Class ByteToCharDecoder
 * 将ByteBuf解码为Char类型
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
        List<Object> out) throws Exception {
        if (in.readableBytes() >= 2) {
            // 将一个或多个Character对象添加到传出的List中
            out.add(in.readChar());
        }
    }
}

