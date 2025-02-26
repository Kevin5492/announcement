package com.kevin.dto;

import java.util.Date;

public record AnnouncementsDTO(Integer announcementId, String userName, Date postDate, Date expireDate, String title,
		String content) {

}
