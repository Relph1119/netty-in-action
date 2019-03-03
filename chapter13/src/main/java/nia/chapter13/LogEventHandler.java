package nia.chapter13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Listing 13.7 LogEventHandler
 * LogEvent处理类
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当异常发生时，打印堆栈信息，并关闭对应的Channel
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        // 创建StringBuilder，并构建输出的字符串
        StringBuilder builder = new StringBuilder();
        builder.append(event.getReceivedTimestamp());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());
        // 打印LogEvent的数据
        System.out.println(builder.toString());
    }
}
