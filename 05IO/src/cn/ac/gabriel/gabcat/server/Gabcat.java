package cn.ac.gabriel.gabcat.server;

public class Gabcat {
    public static void main(String[] args) throws Exception {
        GabcatServer server = new GabcatServer("cn.ac.gabriel.gabcat");
        server.start();
    }
}
