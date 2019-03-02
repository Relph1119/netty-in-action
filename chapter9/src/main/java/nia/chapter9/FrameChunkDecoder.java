package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Listing 9.5 FrameChunkDecoder
 * 检查数据是否过量，并抛出异常
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {
    // 集成ByteToMessageDecoder，用于将入站字节解码为消息

    private final int maxFrameSize;

    // 指定要产生的帧的最大允许大小
    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        if (readableBytes > maxFrameSize) {
            // discard the bytes
            // 如果帧太大，就丢弃它并抛出一个TooLongFrameException异常
            in.clear();
            throw new TooLongFrameException();
        }
        // 否则，从ByteBuf中读取一个新帧
        ByteBuf buf = in.readBytes(readableBytes);
        // 将新帧添加到解码消息的List中
        out.add(buf);
    }
}
