package com.kevin.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
public class Files {
	@Id
	@Column(name = "file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fileId;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_type")
	private String fileType;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] fileData;
	
	@CreationTimestamp
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcements announcement;
    
    public Files(String fileName, String fileType, byte[] fileData) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

    public Files() {}
}
