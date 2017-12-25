package com.signalfire.www.rpc.sample.common;

public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
