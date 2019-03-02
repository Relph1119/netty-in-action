package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import nia.chapter9.FixedLengthFrameDecoder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Listing 9.2 Testing the FixedLengthFrameDecoder
 * 测试FixedLengthFrameDecoder
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class FixedLengthFrameDecoderTest {
    // 使用@Test注解
    @Test
    public void testFramesDecoded() {
        // 创建一个ByteBuf，并初始化9字节
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        // 创建一个EmbeddedChannel，并添加一个FixedLengthFrameDecoder，传入3字节的帧长用于测试
        EmbeddedChannel channel = new EmbeddedChannel(
            new FixedLengthFrameDecoder(3));
        // write bytes
        // 将数据写入EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));
        // 标记Channel为已完成状态
        assertTrue(channel.finish());

        // read messages
        // 读取所生成的消息，并验证ByteBuf是否按照每3帧来分段的
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(
            new FixedLengthFrameDecoder(3));
        // 先写入2个字节数据，由于不足3个，不能通过readInbound读取数据，返回false
        assertFalse(channel.writeInbound(input.readBytes(2)));
        // 再写入7个字节数据，由于大于3字节，能通过readInbound读取数据，返回true
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }
}
