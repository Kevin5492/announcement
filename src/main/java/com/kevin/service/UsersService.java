package com.kevin.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevin.dto.UsersReponseDTO;
import com.kevin.model.Users;
import com.kevin.repository.UsersRepository;

import jakarta.transaction.Transactional;
@Service 
public class UsersService {
	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private UsersRepository usersRepo;

	@Transactional
	public UsersReponseDTO registerAUser(String userName, String phone, String password) {
		 
		
		try {
			if (usersRepo.findUserByPhoneNumber(phone) != null) {//先確定該號碼有沒有註冊過
				return new UsersReponseDTO(false, "該號碼已存在");
			}
			Users user = new Users();
			user.setUserName(userName);
			user.setPassword(pwdEncoder.encode(password));
			user.setCreationDate(new Date());
			user.setPhone(phone);
			usersRepo.save(user);
			System.out.println("有成功執行");
			return new UsersReponseDTO(true, "帳號新增成功");
		} catch (DataIntegrityViolationException e) {
			return new UsersReponseDTO(false, "該號碼已存在");
		}catch (Exception e) {
			e.printStackTrace();
			return new UsersReponseDTO(false, "帳號新增失敗，請重新嘗試");
		}

	}
	public UsersReponseDTO checkUserPassword(String phone,String password) {
		try {
			Users user = usersRepo.findUserByPhoneNumber(phone);
			
			if (user == null) {  //先確定有沒有用該手機註冊的使用者
				return new UsersReponseDTO(false, "該使用者不存在");
			}
			user.getPassword();
			if(pwdEncoder.matches(password, user.getPassword())) {
				return new UsersReponseDTO(true, "密碼正確");
			}
			return new UsersReponseDTO(false, "密碼錯誤，請重新嘗試");
		}catch (Exception e) {
			e.printStackTrace();
			return new UsersReponseDTO(false, "發生錯誤，登入失敗");
		}
		
	}
}
