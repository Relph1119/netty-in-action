package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Listing 8.8 Using Bootstrap with DatagramChannel
 * 使用Bootstrap和DatagramChannel
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapDatagramChannel {

    /**
     * Listing 8.8 Using Bootstrap with DatagramChannel
     */
    public void bootstrap() {
        // 创建一个Bootstrap的实例，用于创建和绑定新的数据包Channel
        Bootstrap bootstrap = new Bootstrap();
        // 设置EventLoopGroup，它提供了用于处理Channel事件的EventLoop
        bootstrap.group(new OioEventLoopGroup())
                // 指定Channel实现
                .channel(OioDatagramChannel.class)
                // 设置用于处理Channel的I/O和数据的ChannelInboundHandler
                .handler(
                        new SimpleChannelInboundHandler<DatagramPacket>() {
                            @Override
                            public void channelRead0(ChannelHandlerContext ctx,
                                                     DatagramPacket msg) throws Exception {
                                // Do something with the packet
                            }
                        }
                );
        // 由于协议是无连接的，所以只调用bind()方法
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Channel bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
