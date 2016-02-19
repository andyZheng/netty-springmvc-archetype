/**
 *
 */
package com.platform.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author minwh
 */
public class ApplicationContextUtils implements ApplicationContextAware {
    
    /** 日志对象 */
    private static Log log = LogFactory.getLog(ApplicationContextUtils.class);
    
    /** 应用程序上下文 */
    private static ApplicationContext applicationContext;

    /**
     * 功能描述：设置应用程序上下文对象。
     * @param context 待设置的应用程序上下文对象。
     */
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        if (applicationContext != null) {
            throw new BeanCreationException("ApplicationContextHolder already holded 'applicationContext'.");
        }
        
        applicationContext = context;
        log.info("holded applicationContext,displayName:" + applicationContext.getDisplayName());
    }

    /**
     * 
     * 功能描述：获取应用程序上下文。
     *
     * @return  应用程序上下文。
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null)
            throw new IllegalStateException("'applicationContext' property is null,ApplicationContextUtils not yet init.");

        return applicationContext;
    }

    /**
     * 
     * 功能描述：获取系统注入的bean.
     *
     * @param beanName  业务bean名称。
     * @return          业务bean对象。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(final String beanName) {
        return (T) getApplicationContext().getBean(beanName);
    }

}
