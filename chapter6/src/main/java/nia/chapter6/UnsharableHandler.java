package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Listing 6.11 Invalid usage of @Sharable
 * 注解@Sharable错误用法
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
// 使用注解@Sharable标注
@Sharable
public class UnsharableHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 将count字段的值加1
        count++;
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        // 记录方法调用，并转发给下一个ChannelHandler
        ctx.fireChannelRead(msg);
    }
}

