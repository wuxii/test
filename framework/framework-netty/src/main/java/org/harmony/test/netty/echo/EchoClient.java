package org.harmony.test.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    private static final String host = "localhost";
    private static final int port = 8080;

    public static void main(String[] args) throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); //
            b.group(workerGroup); //
            b.channel(NioSocketChannel.class); //
            b.option(ChannelOption.TCP_NODELAY, true); //
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); //

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}