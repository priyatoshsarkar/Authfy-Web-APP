package com.priyatosh.authify.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.priyatosh.authify.service.AppUserDetailsService;
import com.priyatosh.authify.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter{
	private final AppUserDetailsService appUserDetailsService;
	private final JwtUtil jwtUtil;
	public static final List<String> PUBLIC_URLS = List.of(
	        "/login",
	        "/register",
	        "/send-resent-otp",
	        "/reset-password",
	        "/logout"
	    );
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getServletPath();
		if(PUBLIC_URLS.contains(path)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		String jwt = null;
		String email = null;
		
		
		final String authorizationHeader = request.getHeader("Authorization");
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
		}
		
		
		if (jwt == null) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					if("jwt".equals(cookie.getName())) {
						jwt = cookie.getValue();
						break;
					}
				}
			}
		}
		
		if (jwt != null) {
			email = jwtUtil.extractEmail(jwt);
			if(email != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
				UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
				if(jwtUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
