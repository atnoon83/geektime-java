package cn.ac.gabriel.rpc.client;

import cn.ac.gabriel.rpc.common.MethodInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RPCClient {
    private final String host;
    private final int port;
    private final MethodInfo methodInfo;

    public RPCClient(String host, int port, MethodInfo methodInfo) {
        this.host = host;
        this.port = port;
        this.methodInfo = methodInfo;
    }

    public Object run() {
        EventLoopGroup group = new NioEventLoopGroup();
        RPCClientHandler handler = new RPCClientHandler();
        try {
            Channel channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("encoder", new ObjectEncoder())
                                    .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    .addLast(handler);
                        }
                    })
                    .connect(host, port)
                    .sync()
                    .channel();
            channel.writeAndFlush(methodInfo);
            channel.closeFuture().sync();
            return handler.getResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return null;
    }
}
