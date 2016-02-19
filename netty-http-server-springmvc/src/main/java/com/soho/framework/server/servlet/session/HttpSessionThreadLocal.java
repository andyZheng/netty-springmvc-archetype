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

package com.soho.framework.server.servlet.session;

import javax.servlet.http.HttpSession;

import com.soho.framework.server.servlet.ServletWebApp;
import com.soho.framework.server.servlet.impl.HttpSessionImpl;

public class HttpSessionThreadLocal {

    public static final ThreadLocal<HttpSession> sessionThreadLocal = new ThreadLocal<HttpSession>();

    private static HttpSessionStore sessionStore;

    public static HttpSessionStore getSessionStore() {
        return sessionStore;
    }

    public static void setSessionStore(HttpSessionStore store) {
        sessionStore = store;
    }

    public static void set(HttpSession session) {
        sessionThreadLocal.set(session);
    }

    public static void unset() {
        sessionThreadLocal.remove();
    }

    public static HttpSessionImpl get() {
        HttpSessionImpl session = (HttpSessionImpl)sessionThreadLocal.get();
        if (session != null)
            session.touch();
        return session;
    }

    public static HttpSessionImpl getOrCreate() {
        if (HttpSessionThreadLocal.get() == null) {
            if (sessionStore == null) {
                sessionStore = new DefaultHttpSessionStore();
            }

            HttpSession newSession = sessionStore.createSession();
            newSession.setMaxInactiveInterval(ServletWebApp.get().getWebappConfig().getSessionTimeout());
            sessionThreadLocal.set(newSession);
        }
        
        return get();
    }

}
