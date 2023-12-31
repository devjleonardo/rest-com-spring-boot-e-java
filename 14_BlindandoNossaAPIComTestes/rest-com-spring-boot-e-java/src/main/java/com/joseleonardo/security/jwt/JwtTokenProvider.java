package com.joseleonardo.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joseleonardo.data.dto.v1.security.TokenDTO;
import com.joseleonardo.exceptions.InvalidJwtAuthenticationException;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {
	
	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";
	
	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Autowired
	private UserDetailsService userDetailsService;
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}
	
	public TokenDTO criarAccessToken(String nomeDeUsuario, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);
		String accessToken = getAccessToken(nomeDeUsuario, roles, now, validity);
		String refreshToken = getRefreshToken(nomeDeUsuario, roles, now);
		
		return new TokenDTO(nomeDeUsuario, true, now, validity, accessToken, refreshToken);
	}
	
	public TokenDTO refreshToken(String refreshToken) {
		if (refreshToken.contains("Bearer ")) {
			refreshToken = refreshToken.substring("Bearer ".length());
		}
		
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
		
		String nomeDeUsuario = decodedJWT.getSubject();
		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
		
		return criarAccessToken(nomeDeUsuario, roles);
	}
	
	private String getAccessToken(String nomeDeUsuario, List<String> roles, Date now, Date validity) {
		String issuerUrl = ServletUriComponentsBuilder
				.fromCurrentContextPath().build().toUriString();
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withSubject(nomeDeUsuario)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}

	private String getRefreshToken(String nomeDeUsuario, List<String> roles, Date now) {
		Date validityRefreshToken = new Date(now.getTime() + (validityInMilliseconds * 3));
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(nomeDeUsuario)
				.sign(algorithm)
				.strip();
	}
	
	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		
		return new UsernamePasswordAuthenticationToken(decodedJWT, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier jwtVerifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = jwtVerifier.verify(token);
		
		return decodedJWT;
	}
	
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		// Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsZWFuZHJvIiwicm9sZXMiOlsiQURNSU4iLCJNQU5BR0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImV4cCI6MTY1MjcxOTUzOCwiaWF0IjoxNjUyNzE1OTM4fQ.muu8eStsRobqLyrFYLHRiEvOSHAcss4ohSNtmwWTRcY
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		
		return null;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		
		try {
			if (decodedJWT.getExpiresAt().before(new Date())) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Token JWT expirado ou inválido!");
		}
	}
}
