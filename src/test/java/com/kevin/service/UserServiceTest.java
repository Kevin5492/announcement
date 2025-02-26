package com.kevin.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kevin.service.UsersService;
@SpringBootTest()
@ExtendWith(SpringExtension.class) 
public class UserServiceTest {
	
	@Autowired
	private UsersService usersService;
	@Test
	void testSomething() {
		usersService.registerAUser("小明","0987654321", "123");
	}

}
