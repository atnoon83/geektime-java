package cn.ac.gabriel.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static final CopyOnWriteArrayList<Channel> clients = new CopyOnWriteArrayList<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        clients.add(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        clients.remove(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "下线了");
    }

    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "说：" + s);
        for (Channel client : clients) {
            if (client != channel) {
                client.writeAndFlush(channel.remoteAddress() + "说：" + s);
            }
        }
    }
}
