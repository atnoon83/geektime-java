package cn.ac.gabriel.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ChatServer {
    private ServerSocketChannel channel;
    private Selector selector;

    public ChatServer(int port) {
        try {
            channel = ServerSocketChannel.open();
            channel.socket().bind(new InetSocketAddress(port));
            System.out.println("ChatServer started on port " + port);
            channel.configureBlocking(false);

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (true) {
                if (selector.select(3000) == 0) {
                    System.out.println("Waiting for connection...");
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel sc = this.channel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("Accepting connection..." + sc.getLocalAddress());
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        while (channel.read(buffer) != -1) {
                            buffer.flip();
                            for (SelectionKey otherKey : selector.keys()) {
                                SelectableChannel target = otherKey.channel();
                                if (target instanceof SocketChannel && target != channel) {
                                    ((SocketChannel) target).write(buffer);
                                    buffer.rewind();
                                }
                            }
                            buffer.clear();
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(8888);
        server.start();
    }
}
