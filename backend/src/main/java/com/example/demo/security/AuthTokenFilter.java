package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.services.UserDetailsServiceImpl;
import java.util.List;
import java.util.Arrays;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
				System.out.println("_____________________filter is running "+request.getRequestURI().toString());
		try {
			String jwt = parseJwt(request);
			System.out.println("Check token: "+jwt);

			List<String> list = Arrays.asList("create","post","gen","logout","update","delete","get","put","list","add","/quer","upload","database");

			if(list.stream().anyMatch(s -> request.getRequestURI().toString().contains(s))){
				if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
					System.out.println("Token is ok");
					String username = jwtUtils.getUserNameFromJwtToken(jwt);
					System.out.println("User: "+username);
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	
					SecurityContextHolder.getContext().setAuthentication(authentication);
					filterChain.doFilter(request, response);
				}
				else{
					//SecurityContextHolder.clearContext();
					response.sendRedirect("/");
					System.out.println("Token is not ok");
				}
			} 
			else{
				System.out.println("Token is not required");
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}
