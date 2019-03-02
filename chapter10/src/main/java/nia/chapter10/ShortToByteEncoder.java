package nia.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Listing 10.5 Class ShortToByteEncoder
 * 接收一个Short类型的实例作为消息，将它编码为ByteBuf的Short类型值
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    @Override
    public void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        // 将Short写入到编码的ByteBuf中
        out.writeShort(msg);
    }
}
