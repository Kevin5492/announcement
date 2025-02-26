package com.kevin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class AppCobfig {
	@Bean
	PasswordEncoder passwordEncoder() { // 加密輸入的密碼
		return new BCryptPasswordEncoder();
	}
}
