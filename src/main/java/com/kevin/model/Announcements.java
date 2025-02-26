package com.kevin.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "announcements")
public class Announcements {
	@Id
	@Column(name = "announcement_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer announcementId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Users user;
	
	@Column(name = "post_date")
	private Date postDate;
	
	@Column(name = "expire_date")
	private Date expireDate;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content")
	private String content;
	
	@OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> files = new ArrayList<>(); 
	
	public void addFile(Files file) {
        files.add(file);
        file.setAnnouncement(this);
    }
}
