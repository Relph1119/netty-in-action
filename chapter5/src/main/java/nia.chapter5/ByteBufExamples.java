package nia.chapter5;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * Created by kerr.
 *
 * Listing 5.1 Backing array
 *
 * Listing 5.2 Direct buffer data access
 *
 * Listing 5.3 Composite buffer pattern using ByteBuffer
 *
 * Listing 5.4 Composite buffer pattern using CompositeByteBuf
 *
 * Listing 5.5 Accessing the data in a CompositeByteBuf
 *
 * Listing 5.6 Access data
 *
 * Listing 5.7 Read all data
 *
 * Listing 5.8 Write data
 *
 * Listing 5.9 Using ByteBufProcessor to find \r
 *
 * Listing 5.10 Slice a ByteBuf
 *
 * Listing 5.11 Copying a ByteBuf
 *
 * Listing 5.12 get() and set() usage
 *
 * Listing 5.13 read() and write() operations on the ByteBuf
 *
 * Listing 5.14 Obtaining a ByteBufAllocator reference
 *
 * Listing 5.15 Reference counting
 *
 * Listing 5.16 Release reference-counted object
 */
public class ByteBufExamples {
    private final static Random random = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;
    /**
     * Listing 5.1 Backing array
     * 支撑数组：将数据存储在JVM的堆空间中
     */
    public static void heapBuffer() {
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        // 检查ByteBuf是否有一个支撑数组
        if (heapBuf.hasArray()) {
            // 如果有，则获取对该数组的引用
            byte[] array = heapBuf.array();
            // 计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            // 获得可读字节数
            int length = heapBuf.readableBytes();
            // 使用数组、偏移量和长度作为参数调用方法
            handleArray(array, offset, length);
        }
    }

    /**
     * Listing 5.2 Direct buffer data access
     * 访问直接缓冲区的数据
     */
    public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        // 检查ByteBuf是否由数组支撑。如果不是，那么这是一个直接缓冲区
        if (!directBuf.hasArray()) {
            // 获取可读字节数
            int length = directBuf.readableBytes();
            // 分配一个新的数组来保存具有该长度的字节数据
            byte[] array = new byte[length];
            // 将字节复制到该数组中
            directBuf.getBytes(directBuf.readerIndex(), array);
            // 使用数组、偏移量和长度作为参数调用方法
            handleArray(array, 0, length);
        }
    }

    /**
     * Listing 5.3 Composite buffer pattern using ByteBuffer
     * 使用ByteBuffer的复合缓冲区模式
     */
    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        // Use an array to hold the message parts
        // 创建一个包含两个ByteBuffer的数组来保存这些消息组件
        ByteBuffer[] message =  new ByteBuffer[]{ header, body };

        // Create a new ByteBuffer and use copy to merge the header and body
        // 创建用来保存所有这些数据的副本
        ByteBuffer message2 =
                ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }


    /**
     * Listing 5.4 Composite buffer pattern using CompositeByteBuf
     * 使用CompositeByteBuf的复合缓冲区模式
     */
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE; // can be backing or direct
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;   // can be backing or directy
        // 将ByteBuf实例追加到CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);
        //...
        // 删除位于索引位置为0（第一个组件）的ByteBuf
        messageBuf.removeComponent(0); // remove the header
        // 循环遍历所有的ByteBuf实例
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }

    /**
     * Listing 5.5 Accessing the data in a CompositeByteBuf
     * 访问CompositeByteBuf中的数据
     */
    public static void byteBufCompositeArray() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        // 获得可读字节数
        int length = compBuf.readableBytes();
        // 分配一个具有可读字节数长度的新数组
        byte[] array = new byte[length];
        // 将字节读到该数组中
        compBuf.getBytes(compBuf.readerIndex(), array);
        // 使用偏移量和长度作为参数使用该数组
        handleArray(array, 0, array.length);
    }

    /**
     * Listing 5.6 Access data
     * 访问数据
     */
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    /**
     * Listing 5.7 Read all data
     * 读取所有数据
     */
    public static void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * Listing 5.8 Write data
     * 写数据
     */
    public static void write() {
        // Fills the writable bytes of a buffer with random integers.
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }

    /**
     * Listing 5.9 Using ByteProcessor to find \r
     * 使用ByteProcessor来寻找回车符(\r)
     *
     * use {@link io.netty.buffer.ByteBufProcessor in Netty 4.0.x}
     */
    public static void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
    }

    /**
     * Listing 5.9 Using ByteBufProcessor to find \r
     *
     * use {@link io.netty.util.ByteProcessor in Netty 4.1.x}
     */
    public static void byteBufProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        int index = buffer.forEachByte(ByteBufProcessor.FIND_CR);
    }

    /**
     * Listing 5.10 Slice a ByteBuf
     * 对ByteBuf进行切片
     */
    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 创建该ByteBuf从索引0开始到索引15结束的一个新切片
        ByteBuf sliced = buf.slice(0, 15);
        // 将打印"Netty in Action"
        System.out.println(sliced.toString(utf8));
        // 更新索引0处的字节
        buf.setByte(0, (byte)'J');
        // 将会成功，因为数据是共享的，对其中一个所做的更改对另一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * Listing 5.11 Copying a ByteBuf
     * 复制一个ByteBuf
     */
    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 创建该ByteBuf从索引0开始到索引15结束的分段的副本
        ByteBuf copy = buf.copy(0, 15);
        // 将打印"Netty in Action"
        System.out.println(copy.toString(utf8));
        // 更新索引0处的字节
        buf.setByte(0, (byte)'J');
        // 将会成功，因为数据是不共享的
        assert buf.getByte(0) != copy.getByte(0);
    }

    /**
     * Listing 5.12 get() and set() usage
     * get()和set()方法的用法
     */
    public static void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第一个字符'N'
        System.out.println((char)buf.getByte(0));
        // 存储当前的readerIndex和writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        // 将索引0处的字节更新为字符'B'
        buf.setByte(0, (byte)'B');
        // 打印第一个字符，现在是'B'
        System.out.println((char)buf.getByte(0));
        // 以下测试会成功，因为这些操作不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    /**
     * Listing 5.13 read() and write() operations on the ByteBuf
     * ByteBuf上的read()和write()操作
     */
    public static void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        // 打印第一个字符'N'
        System.out.println((char)buf.readByte());
        // 存储当前的readerIndex和writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        // 将字符'?'追加到缓冲区
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        // 以下测试会成功，因为writeByte()方法移动了writerIndex
        assert writerIndex != buf.writerIndex();
    }

    private static void handleArray(byte[] array, int offset, int len) {}

    /**
     * Listing 5.14 Obtaining a ByteBufAllocator reference
     * 获取一个到ByteBufAllocator的引用
     */
    public static void obtainingByteBufAllocatorReference(){
        Channel channel = CHANNEL_FROM_SOMEWHERE; //get reference form somewhere
        // 从Channel获取一个到ByteBufAllocator引用
        ByteBufAllocator allocator = channel.alloc();
        //...
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE; //get reference form somewhere
        // 从ChannelHandlerContext获取一个到ByteBufAllocator的引用
        ByteBufAllocator allocator2 = ctx.alloc();
        //...
    }

    /**
     * Listing 5.15 Reference counting
     * 引用计数
     * */
    public static void referenceCounting(){
        Channel channel = CHANNEL_FROM_SOMEWHERE; //get reference form somewhere
        // 从Channel获取ByteBufAllocator
        ByteBufAllocator allocator = channel.alloc();
        //...
        // 从ByteBufAllocator分配一个ByteBuf
        ByteBuf buffer = allocator.directBuffer();
        // 检查引用计数是否为预期的1
        assert buffer.refCnt() == 1;
        //...
    }

    /**
     * Listing 5.16 Release reference-counted object
     * 释放引用计数的对象
     */
    public static void releaseReferenceCountedObject(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE; //get reference form somewhere
        // 减少到该对象的活动引用，当减少到0时，该对象被释放，并且该方法返回true
        boolean released = buffer.release();
        //...
    }


}
