package com.example.demo.payload.request;

import javax.validation.constraints.NotBlank;

public class LogoutRequest {
	@NotBlank
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
