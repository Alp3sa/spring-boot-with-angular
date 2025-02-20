package com.example.demo.controllers;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

import com.example.demo.models.User;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.AuthEntryPointJwt;
import com.example.demo.security.AuthTokenFilter;
import com.example.demo.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@RestController
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
	   web.ignoring().antMatchers("/login","/api/auth/login");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// To enable access through any path.
		/* Before authentication.
		http.authorizeRequests()
                   .antMatchers("/**")
				   .permitAll().and()
				   .exceptionHandling().accessDeniedPage("/error");
		*/
			
		http.exceptionHandling().accessDeniedPage("/error")
			.authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/**").permitAll()
			.anyRequest().authenticated();

			// To allow requests selectively.
			http.csrf().ignoringAntMatchers("/api/auth/login","/api/auth/signup","/api/auth/logout","/gen","/upload","/delete/database","/delete/query");
			
			// To completely allow POST, PUT and DELETE methods.
			//http.csrf().disable();

			//Filter to check authentication.
			http.addFilterAfter(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@PostMapping("/login")
    public String user(@Valid @RequestBody User loginRequest) throws Exception {
		System.out.println("Check authentication ");
		
		Authentication authentication = super.authenticationManagerBean().authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*String jwt = Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + 86400000))
				.signWith(SignatureAlgorithm.HS512, qgen)
				.compact();

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());*/

		//return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    	return "";
    } 
}