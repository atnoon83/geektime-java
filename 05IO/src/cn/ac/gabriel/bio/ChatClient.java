package cn.ac.gabriel.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    Socket socket;

    public ChatClient(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
            this.socket.setKeepAlive(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean send(String msg) {
        try {
            if ("bye".equalsIgnoreCase(msg.trim())) {
                this.socket.close();
                return false;
            }
            OutputStream outputStream = this.socket.getOutputStream();
            outputStream.write(msg.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8888);

        while (true){
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            if (!client.send(msg)) {
                break;
            }
        }
    }
}
