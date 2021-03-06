package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)  //Test will performed on the read datasource
@Rollback(false)  //Changes will NOT be erased
public class RoleRepositoryTests {

	@Autowired
	private RoleRepository repository;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Manage everything");
		Role savedRole = repository.save(roleAdmin);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleEditor= new Role("Editor", "Manages Brands, categories, products, articles and menus");
		Role roleShipper= new Role("Shipper", "View Products, view orders and update order status");
		Role roleSalesPerson= new Role("Salesperson", "Manage Products price, customers, shipping, orders and sales reports");
		Role roleAssistant= new Role("Assistant", "Manage questions and reviews all products");
		
		repository.saveAll(List.of(roleAssistant, roleSalesPerson, roleEditor, roleShipper));	
	}
}
