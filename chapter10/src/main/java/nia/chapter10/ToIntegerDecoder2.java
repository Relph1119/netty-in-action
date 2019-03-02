package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Listing 10.2 Class ToIntegerDecoder2 extends ReplayingDecoder
 * 采用ReplayingDecoder来实现编码功能
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    // 继承ReplayingDecoder，用于将字节编码为消息

    // 传入的ByteBuf是ReplayingDecodeByteBuf
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 从入站ByteBuf中读取一个int，并将其添加到解码消息的List中
        out.add(in.readInt());
    }
}

