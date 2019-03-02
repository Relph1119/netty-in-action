package nia.chapter12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Listing 12.2 Handling text frames
 * 处理文本帧
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果握手成功，则从ChannelPipeline中移除HttpRequestHandler，这是因为将不会接收到任何HTTP消息
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已连接的WebSocket新客户端已经连上了
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            //将新的WebSocket Channel添加到ChannelGroup中，以便它可以接收到所有的消息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数，并将它写到ChannelGroup中所有连接的客户端
        group.writeAndFlush(msg.retain());
    }
}
