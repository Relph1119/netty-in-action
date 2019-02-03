package nia.chapter2.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Listing 2.1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
// Sharable注解：标记一个ChannelHandler可以被多个Channel安全地共享
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    // 对于每个传入的消息都要调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        // 将消息记录到控制台
        System.out.println(
                "Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者，而不 flush 出站消息
        ctx.write(in);
    }

    // 通知ChannelHandlerContext最后一次对channelRead()的调用是当前批量读取中的最后一条消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        /*
         * 将目前暂存于ChannelOutboundBuffer中的消息(在下一次调用flush()或writeAndFlush方法时，
         * 将会尝试写出到套接字) flush 到远程节点，并关闭该Channel
         */
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    // 在读取操作期间，异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        // 打印异常栈跟踪
        cause.printStackTrace();
        // 关闭该Channel
        ctx.close();
    }
}
