package nia.chapter6;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DummyChannelPipeline;
import io.netty.util.CharsetUtil;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * Created by kerr.
 *
 * Listing 6.6 Accessing the Channel from a ChannelHandlerContext
 *
 * Listing 6.7 Accessing the ChannelPipeline from a ChannelHandlerContext
 *
 * Listing 6.8 Calling ChannelHandlerContext write()
 */
public class WriteHandlers {
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;
    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DummyChannelPipeline.DUMMY_INSTANCE;

    /**
     * Listing 6.6 Accessing the Channel from a ChannelHandlerContext
     * 从ChannelHandlerContext访问Channel
     * */
    public static void writeViaChannel() {
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE; //get reference form somewhere
        // 获取到与ChannelHandlerContext相关联的Channel的引用
        Channel channel = ctx.channel();
        // 通过Channel写入缓冲区
        channel.write(Unpooled.copiedBuffer("Netty in Action",
                CharsetUtil.UTF_8));

    }

    /**
     * Listing 6.7 Accessing the ChannelPipeline from a ChannelHandlerContext
     * 通过ChannelHandlerContext访问ChannelPipeline
     * */
    public static void writeViaChannelPipeline() {
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE; //get reference form somewhere
        // h获取到与ChannelHandlerContext相关联的ChannelPipeline
        ChannelPipeline pipeline = ctx.pipeline(); //get reference form somewhere
        // 通过ChannelPipeline写入缓冲区
        pipeline.write(Unpooled.copiedBuffer("Netty in Action",
                CharsetUtil.UTF_8));

    }

    /**
     * Listing 6.8 Calling ChannelHandlerContext write()
     * 调用ChannelHandlerContext的write()方法
     * */
    public static void writeViaChannelHandlerContext() {
        // 获取到ChannelHandlerContext的引用
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE; //get reference form somewhere;
        // write()方法将把缓冲区数据发送到下一个ChannelHandler
        ctx.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
    }

}
