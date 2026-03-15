package com.fitness.gymManagementSystem.bean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Feedback {
	@Id
    private String username;
	private String email;
    private String name;
    @Lob
    private String content;
	public Feedback() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Feedback(String username, String email, String name, String content) {
		super();
		this.username = username;
		this.email = email;
		this.name = name;
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
