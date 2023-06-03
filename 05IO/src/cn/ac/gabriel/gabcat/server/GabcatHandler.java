package cn.ac.gabriel.gabcat.server;

import cn.ac.gabriel.gabcat.http.DefaultGabServlet;
import cn.ac.gabriel.gabcat.http.HttpGabRequest;
import cn.ac.gabriel.gabcat.http.HttpGabResponse;
import cn.ac.gabriel.gabcat.http.ResourceGabServlet;
import cn.ac.gabriel.gabcat.servlet.GabServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

public class GabcatHandler extends ChannelInboundHandlerAdapter {
    private Map<String, GabServlet> nameToServletMap;
    private Map<String, String> nameToClassNameMap;

    public GabcatHandler(Map<String, GabServlet> nameToServletMap, Map<String, String> nameToClassNameMap) {
        this.nameToServletMap = nameToServletMap;
        this.nameToClassNameMap = nameToClassNameMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            String uri = request.uri().toString();
            System.out.println("uri: " + uri);
            String servletName = uri.substring(uri.lastIndexOf("/") + 1);
            int index = servletName.indexOf("?");
            if (index != -1) {
                servletName = servletName.substring(0, index);
            }
            //TODO duel with the static resource
            if (servletName.contains(".do") || servletName.contains(".action")) {
                servletName = servletName.substring(0, servletName.indexOf("."));
            }

            GabServlet servlet = new DefaultGabServlet();
            ResourceGabServlet resourceServlet = new ResourceGabServlet();
            if (nameToServletMap.containsKey(servletName)) {
                servlet = nameToServletMap.get(servletName);
            } else if (nameToClassNameMap.containsKey(servletName)) {
                if (nameToServletMap.get(servletName) == null) {
                    synchronized (this) {
                        if (nameToServletMap.get(servletName) == null) {
                            String className = nameToClassNameMap.get(servletName);
                            Class<?> clazz = Class.forName(className);
                            servlet = (GabServlet) clazz.newInstance();
                            nameToServletMap.put(servletName, servlet);
                        }
                    }
                }
            } else {
                servlet = resourceServlet;
            }

            HttpGabRequest req = new HttpGabRequest(request);
            HttpGabResponse resp = new HttpGabResponse(request, ctx);
            if ("GET".equalsIgnoreCase(request.method().toString())) {
                servlet.doGet(req, resp);
            } else if ("POST".equalsIgnoreCase(request.method().toString())) {
                servlet.doPost(req, resp);
            }
            ctx.close();
        }
    }
}
