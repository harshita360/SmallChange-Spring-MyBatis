package com.fidelity.security;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.support.JstlUtils;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private Environment environment;

	private JwtTokenService jwtUtil;

	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment,
			JwtTokenService jwtUtil) {
		super(authenticationManager);
		this.environment = environment;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {


		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
		response.setHeader("Access-Control-Max-Age", "86400");
		response.setHeader("Access-Control-Allow-Headers",
				"Content-type,Authorization,append,delete,entries,foreach,get,has,keys,set,values");

		if (request.getRequestURI().toString().endsWith("login")) {
			chain.doFilter(request, response);
			return;
		}
		//System.out.println(request.getRequestURI() + ":" + request.getRequestURI().toString().endsWith("login"));
		String authorizationHeader = request.getHeader(environment.getProperty("authorization.headerName"));
		//System.out.println("Header ="+authorizationHeader);
		if (authorizationHeader == null) {
			chain.doFilter(request, response);
			//SecurityContextHolder.getContext().setAuthentication(null);
			return;
		}
		try {
			String token = authorizationHeader
					.substring(environment.getProperty("authorization.header.prefix").length() + 1);
			System.out.println(token);
			if (jwtUtil.isTokenExpired(token)) {
				response.setHeader("TokenExpired", "true");
				chain.doFilter(request, response);
				return;
			}
			String fmtsToken = jwtUtil.getClaimsFmtsToken(token);
			String role = jwtUtil.getClaimsRole(token);
			BigInteger userId = new BigInteger(jwtUtil.extractClientId(token));
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
			
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(fmtsToken, userId,
					authorities);
			
			//authRequest.setAuthenticated(true);
			
			SecurityContextHolder.getContext().setAuthentication(authRequest);
			
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			chain.doFilter(request, response);
		}
	}
}
