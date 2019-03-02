package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Listing 10.1 Class ToIntegerDecoder extends ByteToMessageDecoder
 * 每次从入站ByteBuf中读取4个字节，将其解码为一个int，然后将它添加到一个List中
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    // 继承ByteToMessageDecoder类，用于将字节编码为特定格式

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否有足够的4个字节可读取
        if (in.readableBytes() >= 4) {
            // 从入站ByteBuf中读取一个int，并将其添加到解码消息的List中
            out.add(in.readInt());
        }
    }
}

