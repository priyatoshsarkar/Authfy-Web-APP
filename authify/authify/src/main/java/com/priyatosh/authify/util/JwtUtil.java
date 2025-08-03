package com.priyatosh.authify.util;

//import java.sql.Date;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.validation.Valid;

@Component
public class JwtUtil {

	@Value("${jwt.secret.key}")
	private String SECRET_KEY;
	public String generateToken(UserDetails userDetails) {
		Map<String ,Object> clieams = new HashMap<>();
		return createToken(clieams, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> clieams, String email) {
		return Jwts.builder()
			.setClaims(clieams)
			.setSubject(email)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+ 100 *60 * 60 *10 )) //10 hour
			.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY)
			.compact();
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.setSigningKey(SECRET_KEY)
			.parseClaimsJws(token)
			.getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
