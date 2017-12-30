package com.signalfire.www.rpc.client;

import com.signalfire.www.rpc.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 框架的RPC 客户端（用于发送 RPC 请求）
 *
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(RpcClient.class);

    private String host;
    private int port;

    private RpcResponse response;

    private final Object obj = new Object();

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 读取服务端的返回结果
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        this.response = response;

        synchronized (obj) {
            obj.notifyAll();
        }
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }
}
