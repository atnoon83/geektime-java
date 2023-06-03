package cn.ac.gabriel.gabcat.server;

import cn.ac.gabriel.gabcat.servlet.GabServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GabcatServer {
    private Map<String, GabServlet> nameToServletMap = new ConcurrentHashMap<>();
    private Map<String, String> nameToClassNameMap = new HashMap<>();
    private String basePackage;

    public GabcatServer(String basePackage) {
        this.basePackage = basePackage;
    }

    public void start() throws Exception {
        cacheClassName(this.basePackage);

        runServer();
    }

    private void cacheClassName(String basePackage) {
        URL resource = this.getClass().getClassLoader()
                .getResource(basePackage.replaceAll("\\.", "/"));
        if (resource == null) {
            return;
        }
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cacheClassName(basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String simpleClassName = file.getName().replace(".class", "").trim();
                nameToClassNameMap.put(simpleClassName.toLowerCase(), basePackage + "." + simpleClassName);
            }
        }
    }

    private void runServer() throws Exception {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();
        try {
            int port = initPort();
            ChannelFuture future = new ServerBootstrap()
                    .group(parent, child)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new GabcatHandler(nameToServletMap, nameToClassNameMap));
                        }
                    }).bind(port).sync();
            System.out.println("Server started at port " + port);
            future.channel().closeFuture().sync();
        } finally {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }

    }

    private int initPort() throws DocumentException, IOException {
        InputStream inputStream = GabcatServer.class.getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        Node node = document.selectSingleNode("Server").selectSingleNode("Port");
        int port = Integer.parseInt(node.getText());
        inputStream.close();
        return port;
    }
}