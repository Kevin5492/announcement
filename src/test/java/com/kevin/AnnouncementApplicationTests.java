package com.kevin;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kevin.dto.GenericDTO;
import com.kevin.service.AnnouncementsService;
import com.kevin.service.UsersService;

@SpringBootTest
class AnnouncementApplicationTests {
	@Autowired
	private UsersService usersService;
	@Autowired 
	private AnnouncementsService announcementsService;
	@Test
	void contextLoads() {
//		System.out.println(usersService.registerAUser("小明","0987654321", "123"));
//		System.out.println(usersService.checkUserPassword("0987654321","123"));
		GenericDTO <Void> result = announcementsService.createAnnouncement(1, "測試", "測試", new ArrayList<>());
		System.out.println(result);
		System.out.println(result.isSuccess());
		System.out.println(result.getMessage());
	}
	

}
