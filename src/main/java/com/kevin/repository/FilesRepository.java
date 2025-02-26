package com.kevin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevin.model.Files;

public interface FilesRepository extends JpaRepository<Files, Integer> {

}
