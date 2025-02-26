package com.kevin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kevin.model.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	@Query("from Users u where u.phone = :phone")
    public Users findUserByPhoneNumber(@Param("phone")String phone);
}
