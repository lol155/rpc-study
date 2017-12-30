package com.signalfire.www.rpc.sample.app;

import com.signalfire.www.rpc.sample.common.HelloService;
import com.signalfire.www.rpc.sample.common.Person;
import org.springframework.beans.factory.annotation.Autowired;

public class HelloServiceTest {


    @Autowired
    private RpcProxy rpcProxy;

    public void helloTest1() {
        //调用代理的create方法，代理helloservice接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        //调用代理的方法，执行invoke
        String result = helloService.hello("World");
        System.out.println("服务器返回结果：");
        System.out.println(result);
    }

    public void helloTest2() {
        //调用代理的create方法，代理helloservice接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        //调用代理的方法，执行invoke
        String result = helloService.hello(new Person("Wan","hai"));
        System.out.println("服务器返回结果：");
        System.out.println(result);
    }

}
