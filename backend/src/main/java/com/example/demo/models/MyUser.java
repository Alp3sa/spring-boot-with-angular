package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.stereotype.Component;

@Entity(name = "myuser")
@Component
public class MyUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String username;
    private String password;

	public MyUser() {
	}

	public MyUser(Long id) {
		this.setId(id);
	}

	public MyUser(String user, String password) {
        this.username=username;
        this.password=password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}