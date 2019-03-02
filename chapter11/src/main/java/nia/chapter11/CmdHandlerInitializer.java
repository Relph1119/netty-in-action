package nia.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Listing 11.9 Using a ChannelInitializer as a decoder installer
 * 使用自定义解码器
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    // 每个帧中的分隔符
    private static final byte SPACE = (byte) ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加CmdDecoder解码器用于解码Cmd对象
        pipeline.addLast(new CmdDecoder(64 * 1024));
        // 添加CmdHandler，用于处理和接收Cmd对象
        pipeline.addLast(new CmdHandler());
    }

    // Cmd对象
    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf args;

        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf name() {
            return name;
        }

        public ByteBuf args() {
            return args;
        }
    }

    // Cmd对象解码器，继承分隔符通用解码器
    public static final class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            // 从ByteBuf中获得由换行符分隔的帧
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            // 如果帧为null，返回null
            if (frame == null) {
                return null;
            }
            // 找到第一个空格字符的索引，前面是命令名称，后面是参数
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            // 根据上述规则构造Cmd对象
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // Do something with the command
        }
    }
}
