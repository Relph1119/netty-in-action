package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Listing 11.2 Adding support for HTTP
 * 添加HTTP支持
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */

public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            // 如果是客户端，则添加HttpResponseDecoder解码器来处理服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder());
            // 添加HttpRequestEncoder编码器来向服务器发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            // 如果是服务器，则添加HttpRequestDecoder解码器来接收客户端的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());
            // 添加HttpResponseEncoder编码器来向客户端发送响应
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
