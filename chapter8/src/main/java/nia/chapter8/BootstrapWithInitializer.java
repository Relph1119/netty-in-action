package nia.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * Listing 8.6 Bootstrapping and using ChannelInitializer
 * 引导和使用ChannelInitializer
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class BootstrapWithInitializer {

    /**
     * Listing 8.6 Bootstrapping and using ChannelInitializer
     */
    public void bootstrap() throws InterruptedException {
        // 创建ServerBootstrap，用于创建和绑定新的Channel
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置EventLoopGroup，将其提供用以处理Channel事件的EventLoop
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                //指定Channel实现
                .channel(NioServerSocketChannel.class)
                //注册一个ChannelInitializerImpl的实例来设置ChannelPipeline
                .childHandler(new ChannelInitializerImpl());
        //绑定到地址
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.sync();
    }

    // 用于设置ChannelPipelind的自定义ChannelInitializerImpl实现
    final class ChannelInitializerImpl extends ChannelInitializer<Channel> {

        // 将所需的ChannelHandler添加到ChannelPipeline
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));

        }
    }
}
