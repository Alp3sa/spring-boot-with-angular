package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.request.LogoutRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtUtils;
import com.example.demo.services.UserDetailsImpl;
import java.io.File;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println("+++++++++"+loginRequest.getUsername()+" "+loginRequest.getPassword());
		Authentication authentication;
		try{
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		}catch(Exception e){
			System.out.println("Las credenciales introducidas no son correctas.");
			return ResponseEntity
					.ok()
					.body(new MessageResponse("Las credenciales introducidas no son correctas."));
		}
		System.out.println("**********"+loginRequest.getUsername()+" "+loginRequest.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		System.out.println("*********User token: "+jwt);
		
		//SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		System.out.println("*********User logged in successfully!");

		// Create a personal folder, which will contain user's files, if this doesn't exist already.
		createPersonalDirectory(userDetails.getUsername());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("********Signup");
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.ok()
					.body(new MessageResponse("El nombre de usuario introducido ya existe."));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.ok()
					.body(new MessageResponse("Ya existe una cuenta registrada con ese email."));
		}

		if (signUpRequest.getUsername().length()<3 || signUpRequest.getUsername().length()>30) {
			return ResponseEntity
					.ok()
					.body(new MessageResponse("El nombre de usuario debe tener 3 caracteres como mínimo y 30 como máximo."));
		}

		if (signUpRequest.getEmail().length()>50) {
			return ResponseEntity
					.ok()
					.body(new MessageResponse("El email debe tener como máximo 50 caracteres."));
		}

		if (signUpRequest.getPassword().length()<6 || signUpRequest.getPassword().length()>40) {
			return ResponseEntity
					.ok()
					.body(new MessageResponse("La contraseña debe tener 6 caracteres como mínimo y 40 como máximo."));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		
		System.out.println("*********User registered successfully!");
		
		// Login the new user
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(signUpRequest.getUsername(), signUpRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> listOfRoles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		// Create a personal folder which will contain user's files.
		createPersonalDirectory(signUpRequest.getUsername());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 listOfRoles));
		//return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@Valid @RequestBody LogoutRequest LogoutRequest) {
		System.out.println("*********User logged out successfully!");
		return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
	}

	private void createPersonalDirectory(String username){
		new File("uploads/"+username+"/bases de datos").mkdirs();
		new File("uploads/"+username+"/consultas").mkdirs();
	}
}
