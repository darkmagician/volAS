/**
 * 
 */
package com.vol.mgmt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.mvel2.templates.TemplateRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.vol.common.tenant.Operator;

/**
 * @author scott
 *
 */
public class MailService {
	private static  final Logger log = LoggerFactory.getLogger(MailService.class);
	
	private boolean enable;
	
	@Resource(name="mailSender")
	private JavaMailSender mailSender;
	
	private String sender;
	
	private String registrationSubject;
	private String registrationTemplate;
	private String resetSubject;
	private String resetTemplate;	
	
	
	private String registrationTemplateContent;
	private String resetTemplateContent;
	
	public void init() throws IOException{
		if(!enable){
			return;
		}
		
		registrationTemplateContent =readContent(registrationTemplate);
		resetTemplateContent=readContent(resetTemplate);
	}
	
	public void sendMail(final String subject, final String to, final String sender, String template, Map<String,Object> context){
		if(!enable){
			return;
		}
		final String content = (String) TemplateRuntime.eval(template, context)  ;
		
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
               MimeMessageHelper message = new MimeMessageHelper(mimeMessage,"UTF-8");
				message.setFrom(sender);
				message.setTo(to);
				message.setSubject(subject);
				message.setText(content,false);

            }
         };
		         
         mailSender.send(preparator); 
	}

	private String readContent(String template) throws IOException {
		InputStream in = MailService.class.getResourceAsStream(template);
		String content = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		return content;
	}
	
	public void sendMailForRegistration(Operator operator, String newPass, Map<String,Object> context){
		String email = operator.getEmail();
		if(email == null || email.trim().equals("")){
			log.warn("Email is not provided for operator {}. Notification is not sended", operator.getName());
			return;
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("operator", operator);
		map.put("password", newPass);
		if(context != null){
			map.putAll(context);
		}

		sendMail(registrationSubject,email,sender,registrationTemplateContent,map);
	}

	
	
	public void sendMailForReset(Operator operator, String newPass, Map<String,Object> context){
		String email = operator.getEmail();
		if(email == null || email.trim().equals("")){
			log.warn("Email is not provided for operator {}. Reset Notification is not sended", operator.getName());
			return;
		}
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("operator", operator);
		map.put("password", newPass);
		if(context != null){
			map.putAll(context);
		}
		sendMail(resetSubject,operator.getEmail(),sender,resetTemplateContent,map);
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @param registrationSubject the registrationSubject to set
	 */
	public void setRegistrationSubject(String registrationSubject) {
		this.registrationSubject = registrationSubject;
	}

	/**
	 * @param registrationTemplate the registrationTemplate to set
	 */
	public void setRegistrationTemplate(String registrationTemplate) {
		this.registrationTemplate = registrationTemplate;
	}

	/**
	 * @param resetSubject the resetSubject to set
	 */
	public void setResetSubject(String resetSubject) {
		this.resetSubject = resetSubject;
	}

	/**
	 * @param resetTemplate the resetTemplate to set
	 */
	public void setResetTemplate(String resetTemplate) {
		this.resetTemplate = resetTemplate;
	}
}
