package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Listing 9.1 FixedLengthFrameDecoder
 * 给定足够的数据，产生固定大小的帧，如果没有读取足够的数据，将等待下一个数据块，并将再次检查是否足够产生一个新的帧
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    //继承ByteToMessageDecoder，用于处理入站字节，并将字节解码为消息

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException(
                    "frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否有足够的字节可以被读取，用于生成下一个帧
        while (in.readableBytes() >= frameLength) {
            // 从ByteBuf中读取一个新帧
            ByteBuf buf = in.readBytes(frameLength);
            // 将该帧添加到已被解码的消息列表中
            out.add(buf);
        }
    }
}
