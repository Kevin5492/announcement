package com.kevin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kevin.dto.GenericDTO;
import com.kevin.model.Announcements;
import com.kevin.model.Files;
import com.kevin.model.Users;
import com.kevin.repository.AnnouncementsRepository;
import com.kevin.repository.FilesRepository;
import com.kevin.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
public class AnnouncementsService {

	@Autowired
	private AnnouncementsRepository announcementsRepo;

	@Autowired
	private FilesRepository filesRepo;
	
	@Autowired
	private UsersRepository usersRepo;
	@Transactional
	public GenericDTO<Void> createAnnouncement(Integer userId, String title, String content,
			List<MultipartFile> files) {
		try {	
			Optional<Users> userOp = usersRepo.findById(userId);
			if(userOp.isEmpty()) {
				return GenericDTO.error("使用者不存在");
			}
			Announcements announcement = new Announcements();
			announcement.setTitle(title);
			announcement.setContent(content);
			announcement.setUser(userOp.get());
			if (files == null) {
			    files = new ArrayList<>();
			}
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
				    return GenericDTO.error("檔案 " + file.getOriginalFilename() + " 為空，請重新上傳");
				}
				Files fileEntity = new Files(file.getOriginalFilename(), file.getContentType(), file.getBytes());
				announcement.addFile(fileEntity); // ✅ 自動關聯

			}
			announcementsRepo.save(announcement);
			return GenericDTO.success("新增成功", null);
		} catch (IOException e) { // ✅ 只 catch `IOException`
			return GenericDTO.error("檔案處理失敗：" + e.getMessage());
		} catch (Exception e) {
			return GenericDTO.error("新增失敗：" + e.getMessage());
		}

	}

}
