package com.kevin.dto;

import org.springframework.data.domain.Page;

public record AnnouncementsReponseDTO (boolean success, String mssg,Page<AnnouncementsDTO> announcements) {
	
}
