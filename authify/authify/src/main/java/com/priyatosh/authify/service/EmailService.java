package com.priyatosh.authify.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	@Value("${spring.mail.properties.mail.smtp.from}")
	private String fromEmail;
	public void sendWelcomeEmail(String toEmail, String name) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Welcome to Our platform");
		message.setText("Hello "+ name +", \n\n Thanks for registering with us! \n\nRegards,\n Authify Team");
		mailSender.send(message);
	}
/*
	public void sendResetOtpEmail(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Password Reset OTP");
		message.setText("Your OTP for resetting your password is "+ otp +".User this OTP to proced with resetting youyr password");
		mailSender.send(message);
	}
	
	public void sendOtpEmail(String toEmail,String otp) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("Account Verification OTP");
		message.setText("Your OTP is "+otp+". verify your account using this OTP.");
		mailSender.send(message);
	}
*/
	
	public void sendOtpEmail(String toEmail,String otp)throws MessagingException{
		Context context = new Context();
		context.setVariable("email", toEmail);
		context.setVariable("otp", otp);
		
		String proces = templateEngine.process("verify-email",context);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		
		helper.setFrom(fromEmail);
		helper.setTo(toEmail);
		helper.setSubject("Account Verification OTP");
		helper.setText(proces, true);
		
		mailSender.send(mimeMessage);
	}
	
	public void sendResetOtpEmail(String toEmail,String otp)throws MessagingException{
		Context context = new Context();
		context.setVariable("email", toEmail);
		context.setVariable("otp", otp);
		
		String proces = templateEngine.process("password-reset-email",context);
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		
		helper.setFrom(fromEmail);
		helper.setTo(toEmail);
		helper.setSubject("forgot Your Password?");
		helper.setText(proces, true);
		
		mailSender.send(mimeMessage);
	}
}
