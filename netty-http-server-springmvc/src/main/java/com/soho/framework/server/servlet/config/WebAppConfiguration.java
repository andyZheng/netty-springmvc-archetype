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

package com.soho.framework.server.servlet.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

public class WebAppConfiguration {

    private String name;

    private int sessionTimeout = 60 * 60; // 1 hour

    private Map<String, String> contextParameters;

    private Collection<ServletContextListenerConfiguration> contextListeners;

    private Collection<FilterConfiguration> filters;

    private Collection<ServletConfiguration> servlets;

    private File staticResourcesFolder;

    public WebAppConfiguration addContextParameter(String name, String value) {

        if (this.contextParameters == null)
            this.contextParameters = new HashMap<String, String>();

        this.contextParameters.put(name, value);
        return this;
    }

    public WebAppConfiguration addServletContextListener(Class<? extends ServletContextListener> listenerClass) {
        return this.addServletContextListenerConfigurations(new ServletContextListenerConfiguration(listenerClass));
    }

    public WebAppConfiguration addServletContextListener(ServletContextListener listener) {
        return this.addServletContextListenerConfigurations(new ServletContextListenerConfiguration(listener));
    }

    public WebAppConfiguration addServletContextListenerConfigurations(ServletContextListenerConfiguration... configs) {

        if (configs == null || configs.length == 0)
            return this;

        return this.addServletContextListenerConfigurations(Arrays.asList(configs));
    }

    public WebAppConfiguration addServletContextListenerConfigurations(List<ServletContextListenerConfiguration> configs) {
        if (configs == null || configs.size() == 0)
            return this;

        if (this.contextListeners == null)
            this.contextListeners = new ArrayList<ServletContextListenerConfiguration>();

        this.contextListeners.addAll(configs);
        return this;
    }

    public Collection<ServletContextListenerConfiguration> getServletContextListenerConfigurations() {
        return this.contextListeners != null ? Collections.unmodifiableCollection(this.contextListeners) : null;
    }

    public Map<String, String> getContextParameters() {
        return this.contextParameters != null ? Collections.unmodifiableMap(this.contextParameters) : null;
    }

    public WebAppConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public WebAppConfiguration setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout * 60;
        return this;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public WebAppConfiguration addFilter(Filter filter) {
        return this.addFilterConfigurations(new FilterConfiguration(filter));
    }

    public WebAppConfiguration addFilter(Filter filter, String... urlPatterns) {
        return this.addFilterConfigurations(new FilterConfiguration(filter, urlPatterns));
    }

    public WebAppConfiguration addFilter(Class<? extends Filter> filterClass) {
        return this.addFilterConfigurations(new FilterConfiguration(filterClass));
    }

    public WebAppConfiguration addFilter(Class<? extends Filter> filterClass, String... urlPatterns) {
        return this.addFilterConfigurations(new FilterConfiguration(filterClass, urlPatterns));
    }

    public WebAppConfiguration addFilterConfigurations(FilterConfiguration... filters) {

        if (filters == null || filters.length == 0)
            return this;

        return this.addfilterConfigurations(Arrays.asList(filters));
    }

    public WebAppConfiguration addfilterConfigurations(Collection<FilterConfiguration> configs) {

        if (configs == null || configs.size() == 0)
            return this;

        if (this.filters == null)
            this.filters = new ArrayList<FilterConfiguration>();

        this.filters.addAll(configs);
        return this;
    }

    public Collection<FilterConfiguration> getFilterConfigurations() {
        return this.filters != null ? Collections.unmodifiableCollection(this.filters) : null;
    }

    public boolean hasFilterConfigurations() {
        return this.filters != null && !this.filters.isEmpty();
    }

    public WebAppConfiguration addHttpServlet(HttpServlet servlet) {
        return this.addServletConfigurations(new ServletConfiguration(servlet));
    }

    public WebAppConfiguration addHttpServlet(HttpServlet servlet, String... urlPatterns) {
        return this.addServletConfigurations(new ServletConfiguration(servlet, urlPatterns));
    }

    public WebAppConfiguration addHttpServlet(Class<? extends HttpServlet> servletClass) {
        return this.addServletConfigurations(new ServletConfiguration(servletClass));
    }

    public WebAppConfiguration addHttpServlet(Class<? extends HttpServlet> servletClass, String... urlPatterns) {
        return this.addServletConfigurations(new ServletConfiguration(servletClass, urlPatterns));
    }

    public WebAppConfiguration addServletConfigurations(ServletConfiguration... servlets) {

        if (servlets == null || servlets.length == 0)
            return this;

        return this.addServletConfigurations(Arrays.asList(servlets));
    }

    public WebAppConfiguration addServletConfigurations(Collection<ServletConfiguration> configs) {

        if (configs == null || configs.size() == 0)
            return this;

        if (this.servlets == null)
            this.servlets = new ArrayList<ServletConfiguration>();

        this.servlets.addAll(configs);
        return this;
    }

    public Collection<ServletConfiguration> getServletConfigurations() {
        return this.servlets != null ? Collections.unmodifiableCollection(this.servlets) : null;
    }

    public boolean hasServletConfigurations() {
        return this.servlets != null && !this.servlets.isEmpty();
    }

    public WebAppConfiguration setStaticResourcesFolder(String folder) {
        return this.setStaticResourcesFolder(new File(folder));
    }

    public WebAppConfiguration setStaticResourcesFolder(File folder) {
        if (folder == null)
            throw new IllegalArgumentException("Static resources folder must be not null!");

        if (!folder.exists())
            folder.mkdirs();

        if (!folder.isDirectory())
            throw new IllegalArgumentException("Static resources folder '" + folder.getAbsolutePath()
                    + "' must be a directory!");

        this.staticResourcesFolder = folder;
        return this;
    }

    public File getStaticResourcesFolder() {
        return staticResourcesFolder;
    }
}
