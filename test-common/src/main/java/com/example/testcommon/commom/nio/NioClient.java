package com.example.testcommon.commom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * 2020/3/8
 * created by chenpp
 */
public class NioClient {


    private static Selector selector = null;


    public void start(String ip, int port) throws IOException {
        //创建选择器
        selector = Selector.open();
        //打开监听通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //连接对应的服务器 ip , port
        socketChannel.connect(new InetSocketAddress(ip, port));
        //注册select为连接状态
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        System.out.println("客户端，启动成功...");
    }

    public void listen() throws IOException {
        while (true) {
            //阻塞方法，轮询注册的channel,当至少一个channel就绪的时候才会继续往下执行
            selector.select();
            //获取就绪的SelectionKey
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            SelectionKey key = null;
            //迭代就绪的key
            while (it.hasNext()) {
                key = it.next();
                it.remove();
                //SelectionKey相当于是一个Channel的表示，标记当前channel处于什么状态
                // 按照channel的不同状态处理数据
                process(key);

            }
        }
    }

    private void process(SelectionKey key) throws IOException {
        //channel处于可连接状态，发送消息给服务端

        if (key.isConnectable()) {
            System.out.println("connect事件就绪 ....");
            SocketChannel clientChannel = (SocketChannel) key.channel();
            if (clientChannel.isConnectionPending()) {
                clientChannel.finishConnect();
            }
            clientChannel.configureBlocking(false);
            String name = UUID.randomUUID().toString();
            System.out.println("客户端发送数据：{}" + name);
            ByteBuffer buffer = ByteBuffer.wrap(name.getBytes());
            clientChannel.write(buffer);
            clientChannel.register(key.selector(), SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            //获取对应的socket
            System.out.println("read事件就绪 ....");
            SocketChannel socket = (SocketChannel) key.channel();
            //设置一个读取数据的Buffer 大小为1024
            ByteBuffer buff = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            int len = socket.read(buff);
            if (len > 0) {
                buff.flip();
                content.append(new String(buff.array(), "utf-8"));
                //让客户端读取下一次read
                System.out.println("客户端收到反馈：" + content);
                key.interestOps(SelectionKey.OP_READ);
            } else if (len <= 0) {
                key.cancel();
                socket.close();
            }

        }
    }


}