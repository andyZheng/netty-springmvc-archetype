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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soho.framework.server.servlet.impl.HttpSessionImpl;

public class DefaultHttpSessionStore implements HttpSessionStore {

    private static final Logger log = LoggerFactory.getLogger(DefaultHttpSessionStore.class);

    private static ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

    @Override
    public HttpSession createSession() {
        String sessionId = this.generateNewSessionId();
        log.debug("Creating new session with id {}", sessionId);
        
        HttpSession session = new HttpSessionImpl(sessionId);
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public void destroySession(String sessionId) {
        HttpSession session = this.findSession(sessionId);
        if (null != session) {
            session = null;
        }
        
        log.debug("Destroying session with id {}", sessionId);
        sessions.remove(sessionId);
    }

    @Override
    public HttpSession findSession(String sessionId) {
        if (sessionId == null)
            return null;

        return sessions.get(sessionId);
    }

    public String generateNewSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void destroyInactiveSessions() {
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            HttpSession session = entry.getValue();
            if (session.getMaxInactiveInterval() < 0)
                continue;

            long currentMillis = System.currentTimeMillis();

            if (currentMillis - session.getLastAccessedTime() > session.getMaxInactiveInterval() * 1000) {
                session.invalidate();
                destroySession(entry.getKey());
            }
        }
    }

}
