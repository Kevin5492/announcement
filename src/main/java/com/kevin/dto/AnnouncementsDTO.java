package com.kevin.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementsDTO {
	
	private Integer announcementId;
	private Integer userId;
	private String userName;
	private Date postDate;
	private Date expireDate;
	private String title;
	private String content;
	private List<FilesDTO> files;

	public AnnouncementsDTO(Integer announcementId,Integer userId, String userName, Date postDate, Date expireDate, String title,
			String content) {
		this.userId = userId;
		this.announcementId = announcementId;
		this.userName = userName;
		this.postDate = postDate;
		this.expireDate = expireDate;
		this.title = title;
		this.content = content;
		this.files = new ArrayList<>(); // 預設為空列表，避免 null
	}

}
