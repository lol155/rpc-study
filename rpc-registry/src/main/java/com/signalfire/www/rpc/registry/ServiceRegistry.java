package com.signalfire.www.rpc.registry;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

/**
 * 服务注册 ，ZK 在该架构中扮演了“服务注册表”的角色，用于注册所有服务器的地址与端口，并对客户端提供服务发现的功能
 *
 */
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    private String registryAddress;

    public ServiceRegistry(String registryAddress) {
        //zookeeper的地址
        this.registryAddress = registryAddress;
    }





}
