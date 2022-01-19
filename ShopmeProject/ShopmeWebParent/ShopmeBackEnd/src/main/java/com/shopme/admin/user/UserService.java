package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	/*
	 * Returns a list of all users
	 * */
	public List<User> listAll() {
		return (List<User>) repository.findAll();
	}
}
