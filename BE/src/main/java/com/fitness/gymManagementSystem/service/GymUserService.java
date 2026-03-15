package com.fitness.gymManagementSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fitness.gymManagementSystem.bean.GymUser;
import com.fitness.gymManagementSystem.dao.GymUserRepository;
@Service
public class GymUserService implements UserDetailsService {
	@Autowired
	private GymUserRepository repository;
	private String type;
	private GymUser users;
	
	public void save(GymUser user) {
		repository.save(user);
	}

	public String getType() {
		return type;
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//	 users=repository.findById(username).get();
//		type=users.getType();
//		return users;
//	}
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Optional<GymUser> optionalUser = repository.findById(username);
	        if (optionalUser.isPresent()) {
	        	users=repository.findById(username).get();
	    		type=users.getType();
	    		return users;
	        } else {
	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }
	    }
	public GymUser getUser() {
		return users;
	}
	public List<String> getAllCustomers(){
		return repository.findAllCustomerUsers();
	}
	
	
	public void removeItem(String username) {
		repository.deleteById(username);
	}
	
}
