/*
 * 文件名：RapidChannelInitializer.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 服务器框架
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.soho.framework.server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 功能描述：<code>ChannelInitializerWrapper</code>是Channel Handler与业务容器的包装类。</p>
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2015年5月5日 上午9:59:29
 * @since    Game Server Framework/Channel Pipeline 1.0
 */
public abstract class ChannelInitializerWrapper extends ChannelInitializer<SocketChannel> {
  
    public abstract void shutdown();
}
