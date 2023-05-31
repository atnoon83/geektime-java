package cn.ac.gabriel.rpc.server;

import cn.ac.gabriel.rpc.common.MethodInfo;
import io.netty.channel.*;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

public class RPCServerHandler extends ChannelInboundHandlerAdapter {

    private String getClassName(MethodInfo methodInfo) throws ClassNotFoundException {
        String path = "cn.ac.gabriel.rpc.server.impl";
        String className = methodInfo.getClassName();
        Class supperClass = Class.forName(className);
        Set<Class> impls = new Reflections(path).getSubTypesOf(supperClass);
        if (impls.isEmpty()) {
            System.out.println("No implementation found for " + supperClass.getName());
            return null;
        } else if (impls.size() > 1) {
            System.out.println("More than one implementation found for " + supperClass.getName());
            return null;
        } else {
            return impls.iterator().next().getName();
        }
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MethodInfo methodInfo = (MethodInfo) msg;
        String className = getClassName(methodInfo);
        if (className != null) {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.newInstance();
            Method method = clazz.getMethod(methodInfo.getMethodName(), methodInfo.getParameterTypes());
            Object result = method.invoke(instance, methodInfo.getParameters());
            ctx.writeAndFlush(result);
        }
    }
}
