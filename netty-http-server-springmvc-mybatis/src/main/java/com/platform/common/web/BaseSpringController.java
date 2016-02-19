package com.platform.common.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.platform.common.utils.StringUtils;

public abstract class BaseSpringController {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	protected HttpServletRequest request;
	
	   @InitBinder
	    public void initBinder(final WebDataBinder binder, final WebRequest request) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	        binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
	        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
	        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	        binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
	        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
	        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
	        binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
	    }

	protected MultipartFile getFile(final String name) {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest)request;
			return multipartRequset.getFile(name);
		}
		return null;
	}

	protected List<MultipartFile> getFiles(final String name) {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest)request;
			return multipartRequset.getFiles(name);
		}
		return null;
	}

	protected Map<String, MultipartFile> getFileMap() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest)request;
			return multipartRequset.getFileMap();
		}
		return null;
	}

	protected MultiValueMap<String, MultipartFile> getMultiFileMap() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request instanceof MultipartHttpServletRequest) {
			final MultipartHttpServletRequest multipartRequset = (MultipartHttpServletRequest)request;
			return multipartRequset.getMultiFileMap();
		}
		return null;
	}

	/**
	 * Be equivalent to <code> request.getParameter(name)</code>.
	 */
	protected String get(final String name) {
		return request.getParameter(name);
	}

	protected Integer getInteger(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Integer.valueOf(str);
		}
		return null;
	}

	protected boolean getBoolean(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Boolean.valueOf(str);
		}
		return false;
	}
	protected Long getLong(final String name) {
		final String str = request.getParameter(name);
		if (StringUtils.isNotBlank(str)) {
			return Long.valueOf(str);
		}
		return null;
	}

	/**
	 * Be equivalent to <code>request.setAttribute(key, value)</code>.
	 */
	protected void put(final String key, final Object value) {
		request.setAttribute(key, value);
	}

	/**
	 * Be equivalent to <code>request.getSession().getAttribute(key)</code>.
	 */
	protected <T> T getSessionAttribute(final String key) {
		return (T)WebUtils.getSessionAttribute(request, key);
	}

	/**
	 * Be equivalent to <code>request.getSession().removeAttribute(key)</code>.
	 */
	protected void removeSessionAttribute(final String key) {
		request.getSession().removeAttribute(key);
	}

	/**
	 * Be equivalent to <code>request.getSession().setAttribute(key, value)</code>.
	 */
	protected void setSessionAttribute(final String key, final Object value) {
		request.getSession().setAttribute(key, value);
	}

	public void success() {
		success(null);
	}

	public void success(final String msg) {
		Result.success(msg);
	}

	public void fail() {
		success(null);
	}

	public void fail(final String msg) {
		Result.fail(msg);
	}
	
	public void fail(final String msg, final Exception e) {
		Result.fail(msg, e);
	}
}
