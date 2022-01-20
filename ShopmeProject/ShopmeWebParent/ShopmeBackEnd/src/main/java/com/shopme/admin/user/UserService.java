package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/*
	 * Returns a list of all users
	 * */
	public List<User> listAll() {
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}
	
	/*
	 * Save User Data to the database
	 * */
	public void save(User user) {
		encodePasword(user);
		userRepository.save(user);
	}
	
	public void encodePasword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		//Updated the User password with the new encoded password
		user.setPassword(encodedPassword);
		
	}
}
