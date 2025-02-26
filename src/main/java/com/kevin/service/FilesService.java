package com.kevin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kevin.repository.FilesRepository;

@Service
public class FilesService {

	@Autowired
	private FilesRepository filesRepo;
	
	
}
