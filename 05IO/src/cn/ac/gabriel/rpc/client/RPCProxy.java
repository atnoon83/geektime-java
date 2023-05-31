package cn.ac.gabriel.rpc.client;

import cn.ac.gabriel.rpc.common.MethodInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RPCProxy {
    public static Object create(Class<?> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                MethodInfo methodInfo = new MethodInfo(clazz.getName(), method.getName(), method.getParameterTypes(), args);

                RPCClient client = new RPCClient("localhost", 8888, methodInfo);
                return client.run();
            }
        });
    }
}
