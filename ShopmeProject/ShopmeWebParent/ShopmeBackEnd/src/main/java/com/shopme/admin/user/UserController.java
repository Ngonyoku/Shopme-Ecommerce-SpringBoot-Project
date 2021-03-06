package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
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
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}
	
	/*
	 * Saves the User to the database
	 * */
	@PostMapping("/users/save")
	public String saveUser(
		User user, 
		RedirectAttributes redirectAttributes,
		@RequestParam("image") MultipartFile multipartFile
	) throws IOException {
		if (!multipartFile.isEmpty()) {//Check if the form has a file upload
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
	
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if(user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			service.save(user); //Save user to the database
		}
		
		redirectAttributes.addFlashAttribute("message", "User saved successfully");
		return "redirect:/users"; //Redirect the user to "/users" page
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(
		@PathVariable("id") Integer id,
		Model model,
		RedirectAttributes redirectAttributes
	) {
		try {
			User user = service.get(id); //Returns user with the ID provided
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user",user); //Return user object back to the UI
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID: " + user.getId() + ")");
			return "user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(
			@PathVariable("id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes
	) {
		try {
			service.delete(id); //Delete user from database
			redirectAttributes.addFlashAttribute("message", "User with ID: " + id + " has been deleted successfully!");
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(
		@PathVariable("id") Integer id,
		@PathVariable("status") boolean enabled,
		RedirectAttributes redirectAttributes
	) {
		service.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "Enabled" : "Disabled";
		String message = "User with id " + id + " has been " + status;
		
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
}
