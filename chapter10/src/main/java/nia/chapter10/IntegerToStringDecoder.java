package nia.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Listing 10.3 Class IntegerToStringDecoder
 * 将Integer解码为String
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    public void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        // 将Integer消息转换为String，并将其添加到输出的List中
        out.add(String.valueOf(msg));
    }
}

