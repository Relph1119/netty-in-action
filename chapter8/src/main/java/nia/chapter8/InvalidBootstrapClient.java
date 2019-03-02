package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 8.3 Incompatible Channel and EventLoopGroup
 * 不兼容的Channel和EventLoopGroup
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class InvalidBootstrapClient {

    public static void main(String args[]) {
        InvalidBootstrapClient client = new InvalidBootstrapClient();
        client.bootstrap();
    }

    /**
     * Listing 8.3 Incompatible Channel and EventLoopGroup
     */
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        // 创建一个Bootstrap类的实例，以创建和连接新的客户端Channel
        Bootstrap bootstrap = new Bootstrap();
        // 实现一个适用于NIO的EventLoopGroup实现
        bootstrap.group(group)
                // 指定一个适用于OIO的Channel实现类
                .channel(OioSocketChannel.class)
                // 设置一个用于处理Channel的I/O事件和数据的ChannelInboundHandler
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(
                            ChannelHandlerContext channelHandlerContext,
                            ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        // 尝试连接到远程节点
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }
}
