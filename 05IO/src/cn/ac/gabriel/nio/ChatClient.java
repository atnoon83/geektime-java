package cn.ac.gabriel.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChatClient {
    private SocketChannel channel;

    public ChatClient(String addr, int port) {
        try {
            this.channel = SocketChannel.open();
            this.channel.configureBlocking(false);
            this.channel.connect(new InetSocketAddress(addr, port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg) {
        try {
            if (!this.channel.finishConnect()) {
                System.out.println("Waiting for connection...");
                return;
            }
            if ("bye".equalsIgnoreCase(msg.trim())) {
                this.channel.close();
                return;
            }
            this.channel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read() {
        try {
            if (!this.channel.isConnected()) {
                return;
            }
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (this.channel.read(buffer) > 0) {
                System.out.println(new String(buffer.array()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8888);

        Thread thread = new Thread(() -> {
            while (true) {
                client.read();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String msg = scanner.nextLine();
            client.send(msg);
        }
    }
}
