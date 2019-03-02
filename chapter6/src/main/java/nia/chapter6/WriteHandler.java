package nia.chapter6;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Listing 6.9 Caching a ChannelHandlerContext
 * 缓存到ChannelHandlerContext的引用
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class WriteHandler extends ChannelHandlerAdapter {
    private ChannelHandlerContext ctx;
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 存储到ChannelhandlerContext的引用以供稍后使用
        this.ctx = ctx;
    }

    // 使用之前存储到ChannelHandlerContext的引用来发送消息
    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }
}
