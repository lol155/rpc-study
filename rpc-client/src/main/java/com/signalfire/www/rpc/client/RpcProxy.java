package com.signalfire.www.rpc.client;

import com.signalfire.www.rpc.common.RpcRequest;
import com.signalfire.www.rpc.common.RpcResponse;
import com.signalfire.www.rpc.registry.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * RPC 代理（用于创建 RPC 服务代理）
 *
 */
public class RpcProxy {

    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcProxy(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 创建代理
     *
     * @param interfaceClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        //创建rpcRequest 封装被代理类的属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());

                        //拿到声明这个方法的业务接口名称
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        request.setMethodName(method.getName());

                        //查找服务
                        if (serviceDiscovery != null) {
                            serverAddress = serviceDiscovery.disCovery();
                        }


                        //随机获取服务地址
                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        //创建Netty实现的RpcClient 链接服务端
                        RpcClient rpcClient = new RpcClient(host, port);

                        //创建Netty向服务端发送请求
                        RpcResponse response = rpcClient.send(request);

                        //返回信息
                        if (response.isError()) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                });
    }

}

