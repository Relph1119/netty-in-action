package nia.chapter13;

import java.net.InetSocketAddress;

/**
 * Listing 13.1 LogEvent message
 * LogEvent消息POJO类
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public final class LogEvent {
    // 消息分隔符
    public static final byte SEPARATOR = (byte) ':';
    // 发送LogEvent的源的InetSocketAddress
    private final InetSocketAddress source;
    // 日志文件名
    private final String logfile;
    // 消息内容
    private final String msg;
    // 接收LogEvent的时间
    private final long received;

    // 用于传出消息的构造函数
    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    // 用于传入消息的构造函数
    public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceivedTimestamp() {
        return received;
    }
}
