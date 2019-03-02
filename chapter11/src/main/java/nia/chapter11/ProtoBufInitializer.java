package nia.chapter11;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * Listing 11.14 Using protobuf
 * 使用protobuf
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加ProtobufVarint32FrameDecoder解码器，根据消息中的Google Protocol Buffers的"Base 128 Varints"整型长度字段值动态地将ByteBuf分隔
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        // 添加ProtobufEncoder编码器，用于处理消息的编码
        pipeline.addLast(new ProtobufEncoder());
        // 添加ProtobufDecoder解码器，用于解码消息
        pipeline.addLast(new ProtobufDecoder(lite));
        // 添加ObjectHandler，处理消息
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something with the object
        }
    }
}
