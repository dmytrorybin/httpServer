package hello.world;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.channel.socket.SocketChannel;


import io.netty.channel.ChannelInitializer;


/**
 * Created by ִלטענטי on 30.03.2015.
 */
public class HelloServer {

private int SERVER_PORT;

    public void run() throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
              //      .option(ChannelOption.SO_BACKLOG, 100)
             //       .handler((new LoggingHandler(LogLevel.DEBUG)))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            String ip = channel.remoteAddress().getHostString();


                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
//                            pipeline.addLast("Handler", new ServerHandler());

                            pipeline.addLast("decoder", new HttpRequestDecoder());
                            pipeline.addLast("encoder", new HttpResponseEncoder());
                            pipeline.addLast("handler", new ServerHandler(ip));

                            System.out.println("connection accepted");
                        }
                    });

            ChannelFuture future = bootstrap.bind(SERVER_PORT).sync();
            System.err.println("server initialized");

            future.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws Exception{
        HelloServer server = new HelloServer();
        if (args.length > 0)
            server.SERVER_PORT = Integer.parseInt(args[0]);
        else
            server.SERVER_PORT = 8080;

        server.run();

    }

}
