package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Listing 9.3 AbsIntegerEncoder
 * 对整数取绝对值
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    // 继承MessageToMessageEncoder，将一个消息编码转换为另一种编码格式
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否有足够的字节用来编码，由于int是4个字节，故检查条件为4
        while (in.readableBytes() >= 4) {
            // 从输入的ByteBuf中读取一个整数，并且计算其绝对值
            int value = Math.abs(in.readInt());
            // 将该整数写入到编码消息的List中
            out.add(value);
        }
    }
}
