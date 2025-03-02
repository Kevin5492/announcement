package com.kevin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kevin.dto.FilesDTO;
import com.kevin.model.Files;

public interface FilesRepository extends JpaRepository<Files, Integer> {
	@Query("SELECT new com.kevin.dto.FilesDTO("
			+ "f.fileId, "
			+ "f.fileName, "
			+ "f.fileType)"
			+ "from Files f "
			+ "where f.announcement.announcementId = :announcementId")
	List<FilesDTO> showAnnouncemnetFiles(@Param("announcementId") Integer announcementId);	
	
	@Query("SELECT new com.kevin.dto.FilesDTO("
			+ "f.fileId, "
			+ "f.fileName, "
			+ "f.fileType, "
			+ "f.fileData )"
			+ "from Files f "
			+ "where f.fileId= :fileId")
	FilesDTO showFileById(@Param("fileId") Integer fileId);	

}
