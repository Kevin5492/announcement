package com.kevin.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kevin.dto.AnnouncementsDTO;
import com.kevin.dto.FilesDTO;
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
	
	public GenericDTO<Page<AnnouncementsDTO>> showAllActiveAnnouncements(String search, Integer currentPage) {
		try {
			if (currentPage == null || currentPage < 1) { //避免沒有輸入頁數
				currentPage = 1;
			}
			if (search == null) { //避免搜尋沒有輸入
				search = "";
			}
			
			Pageable pgb = PageRequest.of(currentPage - 1, 10, Sort.Direction.DESC, "postDate");	
			
			List<AnnouncementsDTO> resultList = announcementsRepo.searchByTitleList(search);
			for (AnnouncementsDTO singleDTO :resultList) {
				System.out.println("userName"+singleDTO.getUserName());
				System.out.println("title"+singleDTO.getTitle());
				System.out.println("content"+singleDTO.getContent());
			}
			Page<AnnouncementsDTO> resultPgb = announcementsRepo.searchActiveAnnouncementsByTitle(search, pgb);
			for (AnnouncementsDTO singleDTO :resultPgb.getContent()) {
				System.out.println("Pgb");
				System.out.println("Pgb userName"+singleDTO.getUserName());
				System.out.println("Pgb title"+singleDTO.getTitle());
				System.out.println("Pgb content"+singleDTO.getContent());
			}
			
			return GenericDTO.success("查詢成功", resultPgb);
		} catch (Exception e) {
			e.printStackTrace();
			return GenericDTO.error("查詢失敗：" + e.getMessage());
		}
	}
	
	public GenericDTO<AnnouncementsDTO> showAnAnnouncement(Integer announcementId){ //顯示一個完逞的公告
		
		try {
			AnnouncementsDTO announcement = announcementsRepo.showAnAnnouncement(announcementId);
			List<FilesDTO> files = filesRepo.showAnnouncemnetFiles(announcementId);
			if (files!=null && files.size()!=0) {
				announcement.setFiles(files);
			}
			return GenericDTO.success("查詢成功", announcement);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return GenericDTO.error("查詢失敗：" + e.getMessage());
		}
	}

	@Transactional // 新增一項公告
	public GenericDTO<Void> createAnnouncement(Integer userId, String title, String content,
			Date postDate,Date expireDate,
			List<MultipartFile> files) {
		try {
			Optional<Users> userOp = usersRepo.findById(userId);
			if (userOp.isEmpty()) {
				return GenericDTO.error("使用者不存在");
			}
			Announcements announcement = new Announcements();
			announcement.setTitle(title);
			announcement.setContent(content);
			announcement.setPostDate(postDate);
			announcement.setExpireDate(expireDate);
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
			Date postDate,Date expireDate,
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
			announcement.setTitle(title);
			announcement.setContent(content);
			announcement.setPostDate(postDate);
			announcement.setExpireDate(expireDate);
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
