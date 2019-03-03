package nia.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Listing 13.2 LogEventEncoder
 * 消息编码器
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    // 创建即将被发送到指定InetSocketAddress的DatagramPacket消息
    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc()
            .buffer(file.length + msg.length + 1);
        // 将文件名写入ByteBuf中
        buf.writeBytes(file);
        // 添加分隔符
        buf.writeByte(LogEvent.SEPARATOR);
        // 将日志消息写入ByteBuf中
        buf.writeBytes(msg);
        // 将一个拥有数据和目的地址的DatagramPacket添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
