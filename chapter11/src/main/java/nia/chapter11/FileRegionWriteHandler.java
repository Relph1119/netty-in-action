package nia.chapter11;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by kerr.
 * <p>
 * Listing 11.11 Transferring file contents with FileRegion
 * 使用FileRegion传输文件的内容
 */
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final File FILE_FROM_SOMEWHERE = new File("");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        File file = FILE_FROM_SOMEWHERE; //get reference from somewhere
        Channel channel = CHANNEL_FROM_SOMEWHERE; //get reference from somewhere
        //...
        // 创建一个FileInputStream
        FileInputStream in = new FileInputStream(file);
        // 以文件长度创建一个FileRegion对象
        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());

        // 将region写入到channel中，并添加一个ChannelFutureListener监听器
        channel.writeAndFlush(region)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            // 处理失败异常
                            Throwable cause = future.cause();
                            // Do something
                        }
                    }
                });
    }
}
