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

package com.soho.framework.server.servlet.interceptor;

import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.ServerCookieEncoder;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.soho.framework.server.servlet.impl.HttpSessionImpl;
import com.soho.framework.server.servlet.session.HttpSessionStore;
import com.soho.framework.server.servlet.session.HttpSessionThreadLocal;
import com.soho.framework.server.util.Utils;

public class HttpSessionInterceptor implements HttpServletInterceptor {

    private boolean sessionRequestedByCookie = false;

    public HttpSessionInterceptor(HttpSessionStore sessionStore) {
        HttpSessionThreadLocal.setSessionStore(sessionStore);
    }

    @Override
    public void onRequestReceived(ChannelHandlerContext ctx, HttpRequest request) {

        HttpSessionThreadLocal.unset();


        Collection<Cookie> cookies = Utils.getCookies(HttpSessionImpl.SESSION_ID_KEY, request);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String jsessionId = cookie.value();
                HttpSession s = HttpSessionThreadLocal.getSessionStore().findSession(jsessionId);
                if (s != null) {
                    HttpSessionThreadLocal.set(s);
                    this.sessionRequestedByCookie = true;
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestSuccessed(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {

        HttpSession s = HttpSessionThreadLocal.get();
        if (s != null && !this.sessionRequestedByCookie) {
            HttpHeaders.addHeader(response, SET_COOKIE, ServerCookieEncoder.encode(HttpSessionImpl.SESSION_ID_KEY, s.getId()));
        }

    }

    @Override
    public void onRequestFailed(ChannelHandlerContext ctx, Throwable e, HttpResponse response) {
        this.sessionRequestedByCookie = false;
        HttpSessionThreadLocal.unset();
    }

}
