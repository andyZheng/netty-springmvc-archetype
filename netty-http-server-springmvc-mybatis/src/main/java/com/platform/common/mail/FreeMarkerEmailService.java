package com.platform.common.mail;

import com.platform.common.utils.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * <code>FreeMarkerEmailService</code> is a mail service utility class base freemarker.</p>
 * <p>Sample code for an HTML mail with an inline image and a PDF attachment:
 *
 * <pre class="code">
 * private FreeMarkerEmailService emailService;
 *
 * mailSender.send(new MimeMailMessageBuilder() {
 *   public void prepare(MimeMessageHelper message) throws MessagingException {
 *     message.setFrom("me@mail.com");
 *     message.setTo("you@mail.com");
 *     message.setSubject("my subject");
 *     message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
 *     message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
 *   }
 *
 *   public String getTemplateName(MimeMessageHelper message) {
 *     return "demo.html";
 *   }
 *
 *   public Map<String, String> getParameters() {
 *     Map<String, String> params = new HashMap<String, String>();
 *     params.put("userName", "admin");
 *     params.put("password", "admin");
 *     return params;
 *   }
 * });
 * </pre>
 *
 * @author Andy.Zheng andy.zheng0807@gmail.com
 * @version 1.0, 2015/1/12 15:40
 * @since Common-Platform/Email Service 1.0
 */
@Service
public class FreeMarkerEmailService implements InitializingBean {

	private static  final Logger log = LoggerFactory.getLogger(FreeMarkerEmailService.class);

	@Value("#{systemPropertyLookup['mail.from']}")
	private String from;

	@Value("#{systemPropertyLookup['mail.fromPersonal']}")
	private String fromPersonal;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactory;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * Invoked by a BeanFactory after it has set all bean properties supplied
	 * (and satisfied BeanFactoryAware and ApplicationContextAware).
	 * <p>This method allows the bean instance to perform initialization only
	 * possible when all bean properties have been set and to throw an
	 * exception in the event of misconfiguration.
	 *
	 * @throws Exception in the event of misconfiguration (such
	 *                   as failure to set an essential property) or if initialization fails.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(mailSender, "javaMailSender must be not null");
		Assert.notNull(freeMarkerConfigurationFactory, "freeMarkerConfigurationFactory must be not null");

		if (null == threadPoolTaskExecutor) {
			threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
			threadPoolTaskExecutor.setCorePoolSize(3);
			threadPoolTaskExecutor.setMaxPoolSize(10);
		}
	}

	/**
	 * Send a simple mail message, including data such as the from, to, cc, subject, and text fields.
	 * @param simpleMailMessage simple text message.
	 * @return true If send success, other false.
	 */
	public boolean send(final SimpleMailMessage simpleMailMessage) {
		Assert.notNull(simpleMailMessage, "simpleMailMessage must be not null");
		boolean isSuccess = false;
		try {
			log.info("Starting to send ......");
			String currentFrom = simpleMailMessage.getFrom();
			if (StringUtils.isEmpty(currentFrom)) {
				simpleMailMessage.setFrom(from);
			}
			mailSender.send(simpleMailMessage);
			isSuccess = true;
			log.info("Send success!");
		} catch (MailException e) {
			log.info("Send fail!\n", e);
		}

		return isSuccess;
	}

	/**
	 * Send a simple mail message, including data such as the from, to, cc, subject, and text fields.</p>
	 * @param simpleMailMessage simple text message.
	 * @return true If send success, other false.
	 */
	public Future<Boolean> sendByAsync(final SimpleMailMessage simpleMailMessage) {
		Assert.notNull(simpleMailMessage, "simpleMailMessage must be not null");
		Future<Boolean> result = threadPoolTaskExecutor.submit(new Callable<Boolean>() {
			/**
			 * Computes a result, or throws an exception if unable to do so.
			 *
			 * @return computed result
			 * @throws Exception if unable to compute a result
			 */
			@Override
			public Boolean call() throws Exception {
				return send(simpleMailMessage);
			}
		});

		return result;
	}

	/**
	 * Send a mime mail message, including data such as the from, to, cc, subject, and html fields and attachment files.</p>
	 *
	 *<p>Sample code for an HTML mail with an inline image and a PDF attachment:
	 *
	 * <pre class="code">
	 * mailSender.send(new MimeMailMessageBuilder() {
	 *   public void prepare(MimeMessageHelper message) throws MessagingException {
	 *     message.setFrom("me@mail.com");
	 *     message.setTo("you@mail.com");
	 *     message.setSubject("my subject");
	 *     message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
	 *     message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
	 *   }
	 *
	 *   public String getTemplateName(MimeMessageHelper message) {
	 *     return "demo.html";
	 *   }
	 *
	 *   public Map<String, String> getParameters() {
	 *     Map<String, String> params = new HashMap<String, String>();
	 *     params.put("userName", "admin");
	 *     params.put("password", "admin");
	 *     return params;
	 *   }
	 *
	 * });</pre>
	 *
	 * @param mimeMailMessageBuilder builder mime type message.
	 * @return	true If send success, other false.
	 */
	public boolean send(MimeMailMessageBuilder mimeMailMessageBuilder) {
		Assert.notNull(mimeMailMessageBuilder, "mimeMailMessage must be not null");
		boolean isSuccess = false;
		try {
			log.info("Starting to send ......");
			// Construct mime message
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			Configuration config = freeMarkerConfigurationFactory.getObject();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, config.getDefaultEncoding());
			mimeMailMessageBuilder.prepare(mimeMessageHelper);

			// Set default from.
			Address[] fromAddress = mimeMessage.getFrom();
			if (null == fromAddress || fromAddress.length == 0) {
				mimeMessageHelper.setFrom(from, fromPersonal);
			}

			// Content
			String templateName = mimeMailMessageBuilder.getTemplateName();
			Assert.notNull(templateName, "templateName must be not null");
			Template template = config.getTemplate(templateName);
			String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mimeMailMessageBuilder.getParameters());
			mimeMessageHelper.setText(htmlText, true);

			// Send message
			mailSender.send(mimeMessage);
			isSuccess = true;
			log.info("Send success!");
		} catch (MessagingException e) {
			log.error("Send fail!\n", e);
		} catch (IOException e) {
			log.error("Not found template file!\n", e);
		} catch (TemplateException e) {
			log.error("Parse template fail!\n", e);
		}

		return  isSuccess;
	}

	/**
	 * Send a mime mail message, including data such as the from, to, cc, subject, and html fields and attachment files.</p>
	 *
	 *<p>Sample code for an HTML mail with an inline image and a PDF attachment:
	 *
	 * <pre class="code">
	 * mailSender.send(new MimeMailMessageBuilder() {
	 *   public void prepare(MimeMessageHelper message) throws MessagingException {
	 *     message.setFrom("me@mail.com");
	 *     message.setTo("you@mail.com");
	 *     message.setSubject("my subject");
	 *     message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
	 *     message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
	 *   }
	 *
	 *   public String getTemplateName(MimeMessageHelper message) {
	 *     return "demo.html";
	 *   }
	 *
	 *   public Map<String, String> getParameters() {
	 *     Map<String, String> params = new HashMap<String, String>();
	 *     params.put("userName", "admin");
	 *     params.put("password", "admin");
	 *     return params;
	 *   }
	 *
	 * });</pre>
	 *
	 * @param mimeMailMessageBuilder builder mime type message.
	 * @return	true If send success, other false.
	 */
	public Future<Boolean> sendByAsync(final MimeMailMessageBuilder mimeMailMessageBuilder) {
		Assert.notNull(mimeMailMessageBuilder, "mimeMailMessageBuilder must be not null");
		Future<Boolean> result = threadPoolTaskExecutor.submit(new Callable<Boolean>() {
			/**
			 * Computes a result, or throws an exception if unable to do so.
			 *
			 * @return computed result
			 * @throws Exception if unable to compute a result
			 */
			@Override
			public Boolean call() throws Exception {
				return send(mimeMailMessageBuilder);
			}
		});

		return result;
	}


	public interface MimeMailMessageBuilder {
		public String getTemplateName();
		public Map<String, String> getParameters();
		public void prepare(MimeMessageHelper mimeMessageHelper) throws MessagingException;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void setFreeMarkerConfigurationFactory(FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactory) {
		this.freeMarkerConfigurationFactory = freeMarkerConfigurationFactory;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfigurationFactory() {
		return freeMarkerConfigurationFactory;
	}

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}
}
