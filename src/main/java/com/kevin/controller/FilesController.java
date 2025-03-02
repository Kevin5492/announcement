package com.kevin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kevin.dto.FilesDTO;
import com.kevin.service.FilesService;

@Controller
@RequestMapping("/files")
public class FilesController {
	@Autowired FilesService filesService;

	@GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer fileId) {
        // 取得檔案
        FilesDTO file = filesService.getFileById(fileId);
        if (file == null || file.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok() //點擊之後就會直接下載
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .contentType(MediaType.valueOf(file.getFileType()))
                .body(file.getFileData());
    }
}
