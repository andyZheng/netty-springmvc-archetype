/*
 * 文件名：EmailMessageSender.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.platform.common.config.ConfigurationKey;
import com.platform.common.config.ConfigurationReader;

/**
 * 功能描述：<code>EmailMessageSender</code>是邮件信息发送器，本类提供发送消息并带附件功能。
 * <p>
 * 
 * @author Andy.zheng andy.zheng0807@gmail.com
 * @version 1.0, 2014年3月11日 下午7:24:19
 * @since AutoSalaryManage 1.0
 */
public class EmailMessageSender {

	/** 配置文件读取器 */
	private static ConfigurationReader reader = ConfigurationReader.getInstance();
	
	/** 邮件服务器地址 */
	private static String host = reader.getString(ConfigurationKey.EMAIL_SERVER_HOST);
	
	/** 邮件服务器端口 */
	private static int port = reader.getInt(ConfigurationKey.EMAIL_SERVER_PORT);
	
	/** 邮件服务器是否需要认证 */
	private static String isAuth = reader.getString(ConfigurationKey.EMAIL_SERVER_AUTH);
	
	/** 邮件服务器认证用户名 */
	private static String userName = reader.getString(ConfigurationKey.EMAIL_SERVER_USER);
	
	/** 邮件服务器认证密码 */
	private static String password = reader.getString(ConfigurationKey.EMAIL_SERVER_PASSWORD);
	
	/** 当前邮件发件人 */
	private static String from = reader.getString(ConfigurationKey.EMAIL_FROM_PERSON);
	
	/** 开启debug模式 */
	private static boolean debug = false;
	
	/** 邮件通道对象 */
	private static Transport transport = null;
	
	/** 邮件会话 */
	private static Session session = null;
	
	
	static{
		try {
			boolean model = reader.getBoolean(ConfigurationKey.ENABLE_DUBUG_MODEL);
			debug = model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == transport){
			Properties props = new Properties();
			
			// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
			props.put("mail.smtp.host", host);
			
			// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证
			props.put("mail.smtp.auth", isAuth);
			
			// 用刚刚设置好的props对象构建一个session
			session = Session.getDefaultInstance(props);

			// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
			// 用（你可以在控制台（console)上看到发送邮件的过程）
			session.setDebug(debug);
			
			try {
				transport = session.getTransport("smtp");
				// 连接服务器的邮箱
				transport.connect(host, port, userName, password);
				System.out.println("成功连接邮件服务器！！！");
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/** 邮件主题 */
	private String subject; 
	
	/** 收件人地址 */
	private String to;
	
	/** 附件地址 */
	private String affix;
	
	/** 附件名称 */
	private String affixName; 
	
	/** 邮件内容 */
	private String content;

	/***
	 * 
	 * 功能描述：设置收件人、主题信息。
	 *
	 * @param to		当前邮件收件人。
	 * @param subject	当前邮件主题。
	 * @param content	当前邮件内容。
	 */
	public void setHeader(String to, String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * 
	 * 功能描述：设置当前邮件附件信息。
	 *
	 * @param affix		当前附件文件。
	 * @param affixName 当前附件名称。
	 */
	public void setAffix(String affix, String affixName) {
		this.affix = affix;
		this.affixName = affixName;
	}
	
	/**
	 * 
	 * 功能描述：发送邮件内容。
	 *
	 */
	public boolean send() {
		boolean isSend = true;
		
		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 加载发件人地址
			message.setFrom(new InternetAddress(from));
			
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			// 加载标题
			message.setSubject(subject);

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(content);
			multipart.addBodyPart(contentPart);
			
			// 添加附件
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(affix);
			
			// 添加附件的内容
			messageBodyPart.setDataHandler(new DataHandler(source));
			
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
			multipart.addBodyPart(messageBodyPart);

			// 将multipart对象放到message中
			message.setContent(multipart);
			
			// 保存邮件消息
			message.saveChanges();
			
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			isSend = false;
		}
		
		return isSend;
	}
}
