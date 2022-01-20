package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	/*
	 * Returns a list of all users in the database
	 * Returns the users.html file
	 * */
	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers",listUsers);
		return "users";
	}
	
	/*
	 * Return the user_form.html file
	 * */
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles(); //Get the list of roles	
		User user = new User(); //The instance will bind this object to the user form
		
		user.setEnabled(true);
			
		model.addAttribute("user", user);
		model.addAttribute("listRoles",listRoles);
		return "user_form";
	}
	
	/*
	 * Saves the User to the database
	 * */
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes) {
		service.save(user); //Save user to the database
		
		redirectAttributes.addFlashAttribute("message", "User saved successfully");
		return "redirect:/users"; //Redirect the user to "/users" page
	}
}