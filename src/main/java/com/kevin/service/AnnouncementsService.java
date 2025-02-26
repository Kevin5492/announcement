package com.kevin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kevin.dto.AnnouncementsDTO;
import com.kevin.dto.GenericDTO;
import com.kevin.model.Announcements;
import com.kevin.model.Files;
import com.kevin.model.Users;
import com.kevin.repository.AnnouncementsRepository;
import com.kevin.repository.FilesRepository;
import com.kevin.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;

@Service
public class AnnouncementsService {

	@Autowired
	private AnnouncementsRepository announcementsRepo;

	@Autowired
	private FilesRepository filesRepo;

	@Autowired
	private UsersRepository usersRepo;

	public GenericDTO<Page<AnnouncementsDTO>> showAllAnnouncements(String title, Integer currentPage) {
		try {
			if (currentPage == null) {
				currentPage = 1;
			}
			Pageable pgb = PageRequest.of(currentPage - 1, 10, Sort.Direction.DESC, "postDate");
			;
			return GenericDTO.success("查詢成功", announcementsRepo.searchByTitle(title, pgb));
		} catch (Exception e) {
			e.printStackTrace();
			return GenericDTO.error("查詢失敗：" + e.getMessage());
		}
	}

	@Transactional // 新增一項公告
	public GenericDTO<Void> createAnnouncement(Integer userId, String title, String content,
			List<MultipartFile> files) {
		try {
			Optional<Users> userOp = usersRepo.findById(userId);
			if (userOp.isEmpty()) {
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
				announcement.addFile(fileEntity); // 把file 加進announcement 中
			}
			announcementsRepo.save(announcement);
			return GenericDTO.success("新增成功", null);
		} catch (IOException e) { 
			return GenericDTO.error("檔案處理失敗：" + e.getMessage());
		} catch (Exception e) {
			return GenericDTO.error("新增失敗：" + e.getMessage());
		}

	}

	@Transactional
	public GenericDTO<Void> updateAnnouncement(Integer userId, Integer announcementId, String title, String content,
			List<Integer> fileIdsToDelete, List<MultipartFile> newFiles) {
		try {
			Optional<Announcements> announcementOp = announcementsRepo.findById(announcementId);
			if (announcementOp.isEmpty()) { // 檢查公告是不是不存在
				return GenericDTO.error("公告不存在");
			}
			Announcements announcement = announcementOp.get(); // 檢查是不是正確的使用者
			if (!announcement.getUser().getUserId().equals(userId)) {
				return GenericDTO.error("無權修改此公告");
			}
			if (fileIdsToDelete != null && !fileIdsToDelete.isEmpty()) { // 把指定的檔案都刪掉
				List<Files> filesToRemove = filesRepo.findAllById(fileIdsToDelete);
				if (filesToRemove.isEmpty()) {
					return GenericDTO.error("要刪除的檔案不存在");
				}
				filesRepo.deleteAll(filesToRemove);
			}
			if (newFiles != null) { // 上傳所有新增的檔案
				for (MultipartFile file : newFiles) {
					if (file.isEmpty()) {
						return GenericDTO.error("檔案 " + file.getOriginalFilename() + " 為空，請重新上傳");
					}
					Files fileEntity = new Files(file.getOriginalFilename(), file.getContentType(), file.getBytes());
					announcement.addFile(fileEntity);
				}
			}
			announcementsRepo.save(announcement);

			return GenericDTO.success("公告更新成功", null);

		} catch (IOException e) {
			return GenericDTO.error("檔案處理失敗：" + e.getMessage());
		} catch (Exception e) {
			return GenericDTO.error("新增失敗：" + e.getMessage());
		}
	}

	@Transactional
	public GenericDTO<Void> deleteAnnouncement(Integer userId, Integer announcementId) {
		try {
			Optional<Announcements> announcementOp = announcementsRepo.findById(announcementId);
			if (announcementOp.isEmpty()) { // 檢查公告是不是不存在
				return GenericDTO.error("公告不存在");
			}
			Announcements announcement = announcementOp.get(); // 檢查是不是正確的使用者
			if (!announcement.getUser().getUserId().equals(userId)) {
				return GenericDTO.error("無權修改此公告");
			}
//			List<Files> announcementFiles = announcement.getFiles();   //手動刪除每個關連到 announcement的file
//			filesRepo.deleteAll(announcementFiles);
			announcementsRepo.delete(announcement);
			return GenericDTO.success("公告刪除成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return GenericDTO.error("刪除失敗：" + e.getMessage());
		}
	}
}
