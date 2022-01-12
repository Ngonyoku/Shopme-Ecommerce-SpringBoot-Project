package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Runs the tests on the real database
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);  //Pulls a specific row from the database
		
		User userMorio= new User("morio@example.com", "123456789", "Morio", "Anzenza");
		userMorio.addRole(roleAdmin);
		
		User savedUser = repository.save(userMorio);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userCate = new User("cate@example.com", "123456", "Cate", "Wanjiru");
		Role roleEditor = new Role(4);
		Role roleAssistant= new Role(2);
		userCate.addRole(roleAssistant);
		userCate.addRole(roleEditor);
		
		User savedUser = repository.save(userCate);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repository.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userMorio = repository.findById(1).get();
		System.out.println(userMorio);
		assertThat(userMorio).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userMorio = repository.findById(1).get();
		userMorio.setEnabled(true);
		userMorio.setEmail("morio.fulani@example.com");
		
		repository.save(userMorio);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userCate= repository.findById(2).get();
		Role roleSalesPerson= new Role(3);
		Role roleEditor= new Role(4);
		
		userCate.getRoles().remove(roleEditor);
		userCate.addRole(roleSalesPerson);
		
		repository.save(userCate);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repository.deleteById(userId);
	}
}
