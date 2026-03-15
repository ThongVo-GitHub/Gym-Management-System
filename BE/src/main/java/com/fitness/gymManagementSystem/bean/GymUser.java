package com.fitness.gymManagementSystem.bean;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;


@Entity

public class GymUser extends User{
	
	@Id
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private Long phone_number;
	private String type;
	public GymUser() {
		super("abc","xyz",new ArrayList<>());
		// TODO Auto-generated constructor stub
	}
	
	public GymUser(String email) {
		super("abc","xyz",new ArrayList<>());
		// TODO Auto-generated constructor stub
	}
	
	public GymUser(String username, String password,Collection<? extends GrantedAuthority> authorities,String username2,String password2, String firstName,
			String email, String type,String lastName,Long phone_number) {
		super(username,password,authorities);
		this.username = username2;
		this.password = password2;
		this.firstName = firstName;
		this.lastName= lastName;
		this.email = email;
		this.type = type;
		this.phone_number=phone_number;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public Long getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(Long phone_number) {
		this.phone_number = phone_number;
	}
	@Override
	public String toString() {
		return "GymUser [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", phone_number=" + phone_number + ", type=" + type + "]";
	}
	
	
}
