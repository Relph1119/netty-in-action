package nia.test.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import nia.chapter9.AbsIntegerEncoder;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Listing 9.4 Testing the AbsIntegerEncoder
 * 测试AbsIntegerEncoder
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded() {
        // 创建一个ByteBuf，并初始化9个负数
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        // 创建一个EmbeddChann，添加一个AbsIntegerEncoder编码器
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        // 写入ByteBuf，并测试readOutbound()方法是否能返回数据
        assertTrue(channel.writeOutbound(buf));
        // 判断channel是否为已完成状态
        assertTrue(channel.finish());

        // read bytes
        // 读取所产生的消息，并测试负整数对应的绝对值
        for (int i = 1; i < 10; i++) {
            assertEquals(i, channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}
