/*
 * Copyright 2013 by Maxim Kalina
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.soho.framework.server.netty.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soho.framework.server.servlet.async.ThreadLocalAsyncExecutor;

public class AsyncHttpServletHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpServletHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object e) throws Exception {
        if (e instanceof ServletResponse) {
            logger.info("Handler async task...");
            HttpServletResponse response = (HttpServletResponse)e;
            Runnable task = ThreadLocalAsyncExecutor.pollTask(response);
            task.run();
            
            // write response...
            ChannelFuture future = ctx.channel().writeAndFlush(response);

            String keepAlive = response.getHeader(CONNECTION);
            if (null != keepAlive && HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(keepAlive)) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } else {
            ctx.fireChannelRead(e);
        }
    }
}
