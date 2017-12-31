package com.signalfire.www.rpc.sample.app;

import com.signalfire.www.rpc.client.RpcProxy;
import com.signalfire.www.rpc.sample.common.HelloService;
import com.signalfire.www.rpc.sample.common.Person;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloServiceTest {


    @Autowired
    private static RpcProxy rpcProxy;

    @BeforeClass
    public static void init() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        rpcProxy = (RpcProxy) context.getBean("rpcProxy");
    }

    @Test
    public void helloTest1() {
        //调用代理的create方法，代理helloservice接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        //调用代理的方法，执行invoke
        String result = helloService.hello("World");
        System.out.println("服务器返回结果：");
        System.out.println(result);
    }

    @Test
    public void helloTest2() {
        //调用代理的create方法，代理helloservice接口
        HelloService helloService = rpcProxy.create(HelloService.class);

        //调用代理的方法，执行invoke
        String result = helloService.hello(new Person("Wan","hai"));
        System.out.println("服务器返回结果：");
        System.out.println(result);
    }

}
