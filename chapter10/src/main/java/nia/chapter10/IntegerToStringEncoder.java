package nia.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Listing 10.6 Class IntegerToStringEncoder
 * 将Integer消息格式编码成String格式
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        // 将Integer转换为String，并将其添加到List中
        out.add(String.valueOf(msg));
    }
}

