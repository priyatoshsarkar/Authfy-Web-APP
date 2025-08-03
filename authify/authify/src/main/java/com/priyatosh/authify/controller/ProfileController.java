package com.priyatosh.authify.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.priyatosh.authify.io.ProfileRequest;
import com.priyatosh.authify.io.ProfileResponse;
import com.priyatosh.authify.service.EmailService;
import com.priyatosh.authify.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class ProfileController {
	private final ProfileService profileService;
	private final EmailService emailService;
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ProfileResponse register(@Valid @RequestBody ProfileRequest request) {
		ProfileResponse response = profileService.createProfile(request);
		emailService.sendWelcomeEmail(response.getEmail(), response.getName());
		return response;
	}
	
	@GetMapping("/profile")
	public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name")String email) {
		return profileService.getProfile(email);
	}
}
