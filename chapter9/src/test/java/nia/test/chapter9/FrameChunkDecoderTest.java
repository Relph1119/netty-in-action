package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import nia.chapter9.FrameChunkDecoder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Listing 9.6 Testing FrameChunkDecoder
 *  测试FixedLengthFrameDecoder
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */

public class FrameChunkDecoderTest {
    @Test
    public void testFramesDecoded() {
        // 创建一个ByteBuf，初始化9个字节数据
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        // 创建一个最大允许大小为3帧的FrameChunkDecoder
        EmbeddedChannel channel = new EmbeddedChannel(
            new FrameChunkDecoder(3));

        // 写入2个字节，由于小于3，可以readInbound()，返回true
        assertTrue(channel.writeInbound(input.readBytes(2)));
        try {
            // 写入4个字节，由于大于3，会抛出异常，测试失败
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            // expected exception
        }

        // 写入剩下的3个字节，由于等于3，返回true
        assertTrue(channel.writeInbound(input.readBytes(3)));
        assertTrue(channel.finish());

        // Read frames
        // 读取产生的消息，并验证消息长度的正确性，由于总共写入5个字节
        ByteBuf read = (ByteBuf) channel.readInbound();
        // read 读取了第一次的2个字节，测试成功
        assertEquals(buf.readSlice(2), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        // read 读取了3个字节，由于丢失了4个字节，所以与buf的后3个字节一样，测试成功
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();
        buf.release();
    }
}
