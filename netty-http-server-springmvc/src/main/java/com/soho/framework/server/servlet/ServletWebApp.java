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

package com.soho.framework.server.servlet;

import io.netty.channel.group.ChannelGroup;

import java.io.File;
import java.util.Map;

import com.soho.framework.server.servlet.config.FilterConfiguration;
import com.soho.framework.server.servlet.config.ServletConfiguration;
import com.soho.framework.server.servlet.config.ServletContextListenerConfiguration;
import com.soho.framework.server.servlet.config.WebAppConfiguration;
import com.soho.framework.server.servlet.impl.FilterChainImpl;
import com.soho.framework.server.servlet.impl.ServletContextImpl;

public class ServletWebApp {

    private static ServletWebApp instance;

    private WebAppConfiguration webAppConfig;

    private ChannelGroup sharedChannelGroup;

    public static ServletWebApp get() {

        if (instance == null)
            instance = new ServletWebApp();

        return instance;
    }

    private ServletWebApp() {
    }

    public void init(WebAppConfiguration webapp, ChannelGroup sharedChannelGroup) {
        this.webAppConfig = webapp;
        this.sharedChannelGroup = sharedChannelGroup;
        this.initServletContext();
        this.initContextListeners();
        this.initFilters();
        this.initServlets();
    }

    public void destroy() {
        this.destroyServlets();
        this.destroyFilters();
        this.destroyContextListeners();
    }

    private void initContextListeners() {
        if (webAppConfig.getServletContextListenerConfigurations() != null) {
            for (ServletContextListenerConfiguration ctx : webAppConfig.getServletContextListenerConfigurations()) {
                ctx.init();
            }
        }
    }

    private void destroyContextListeners() {
        if (webAppConfig.getServletContextListenerConfigurations() != null) {
            for (ServletContextListenerConfiguration ctx : webAppConfig.getServletContextListenerConfigurations()) {
                ctx.destroy();
            }
        }
    }

    private void destroyServlets() {
        if (webAppConfig.getServletConfigurations() != null) {
            for (ServletConfiguration servlet : webAppConfig.getServletConfigurations()) {
                servlet.destroy();
            }
        }
    }

    private void destroyFilters() {
        if (webAppConfig.getFilterConfigurations() != null) {
            for (FilterConfiguration filter : webAppConfig.getFilterConfigurations()) {
                filter.destroy();
            }
        }
    }

    protected void initServletContext() {
        ServletContextImpl ctx = ServletContextImpl.get();
        ctx.setServletContextName(this.webAppConfig.getName());
        
        if (webAppConfig.getContextParameters() != null) {
            for (Map.Entry<String, String> entry : webAppConfig.getContextParameters().entrySet()) {
                ctx.addInitParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void initFilters() {
        if (webAppConfig.getFilterConfigurations() != null) {
            for (FilterConfiguration filter : webAppConfig.getFilterConfigurations()) {
                filter.init();
            }
        }
    }

    protected void initServlets() {
        if (webAppConfig.hasServletConfigurations()) {
            for (ServletConfiguration servlet : webAppConfig.getServletConfigurations()) {
                servlet.init();
            }
        }
    }

    public FilterChainImpl initializeChain(String uri) {
        ServletConfiguration servletConfiguration = this.findServlet(uri);
        FilterChainImpl chain = new FilterChainImpl(servletConfiguration);

        if (this.webAppConfig.hasFilterConfigurations()) {
            for (FilterConfiguration s : this.webAppConfig.getFilterConfigurations()) {
                if (s.matchesUrlPattern(uri))
                    chain.addFilterConfiguration(s);
            }
        }

        return chain;
    }

    private ServletConfiguration findServlet(String uri) {

        if (!this.webAppConfig.hasServletConfigurations()) {
            return null;
        }

        for (ServletConfiguration s : this.webAppConfig.getServletConfigurations()) {
            if (s.matchesUrlPattern(uri))
                return s;
        }

        return null;
    }

    public File getStaticResourcesFolder() {
        return this.webAppConfig.getStaticResourcesFolder();
    }

    public WebAppConfiguration getWebappConfig() {
        return webAppConfig;
    }

    public ChannelGroup getSharedChannelGroup() {
        return sharedChannelGroup;
    }
}
