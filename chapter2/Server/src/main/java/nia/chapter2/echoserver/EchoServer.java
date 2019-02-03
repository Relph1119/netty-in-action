package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)
        throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() +
                " <port>"
            );
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 1.创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 2.创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 3.指定所使用的NIO传输Channel
                .channel(NioServerSocketChannel.class)
                    // 4.使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))
                    // 5.添加一个EchoServerHandler到子Channel的ChannelPipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        // 由于EchoServerHandler被标注为@Shareable，所以可以线程安全地使用同一个实例
                        ch.pipeline().addLast(serverHandler);
                    }
                });

            // 6.异步地绑定服务器，调用sync()方法阻塞等待，直到绑定完成
            ChannelFuture f = b.bind().sync();
            System.out.println(EchoServer.class.getName() +
                " started and listening for connections on " + f.channel().localAddress());
            // 7.获取Channel的ColseFuture，并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 8.关闭EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
