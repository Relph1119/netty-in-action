package nia.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 8.4 Bootstrapping a server
 * 引导服务器
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapServer {

    /**
     * Listing 8.4 Bootstrapping a server
     */
    public void bootstrap() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 创建服务器启动示例
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置EventLoopGroup，其提供了用于处理Channel事件的EventLoop
        bootstrap.group(group)
                // 指定要使用的Channel实现
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子Channel的I/O及数据的ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                                ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        // 通过配置好的ServerBootstrap的实例绑定该Channel
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
