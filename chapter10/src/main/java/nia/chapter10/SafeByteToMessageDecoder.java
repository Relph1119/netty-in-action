package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Listing 10.4 TooLongFrameException
 * 使用TooLongFrameException异常，来通知ChannelPipeline中的其他ChannelHandler发生了帧大小溢出
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */

public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int readable = in.readableBytes();
            // 检查缓冲区中的数据是否超过MAX_FRAME_SIZE个字节
            if (readable > MAX_FRAME_SIZE) {
                // 如果超过，丢弃数据，并抛出异常
                in.skipBytes(readable);
                throw new TooLongFrameException("Frame too big!");
        }
        // do something
        // ...
    }
}
