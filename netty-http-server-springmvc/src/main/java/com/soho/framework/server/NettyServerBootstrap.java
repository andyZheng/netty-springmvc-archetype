/*
 * 文件名：Server.java
 * 版权：Copyright 2014-2015 Chengdu SOHO Studio. All Rights Reserved. 
 * 描述： 服务器框架
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.soho.framework.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import com.soho.framework.server.config.ServerConfig;
import com.soho.framework.server.netty.initializer.ChannelInitializerWrapper;
import com.soho.framework.server.netty.initializer.HttpChannelInitializer;
import com.soho.framework.server.servlet.config.ServletConfiguration;
import com.soho.framework.server.servlet.config.WebAppConfiguration;

/**
 * 功能描述：<code>Bootstrap</code>是本服务器引导程序类，用于调度服务器启动接口。
 * <p>
 * 
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2014年3月17日 上午10:37:49
 * @since Netty-SpringMVC Server Framework/BootStrap 1.0
 */
public class NettyServerBootstrap {

    /** 日志对象 */
    final static Logger logger = LoggerFactory.getLogger(NettyServerBootstrap.class);

    /** 服务器支持的命令行参数选项 */
    private Options options;

    /** Socket链接轮询器（acceptor thread loop, 默认线程数为处理器*2 ） */
    private NioEventLoopGroup acceptorGroup = new NioEventLoopGroup(1);

    /** IO处理轮询器（work thread loop, 默认线程数为处理器*2 ） */
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    /** 当前服务器待加载的ChannelInitializer */
    private ChannelInitializerWrapper currentInitializer;

    /**
     * 功能描述：缺省构造函数。
     * 
     */
    private NettyServerBootstrap() {
        this.registerCommandLineOptions();
    }

    /**
     * 功能描述： 创建HTTP服务器实例。
     *
     * @return  HTTP服务器实例。
     */
    public static NettyServerBootstrap createServer() {
        return new NettyServerBootstrap();
    }
    
    /**
     * 功能描述：启动服务器。
     *
     */
    public void start() {
        try {
            Channel channel = this.startup();
            
            String http = ServerConfig.isSsl() ? "https" : "http";
            System.err.println("Connect to " + http + "://127.0.0.1:" + ServerConfig.getPort() + '/');
            
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.shutdownServer();
        }
    }

    /**
     * 功能描述：执行服务器引导程序。
     * 
     * @throws Exception
     */
    public Channel startup() {
        Channel channel = null;
        try {
            // 创建netty服务端引导对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 设置接收器组以及请求轮询器组
            serverBootstrap.group(acceptorGroup, workerGroup);

            // 设置服务端soket通道
            serverBootstrap.channel(NioServerSocketChannel.class);

            // 配置Netty日志级别
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

            // configure web-app
            WebAppConfiguration webapp = new WebAppConfiguration()
                    .setName("netty-web-server") // 项目名称
                    .setSessionTimeout(ServerConfig.getSessionTimeout()) // 设置session超时时间
                    .setStaticResourcesFolder("/statics") // 静态资源名称
                    
                    .addContextParameter(ContextLoader.CONFIG_LOCATION_PARAM,"classpath*:/spring/applicationContext-*.xml")
                    .addServletContextListener(ContextLoaderListener.class)
                    .addServletConfigurations(new ServletConfiguration(DispatcherServlet.class)
                             // 缺省为 /WEB-INF/DispatcherServlet-servlet.xml
                            .addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "classpath:/springmvc-servlet.xml"))
//                    .addFilterConfigurations(new FilterConfiguration(CharacterEncodingFilter.class)
//                            .addInitParameter("encoding", ServerConfig.getCharsetName())
//                            .addInitParameter("forceEncoding", "true")
//                            )
                            
                    ;
            
            // 创建Http通道初始化器。
            currentInitializer = new HttpChannelInitializer(webapp);
            serverBootstrap.childHandler(currentInitializer);

            // 绑定服务器端口
            int port = ServerConfig.getPort();
            channel = serverBootstrap.bind(port).sync().channel();
            logger.info("Running {} netty server on {} ...", ServerConfig.getProtocol(), port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }
    
    /**
     * 功能描述：解析命令行参数。
     *
     * @param args
     *            当前命令行参数
     */
    public void parseCommondArguments(String[] args) {
        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            // 解析端口参数
            if (cmd.hasOption("port")) {
                try {
                    int port = Integer.valueOf(cmd.getOptionValue("port"));
                    ServerConfig.setPort(port);
                } catch (Exception e) {
                    logger.error("Parse port fail!", e);
                }
            }
            
            // TODO 解析其他参数
        } catch (Exception e) {
            logger.error("Error on cli", e);
        }
    }

    public void shutdownServer() {
        if (null != currentInitializer) {
            currentInitializer.shutdown();
        }
        if (acceptorGroup != null) {
            acceptorGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    private void registerCommandLineOptions() {
        options = new Options();

        // 服务器端口选项
        options.addOption("port", true, "sever port");
    }

    /**
     * 功能描述：入口程序。
     * 
     * @param args
     *            控制台参数
     */
    public static void main(String[] args) {
        NettyServerBootstrap bootStrap = NettyServerBootstrap.createServer();
        // 解析命令命令行参数
        bootStrap.parseCommondArguments(args);

        // 启动服务器
        bootStrap.start();
    }
}
