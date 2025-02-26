package com.kevin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kevin.dto.AnnouncementsDTO;
import com.kevin.model.Announcements;

public interface AnnouncementsRepository extends JpaRepository<Announcements, Integer> {
	@Query("SELECT new com.kevin.dto.AnnouncementsDTO("
			+ "a.announcementId, "
			+ "a.user.userName, "
			+ "a.postDate,"
			+ "a.expireDate,"
			+ "a.title,"
			+ "a.content) "
			+ "FROM Announcements a WHERE a.title LIKE %:keyword%")//顯示所有公告
    Page<AnnouncementsDTO> searchByTitle(@Param("keyword") String keyword, Pageable pageable);
}
