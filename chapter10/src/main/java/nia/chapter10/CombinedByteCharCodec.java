package nia.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Listing 10.10 CombinedChannelDuplexHandler<I,O>
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
// ByteToCharDecoder解码器和CharToByteEncoder编码器合并为 CombinedByteCharCodec编解码器类
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        // 将委托的实例传给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
