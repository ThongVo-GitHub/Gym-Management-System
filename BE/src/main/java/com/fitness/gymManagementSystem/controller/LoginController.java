package com.fitness.gymManagementSystem.controller;


import com.fitness.gymManagementSystem.bean.GymUser;
import com.fitness.gymManagementSystem.service.GymUserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private GymUserService service;
	
	
	
	@GetMapping("/register")
	public ModelAndView showUserRegisterPage() {
		GymUser user = new GymUser();
		ModelAndView mv = new ModelAndView("newUserRegistration");
		mv.addObject("userRecord",user);
		return mv;
	}
	
	@PostMapping("/register")
	public ModelAndView saveUserRegisterPage(@ModelAttribute("userRecord") GymUser user) {
		GymUser gymUser = new GymUser();
		String encodedPassword = bcrypt.encode(user.getPassword());
		gymUser.setFirstName(user.getFirstName());
		gymUser.setLastName(user.getLastName());
		gymUser.setType(user.getType());
		gymUser.setEmail(user.getEmail());
		gymUser.setUsername(user.getUsername());
		gymUser.setPhone_number(user.getPhone_number());
		gymUser.setPassword(encodedPassword);
		service.save(gymUser);
		
		// Redirect to verification page after successful registration
		return new ModelAndView("loginPage");
	}
	
	@GetMapping("/loginpage")
	public ModelAndView showLoginPage() {
		return new ModelAndView("loginPage");
	}
	
	@GetMapping("/loginerror")
	public ModelAndView showLoginErrorPage() {
		return new ModelAndView("loginErrorPage");
	}
}
