package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by kerr.
 *
 * Listing 1.3 Asynchronous connect
 *
 * Listing 1.4 Callback in action
 */
public class ConnectExample {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Listing 1.3 Asynchronous connect
     *
     * Listing 1.4 Callback in action
     * */
    public static void connect() {
        Channel channel = CHANNEL_FROM_SOMEWHERE; //reference form somewhere
        // 异步连接到远程节点
        // Does not block
        ChannelFuture future = channel.connect(
                new InetSocketAddress("192.168.0.1", 25));
        // 注册一个ChannelFutureListener，以便在操作完成时获得通知
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    // 如果操作成功，则创建一个ByteBuf以持有数据
                    ByteBuf buffer = Unpooled.copiedBuffer(
                            "Hello", Charset.defaultCharset());
                    // 将数据异步发送到远程节点。返回一个ChannelFuture
                    ChannelFuture wf = future.channel()
                            .writeAndFlush(buffer);
                    // ...
                } else {
                    // 如果发生错误，则访问描述原因的Throwable
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}