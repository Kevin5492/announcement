package com.kevin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FilesDTO {
	private Integer fileId;
	private String fileName;
	private String fileType;
	private byte[] fileData;

	public FilesDTO(Integer fileId, String fileName, String fileType, byte[] fileData) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileData = fileData;
	}

	public FilesDTO(Integer fileId, String fileName, String fileType) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileData = null;
	}

}
