package com.kevin;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.kevin.dto.AnnouncementsDTO;
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
//		GenericDTO <Void> result = announcementsService.createAnnouncement(1, "測試", "測試", new ArrayList<>());
//		System.out.println(result);
//		System.out.println(result.isSuccess());
//		System.out.println(result.getMessage());
//		announcementsService.showAllAnnouncements(null,null);
		Page<AnnouncementsDTO> resultPgb = (Page<AnnouncementsDTO>)announcementsService.showAllActiveAnnouncements(null,null).getData();
//		System.out.println("content"+resultPgb.getContent());
//		System.out.println("size"+resultPgb.getContent().size());
		List<AnnouncementsDTO> resultList = resultPgb.getContent();
		for (AnnouncementsDTO singleDTO :resultList) {
			System.out.println("Test userName"+singleDTO.getUserName());
			System.out.println("Test title"+singleDTO.getTitle());
			System.out.println("Test content"+singleDTO.getContent());
		}

	}
	

}
