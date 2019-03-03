package nia.chapter13;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Listing 13.8 LogEventMonitor
 * 消息监控
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class LogEventMonitor {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_BROADCAST, true)
            .handler( new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel)
                    throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    // 将LogEventDecoder和LogEventHandler添加到ChannelPipeline中
                    pipeline.addLast(new LogEventDecoder());
                    pipeline.addLast(new LogEventHandler());
                }
            } )
            .localAddress(address);
    }

    public Channel bind() {
        // 绑定Channel，注意DatagramChannel是无连接的
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException(
            "Usage: LogEventMonitor <port>");
        }
        // 构建一个LogEventMonitor
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(Integer.parseInt(args[0])));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running");
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
