package cn.ac.gabriel.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RPCClientHandler extends ChannelInboundHandlerAdapter {
    private Object Response;

    public Object getResponse() {
        return Response;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response = msg;
        ctx.close();
    }
}
