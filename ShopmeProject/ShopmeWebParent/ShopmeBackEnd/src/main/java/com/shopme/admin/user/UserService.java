package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
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
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null); //True if its in Updating Mode
		
		if	(isUpdatingUser) {
			User existingUser = userRepository.findById(user.getId()).get();
			//Check if password is Empty or NOT
			if	(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword()); //Set existing password
			} else {
				encodePasword(user);
			}
		} else {
			encodePasword(user);
		}

		return userRepository.save(user);
	}
	
	public void encodePasword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		//Updated the User password with the new encoded password
		user.setPassword(encodedPassword);	
	}
	
	/*
	 * Confirms that the email passed DOES indeed exist in the database
	 * */
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepository.getUserByEmail(email);
		if (userByEmail == null) {
			return true;
		}
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if	(userByEmail != null) return false;
		} else {
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		userRepository.updateEnabledStatus(id, enabled);
	}

	/*
	 * Return an instance of user with the id provided
	 * */
	public User get(Integer id) throws UserNotFoundException{
		try {
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could NOT find user with id: " + id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could NOT find user with id: " + id);
		}
		userRepository.deleteById(id);
	}
}
