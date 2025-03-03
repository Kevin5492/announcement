package com.kevin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kevin.dto.AnnouncementsDTO;
import com.kevin.model.Announcements;

public interface AnnouncementsRepository extends JpaRepository<Announcements, Integer> {
	@Query(value = "SELECT new com.kevin.dto.AnnouncementsDTO("
			+ "a.announcementId,"
			+ "a.user.userId, "
			+ "a.user.userName, "
			+ "a.postDate,"
			+ "a.expireDate,"
			+ "a.title,"
			+ "a.content) "
			+ "FROM Announcements a WHERE (a.title LIKE %:keyword% or a.content LIKE %:keyword% )"
			+ "AND a.postDate <= CURRENT_TIMESTAMP "
	        + "AND a.expireDate >= CURRENT_TIMESTAMP"
	        ,
	        countQuery = "SELECT count(a) FROM Announcements a " +
	         "WHERE (a.title LIKE %:keyword% OR a.content LIKE %:keyword%) " +
	         "AND a.postDate <= CURRENT_TIMESTAMP " +
	         "AND a.expireDate >= CURRENT_TIMESTAMP") //顯示所有還在公告時間的公告
    Page<AnnouncementsDTO> searchActiveAnnouncementsByTitle(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("SELECT new com.kevin.dto.AnnouncementsDTO("
			+ "a.announcementId, "
			+ "a.user.userId, "
			+ "a.user.userName, "
			+ "a.postDate,"
			+ "a.expireDate,"
			+ "a.title,"
			+ "a.content) "
			+ "FROM Announcements a WHERE a.announcementId = :announcementId") //顯示單筆公告
	AnnouncementsDTO showAnAnnouncement(@Param("announcementId") Integer announcementId);
	
	@Query("SELECT new com.kevin.dto.AnnouncementsDTO("
			+ "a.announcementId, "
			+ "a.user.userId, "
			+ "a.user.userName, "
			+ "a.postDate,"
			+ "a.expireDate,"
			+ "a.title,"
			+ "a.content) "
			+ "FROM Announcements a WHERE (a.title LIKE %:keyword% or a.content LIKE %:keyword% )"
			+ "AND a.postDate <= CURRENT_TIMESTAMP "
	        + "AND a.expireDate >= CURRENT_TIMESTAMP")//顯示所有公告 測試用
    List<AnnouncementsDTO> searchByTitleList(@Param("keyword") String keyword);
}
