package cn.ac.gabriel.gabcat.http;

import cn.ac.gabriel.gabcat.servlet.GabResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

import java.nio.charset.StandardCharsets;

/**
 * Wrapper of Http Response
 */
public class HttpGabResponse implements GabResponse {
    /**
     * Http Request
     */
    HttpRequest request;
    /**
     * Channel Handler Context
     */
    ChannelHandlerContext ctx;

    /**
     * Constructor
     * @param request Http Request
     * @param ctx Channel Handler Context
     */
    public HttpGabResponse(HttpRequest request, ChannelHandlerContext ctx) {
        this.request = request;
        this.ctx = ctx;
    }

    /**
     * Define the response and write content to response
     * @param content content to write
     * @throws Exception exception when writing
     */
    @Override
    public void write(String content) throws Exception {
        if (StringUtil.isNullOrEmpty(content)) {
            return;
        }

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));

        HttpHeaders headers = response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "application/json")
                .set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
                .set(HttpHeaderNames.EXPIRES, 0);
        if (response.headers().contains(HttpHeaderNames.CONNECTION)) {
            headers.set(HttpHeaderNames.CONNECTION, response.headers().get(HttpHeaderNames.CONNECTION));
        }
        ctx.writeAndFlush(response);
    }
}
