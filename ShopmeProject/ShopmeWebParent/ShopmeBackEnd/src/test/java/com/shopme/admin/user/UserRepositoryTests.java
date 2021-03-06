package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

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
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);  //Pulls a specific row from the database
		
		User userMorio= new User("morio@example.com", "123456789", "Morio", "Anzenza");
		userMorio.addRole(roleAdmin);
		
		User savedUser = repo.save(userMorio);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userCate = new User("cate@example.com", "123456", "Cate", "Wanjiru");
		Role roleEditor = new Role(4);
		Role roleAssistant= new Role(2);
		userCate.addRole(roleAssistant);
		userCate.addRole(roleEditor);
		
		User savedUser = repo.save(userCate);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userMorio = repo.findById(1).get();
		System.out.println(userMorio);
		assertThat(userMorio).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userMorio = repo.findById(1).get();
		userMorio.setEnabled(true);
		userMorio.setEmail("morio.fulani@example.com");
		
		repo.save(userMorio);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userCate= repo.findById(2).get();
		Role roleSalesPerson= new Role(3);
		Role roleEditor= new Role(4);
		
		userCate.getRoles().remove(roleEditor);
		userCate.addRole(roleSalesPerson);
		
		repo.save(userCate);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 1;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "dc65963HD@xys.cmm"; //No such email exists in database.
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull(); //The test MUST fail since there is no user with the above email
	}
	
	@Test
	public void testCountById() {
		Integer id = 5; //Use ID that does NOT exist in database
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, true);
	}
}
