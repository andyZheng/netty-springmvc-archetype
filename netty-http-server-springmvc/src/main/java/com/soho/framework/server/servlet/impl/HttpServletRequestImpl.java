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

import static io.netty.handler.codec.http.HttpHeaders.Names.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static io.netty.handler.codec.http.HttpHeaders.Names.WWW_AUTHENTICATE;
import static io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType.Attribute;
import static javax.servlet.DispatcherType.REQUEST;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.ssl.SslHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.soho.framework.server.netty.ChannelThreadLocal;
import com.soho.framework.server.servlet.ServletWebApp;
import com.soho.framework.server.servlet.config.ServletConfiguration;
import com.soho.framework.server.servlet.session.HttpSessionThreadLocal;
import com.soho.framework.server.util.Utils;

public class HttpServletRequestImpl implements HttpServletRequest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    

    private final HttpRequest originalRequest;
    
    private final ServletInputStreamImpl inputStream;
    
    private final BufferedReader reader;

    private final QueryStringDecoder queryStringDecoder;
    
    
    private String characterEncoding;
    
    private URIParser uriParser;

    private HttpPostRequestDecoder postRequestDecoder;

    private Principal userPrincipal;

    private boolean asyncStarted;

    private DispatcherType dispatcherType = REQUEST;

    private AsyncContextImpl asyncContext;
    
    private Map<String, Object> attributes;
    private Map<String, Part> parts;
    private Map<String, String[]> parmeters;
    
   
    public HttpServletRequestImpl(HttpRequest request, FilterChainImpl chain) {
        this.originalRequest = request;

        if (request instanceof FullHttpRequest) {
            this.inputStream = new ServletInputStreamImpl((FullHttpRequest) request);
        } else {
            this.inputStream = new ServletInputStreamImpl(request);
        }
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.queryStringDecoder = new QueryStringDecoder(request.uri());

        // post请求
        if (HttpMethod.POST.name().equalsIgnoreCase(request.method().name())) {
            postRequestDecoder = new HttpPostRequestDecoder(request);
        }

        this.uriParser = new URIParser(chain);
        this.uriParser.parse(request.uri());
        this.characterEncoding = Utils.getCharsetFromContentType(getContentType());
    }

    public HttpRequest getOriginalRequest() {
        return originalRequest;
    }

    @Override
    public String getContextPath() {
        return ServletContextImpl.get().getContextPath();
    }

    @Override
    public Cookie[] getCookies() {
        String cookieString = this.originalRequest.headers().get(COOKIE);
        if (cookieString != null) {
            Set<io.netty.handler.codec.http.Cookie> cookies = CookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                Cookie[] cookiesArray = new Cookie[cookies.size()];
                int indx = 0;
                for (io.netty.handler.codec.http.Cookie c : cookies) {
                    Cookie cookie = new Cookie(c.name(), c.value());
                    cookie.setComment(c.comment());
                    cookie.setDomain(c.domain());
                    cookie.setMaxAge((int) c.maxAge());
                    cookie.setPath(c.path());
                    cookie.setSecure(c.isSecure());
                    cookie.setVersion(c.version());
                    cookiesArray[indx] = cookie;
                    indx++;
                }
                return cookiesArray;

            }
        }
        return null;
    }

    @Override
    public long getDateHeader(String name) {
        String longVal = getHeader(name);
        if (longVal == null)
            return -1;

        return Long.parseLong(longVal);
    }

    @Override
    public String getHeader(String name) {
        return HttpHeaders.getHeader(this.originalRequest, name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Utils.enumeration(this.originalRequest.headers().names());
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return Utils.enumeration(this.originalRequest.headers().getAll(name));
    }

    @Override
    public int getIntHeader(String name) {
        return HttpHeaders.getIntHeader(this.originalRequest, name, -1);
    }

    @Override
    public String getMethod() {
        return this.originalRequest.method().name();
    }

    @Override
    public String getQueryString() {
        return this.uriParser.getQueryString();
    }

    @Override
    public String getRequestURI() {
        return this.uriParser.getRequestUri();
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        String scheme = this.getScheme();
        int port = this.getServerPort();
        String urlPath = this.getRequestURI();

        // String servletPath = req.getServletPath ();
        // String pathInfo = req.getPathInfo ();

        url.append(scheme); // http, https
        url.append("://");
        url.append(this.getServerName());
        if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
            url.append(':');
            url.append(this.getServerPort());
        }

        url.append(urlPath);
        return url;
    }

    @Override
    public int getContentLength() {
        return (int) HttpHeaders.getContentLength(this.originalRequest, -1);
    }

    @Override
    public String getContentType() {
        return HttpHeaders.getHeader(this.originalRequest, HttpHeaders.Names.CONTENT_TYPE);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values != null ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (null == this.parmeters) {
            this.parmeters = new HashMap<>();
            
            // get query string parameters.
            Map<String, List<String>> queryStringParameters = this.queryStringDecoder.parameters();
            for (String key : queryStringParameters.keySet()) {
                List<String> values = queryStringParameters.get(key);
                this.parmeters.put(key, values.toArray(new String[values.size()]));
            }
            
            // get request body parameters for the post request.
            if (null != this.postRequestDecoder) {
                List<InterfaceHttpData> bodyHttpDatas = this.postRequestDecoder.getBodyHttpDatas();
                for (InterfaceHttpData data : bodyHttpDatas) {
                    if (data.getHttpDataType() == Attribute) {
                        Attribute attribute = (Attribute) data;
                        try {
                            Object obj = this.parmeters.get(attribute.getName());
                            if (null == obj) {
                                this.parmeters.put(attribute.getName(), new String[]{attribute.getValue()});
                            } else {
                                String[] values = (String[])obj;
                                String[] tempValues = new String[]{attribute.getValue()};
                                String[] newValues = new String[values.length + tempValues.length];
                                System.arraycopy(values, 0, newValues, 0, values.length);
                                System.arraycopy(tempValues, 0, newValues, values.length, tempValues.length);
                                this.parmeters.put(attribute.getName(), newValues);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        return this.parmeters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Utils.enumerationFromKeys(this.getParameterMap());
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.getParameterMap().get(name);
    }

    
    @Override
    public String getProtocol() {
        return this.originalRequest.protocolVersion().toString();
    }

    @Override
    public Object getAttribute(String name) {
        if (attributes != null)
            return this.attributes.get(name);

        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Utils.enumerationFromKeys(this.attributes);
    }

    @Override
    public void removeAttribute(String name) {
        if (this.attributes != null)
            this.attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        if (this.attributes == null)
            this.attributes = new HashMap<String, Object>();

        this.attributes.put(name, o);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return this.reader;
    }

    @Override
    public String getRequestedSessionId() {
        HttpSessionImpl session = HttpSessionThreadLocal.get();
        return session != null ? session.getId() : null;
    }

    @Override
    public HttpSession getSession() {
        HttpSession s = HttpSessionThreadLocal.getOrCreate();
        return s;
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession session = HttpSessionThreadLocal.get();
        if (session == null && create) {
            session = HttpSessionThreadLocal.getOrCreate();
        }
        return session;
    }

    @Override
    public String getPathInfo() {
        return this.uriParser.getPathInfo();
    }

    @Override
    public Locale getLocale() {
        String locale = HttpHeaders.getHeader(this.originalRequest, Names.ACCEPT_LANGUAGE, DEFAULT_LOCALE.toString());
        return new Locale(locale);
    }

    @Override
    public String getRemoteAddr() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().remoteAddress();
        return addr.getAddress().getHostAddress();
    }

    @Override
    public String getRemoteHost() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().remoteAddress();
        return addr.getHostName();
    }

    @Override
    public int getRemotePort() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().remoteAddress();
        return addr.getPort();
    }

    @Override
    public String getServerName() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().localAddress();
        return addr.getHostName();
    }

    @Override
    public int getServerPort() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().localAddress();
        return addr.getPort();
    }

    @Override
    public String getServletPath() {
        String servletPath = this.uriParser.getServletPath();
        if (servletPath.equals("/"))
            return "";

        return servletPath;
    }

    @Override
    public String getScheme() {
        return this.isSecure() ? "https" : "http";
    }

    @Override
    public boolean isSecure() {
        return ChannelThreadLocal.get().pipeline().get(SslHandler.class) != null;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return true;
    }

    @Override
    public String getLocalAddr() {
        InetSocketAddress addr = (InetSocketAddress) ChannelThreadLocal.get().localAddress();
        return addr.getAddress().getHostAddress();
    }

    @Override
    public String getLocalName() {
        return getServerName();
    }

    @Override
    public int getLocalPort() {
        return getServerPort();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        this.characterEncoding = env;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        Collection<Locale> locales = Utils.parseAcceptLanguageHeader(HttpHeaders.getHeader(this.originalRequest,
                HttpHeaders.Names.ACCEPT_LANGUAGE));

        if (locales == null || locales.isEmpty()) {
            locales = new ArrayList<Locale>();
            locales.add(Locale.getDefault());
        }
        return Utils.enumeration(locales);
    }

    @Override
    public String getAuthType() {
        return getHeader(WWW_AUTHENTICATE);
    }

    @Override
    public String getPathTranslated() {
        throw new IllegalStateException("Method 'getPathTranslated' not yet implemented!");
    }

    @Override
    public String getRemoteUser() {
        return getHeader(AUTHORIZATION);
    }

    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new IllegalStateException("Method 'isRequestedSessionIdFromURL' not yet implemented!");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new IllegalStateException("Method 'isRequestedSessionIdFromUrl' not yet implemented!");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new IllegalStateException("Method 'isRequestedSessionIdValid' not yet implemented!");
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new IllegalStateException("Method 'isUserInRole' not yet implemented!");
    }

    @Override
    public String getRealPath(String path) {
        throw new IllegalStateException("Method 'getRealPath' not yet implemented!");
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
    public ServletContext getServletContext() {
        return ServletContextImpl.get();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return startAsync(this, null);
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {

        this.asyncStarted = true;
        this.asyncContext = new AsyncContextImpl(servletRequest, servletResponse);
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return this.asyncStarted;
    }

    @Override
    public boolean isAsyncSupported() {
        return true;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return this.asyncContext;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return dispatcherType;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {
    }

    @Override
    public void logout() throws ServletException {
    }

    public void addPart(Part part) {
        this.parts.put(part.getName(), part);
    }

    @Override
    public Part getPart(String name) throws IOException, IllegalStateException, ServletException {
        return this.parts.get(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, IllegalStateException, ServletException {
        return this.parts.values();
    }

}
