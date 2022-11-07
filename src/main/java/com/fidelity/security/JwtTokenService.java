package com.fidelity.security;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtTokenService {
	
	//@Value("jwt.expiration")
	private long expiration=4523452;
	
	//@Value("jwt.secrete-token")
	private String secreteKey="Nikhil V";
	
	
	private long getCurrentTime() {
		Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("IST"));
		return cal.getTimeInMillis();
	}
	
	public String extractClientId(String token)   {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date getExpiration(String token)   {
		return extractClaim(token, Claims::getExpiration );
	}
	
	public String getClaimsRole(String token)  {
		Claims claims=extractAllClaims(token);
		return (String) claims.get("ROLE");
	}
	
	public String getClaimsFmtsToken(String token)  {
		Claims claims=extractAllClaims(token);
		return claims.get("fmtsToken").toString();
	}
	
	public Claims getClaim(String token)   {
		final Claims claims=extractAllClaims(token);
		return claims;
	}

	public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
		final Claims claims=extractAllClaims(token);
		return claimsResolver.apply(claims);
		
	}
	
	public boolean isTokenExpired(String token)   {
		return getExpiration(token).before(new Date(this.getCurrentTime()));
	}
	
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secreteKey).parseClaimsJws(token).getBody();
	}
	
	
	public String createToken(Map<String,Object> claims,BigInteger clientId)   {
		return Jwts.builder().setClaims(claims).setSubject(clientId.toString()).setIssuedAt(new Date())
				.setExpiration(new Date(this.getCurrentTime()+(expiration)))
				.signWith(SignatureAlgorithm.HS256, secreteKey).compact();
	}
	public boolean validateToken(String token)   {
		
		return this.isTokenExpired(token);
		
	}

}
