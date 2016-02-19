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

package com.soho.framework.server.servlet.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soho.framework.server.servlet.ServletWebApp;
import com.soho.framework.server.servlet.config.ServletConfiguration;
import com.soho.framework.server.util.Utils;

public class ServletContextImpl extends ConfigAdapter implements ServletContext {

    private static final Logger log = LoggerFactory.getLogger(ServletContextImpl.class);

    private static ServletContextImpl instance;

    private Map<String, Object> attributes;

    private String servletContextName;

    public static ServletContextImpl get() {
        if (instance == null)
            instance = new ServletContextImpl();

        return instance;
    }

    private ServletContextImpl() {
        super("Netty Servlet");
    }

    @Override
    public Object getAttribute(String name) {
        return attributes != null ? attributes.get(name) : null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Utils.enumerationFromKeys(attributes);
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public int getMajorVersion() {
        return 3;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return ServletContextImpl.class.getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return ServletContextImpl.class.getResourceAsStream(path);
    }

    @Override
    public String getServerInfo() {
        return super.getOwnerName();
    }

    @Override
    public void log(String msg) {
        log.info(msg);
    }

    @Override
    public void log(Exception exception, String msg) {
        log.error(msg, exception);
    }

    @Override
    public void log(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void removeAttribute(String name) {
        if (this.attributes != null)
            this.attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object object) {
        if (this.attributes == null)
            this.attributes = new HashMap<String, Object>();

        this.attributes.put(name, object);
    }

    @Override
    public String getServletContextName() {
        return this.servletContextName;
    }

    public void setServletContextName(String servletContextName) {
        this.servletContextName = servletContextName;
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        Collection<ServletConfiguration> colls = ServletWebApp.get().getWebappConfig().getServletConfigurations();
        HttpServlet servlet = null;
        for (ServletConfiguration configuration : colls) {
            if (configuration.getHttpComponent().getServletName().equalsIgnoreCase(name)) {
                servlet = configuration.getHttpComponent();
            }
        }
        
        return servlet;
    }

    @Override
    public Enumeration<String> getServletNames() {
        Collection<ServletConfiguration> colls = ServletWebApp.get().getWebappConfig().getServletConfigurations();
        
        List<String> servletNames = new ArrayList<String>();
        for (ServletConfiguration configuration : colls) {
            servletNames.add(configuration.getHttpComponent().getServletName());
        }
        
        return Utils.enumeration(servletNames);
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        Collection<ServletConfiguration> colls = ServletWebApp.get().getWebappConfig().getServletConfigurations();
        
        List<Servlet> servlets = new ArrayList<Servlet>();
        for (ServletConfiguration configuration : colls) {
            servlets.add(configuration.getHttpComponent());
        }
        
        return Utils.enumeration(servlets);
    }

    @Override
    public ServletContext getContext(String uripath) {
        return this;
    }

    @Override
    public String getMimeType(String file) {
        return Utils.getMimeType(file);

    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        Collection<ServletConfiguration> colls = ServletWebApp.get().getWebappConfig().getServletConfigurations();
        HttpServlet servlet = null;
        for (ServletConfiguration configuration : colls) {
            if (configuration.getConfig().getServletName().equals(name)) {
                servlet = configuration.getHttpComponent();
            }
        }

        return new RequestDispatcherImpl(name, null, servlet);
    }

    @Override
    public String getRealPath(String path) {
        if (null != path && path.startsWith("/")) {
            try {
                File file = File.createTempFile("netty-http-server", "");
                file.mkdirs();
                return file.getAbsolutePath();
            } catch (IOException e) {
                throw new IllegalStateException("Method 'getRealPath' not yet implemented!");
            }
        } else {
            throw new IllegalStateException("Method 'getRealPath' not yet implemented!");
        }
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        Collection<ServletConfiguration> colls = ServletWebApp.get().getWebappConfig().getServletConfigurations();
        HttpServlet servlet = null;
        String servletName = null;
        for (ServletConfiguration configuration : colls) {
            if (configuration.matchesUrlPattern(path)) {
                servlet = configuration.getHttpComponent();
                servletName = configuration.getHttpComponent().getServletName();
            }
        }

        return new RequestDispatcherImpl(servletName, path, servlet);
    }

    @Override
    public int getEffectiveMajorVersion() {
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        this.setInitParameter(name, value);
        return false;
    }

    @Override
    public Dynamic addServlet(String servletName, String className) {
        return null;
    }

    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        return null;
    }

    @Override
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    @Override
    public void addListener(String className) {
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public void declareRoles(String... roleNames) {
    }

}
