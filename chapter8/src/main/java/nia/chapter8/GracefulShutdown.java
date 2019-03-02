package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * Listing 8.9 Graceful shutdown
 * 优雅关闭应用程序
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class GracefulShutdown {
    public static void main(String args[]) {
        GracefulShutdown client = new GracefulShutdown();
        client.bootstrap();
    }

    /**
     * Listing 8.9 Graceful shutdown
     */
    public void bootstrap() {
        // 创建处理I/O的EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        // 创建并配置一个Bootstrap类的实例
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
             .channel(NioSocketChannel.class)
        //...
             .handler(
                new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(
                            ChannelHandlerContext channelHandlerContext,
                            ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                }
             );
        bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).syncUninterruptibly();
        //,,,
        // shutdownGracefully()方法将释放所有资源，并关闭所有当前正在使用中的Channel
        Future<?> future = group.shutdownGracefully();
        // block until the group has shutdown
        future.syncUninterruptibly();
    }
}
