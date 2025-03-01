package com.kevin.service;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kevin.dto.GenericDTO;
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
	public GenericDTO<Void> registerAUser(String userName, String phone, String password) {
		 
		
		try {
			String phonePattern = "^09\\d{8}$"; // 驗證手機格式
			
			if(!Pattern.matches(phonePattern, phone)) {
				return new GenericDTO<Void>(false,"手機號碼格式錯誤",null);
			}
			
	        //驗證密碼（8-20 字元，包含大小寫、數字、特殊符號）
			String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$";
		    if (!Pattern.matches(passwordPattern, password)) {
		    	return new GenericDTO<Void>(false,"密碼須包含大小寫字母、數字、特殊符號，長度 8-20",null);
		    }

		    // 驗證名稱（僅限中英文、數字，長度 2-20）
		    String namePattern = "^[\\p{L}\\p{N}]{2,20}$";
		    if (!Pattern.matches(namePattern, userName)) {
		    	return new GenericDTO<Void>(false, "名稱只能包含中英文、數字，長度 2-20",null);
		    }
			if (usersRepo.findUserByPhoneNumber(phone) != null) {//先確定該號碼有沒有註冊過
				return new GenericDTO<Void>(false, "該號碼已存在",null);
			}
			Users user = new Users();
			user.setUserName(userName);
			user.setPassword(pwdEncoder.encode(password));
			user.setCreationDate(new Date());
			user.setPhone(phone);
			usersRepo.save(user);
			System.out.println("有成功執行");
			return new GenericDTO<Void>(true,"帳號新增成功，請使用該帳號登入",null);
		} catch (DataIntegrityViolationException e) { // 如果插入時遇到違反資料庫 Unique Key 回傳號碼已存在
			return new GenericDTO<Void>(false, "該號碼已存在",null);
		}catch (Exception e) {
			e.printStackTrace();
			return new GenericDTO<Void>(false, "帳號新增失敗，請重新嘗試",null);
		}

	}
	public GenericDTO<Integer> checkUserPassword(String phone,String password) {
		try {
			Users user = usersRepo.findUserByPhoneNumber(phone);
			
			if (user == null) {  //先確定有沒有用該手機註冊的使用者
				return new GenericDTO<Integer>(false, "該使用者不存在",null);
			}
			user.getPassword();
			if(pwdEncoder.matches(password, user.getPassword())) {
				return new GenericDTO<Integer>(true, user.getUserName(),user.getUserId());
			}
			return new GenericDTO<Integer>(false, "密碼錯誤，請重新嘗試",null);
		}catch (Exception e) {
			e.printStackTrace();
			return new GenericDTO<Integer>(false, "發生錯誤，登入失敗",null);
		}
		
	}
}
