package com.weatherandoutfit.infra;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@PropertySource("classpath:gmail.properties")
@Service
public class SendEmailService {
	@Value("${gmail}")
	private String sender;
	@Autowired
	private RedisService redis;
	private final JavaMailSender mailSender;
	private int authNumber;
	
	@Autowired
	public SendEmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void createAuthNumber() {
		Random random = new Random();
		String randomStr = "";
		for(int i = 0 ; i < 6; i++) {
			randomStr += Integer.toString(random.nextInt(10));
		}
		authNumber = Integer.parseInt(randomStr);
	}
	
	public String setEmail(String email) {
		createAuthNumber();
		String receiver = email;
		String title = "Weather & Outfit에서 보내는 인증번호 메일입니다";
		String body ="<html><body>"+ 
								"Weather & Outfit" +
								"<br><br><br>" +
								"인증 번호 <br>" + 
								"<h2>" + authNumber + "</h2><br>"
								+"</body></html>";
		log.info("{}, {} ", sender, receiver);
		mailSend(sender, receiver, title, body);
		
		return Integer.toString(authNumber);
	}

	private void mailSend(String from, String receiver, String title, String body) {
		log.info("{},{}" ,from, receiver);
		 log.info("mailSender is null: {}", mailSender == null);
	
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
			helper.setFrom(from);
			helper.setTo(receiver);
			helper.setSubject(title);
			helper.setText(body, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		redis.setData(Integer.toString(authNumber), receiver);
	}

	public Boolean checkAuthCode(String email, String authCode) {
		if(redis.getData(authCode) == null) {
			return false;
		}else if(redis.getData(authCode).equals(email)) {
			return true;
		} else {			
			return false;
		}
	}
	
}
