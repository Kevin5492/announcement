package com.kevin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kevin.dto.GenericDTO;
import com.kevin.dto.UsersReponseDTO;
import com.kevin.service.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class UsersController {

	@Autowired
	UsersService usersService;

	@GetMapping("/login")
	public String loginPage(HttpSession session, RedirectAttributes redirectAttributes) { // 跳轉到登入頁面
		if (session.getAttribute("userId") != null) {
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您已登入帳號，若要切換帳號請先登出");
			return "redirect:/announcements";
		}
		return "users/login";
	}

	@GetMapping("/createAccount")
	public String createUserPage(HttpSession session, RedirectAttributes redirectAttributes) { // 跳轉到創建帳號頁面

		if (session.getAttribute("userId") != null) {
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您已登入帳號，若要創建帳號請先登出");
			return "redirect:/announcements";
		}
		return "users/create";
	}

	@PostMapping("/logout")
	public String userLogout(HttpSession session, RedirectAttributes redirectAttributes) { // 用來接收登出請求
		if (session.getAttribute("userId") != null) {
			session.invalidate();
			redirectAttributes.addFlashAttribute("success", true);
			redirectAttributes.addFlashAttribute("mssg", "您已成功登出");
			return "redirect:/login";
		}
		redirectAttributes.addFlashAttribute("success", false);
		redirectAttributes.addFlashAttribute("mssg", "您並未登入");
		return "redirect:/announcements";
	}

	@PostMapping("/createAccount")
	public String createAnUser(String userName, String phone, String password, Model model, // 用來接收創建帳號請求
			RedirectAttributes redirectAttributes) {
		GenericDTO<Void> usersResponse = usersService.registerAUser(userName, phone, password);
		redirectAttributes.addFlashAttribute("success", usersResponse.isSuccess());
		redirectAttributes.addFlashAttribute("mssg", usersResponse.getMessage());

		if (usersResponse.isSuccess()) {

			return "redirect:/login";
		}
		return "redirect:/createAccount";
	}
	@PostMapping("/loginAccount")
	public String userLogin (HttpSession session, String phone, String password,RedirectAttributes redirectAttributes) { // 用來接收登入請求
		GenericDTO<Integer> usersResponse = usersService.checkUserPassword(phone, password);
		if (usersResponse.isSuccess()) {
			redirectAttributes.addFlashAttribute("success", true);
			redirectAttributes.addFlashAttribute("mssg", "您已成功登入");
			session.setAttribute("userId",usersResponse.getData());
			session.setAttribute("userName",usersResponse.getMessage()); // userName 放在回應的 message
			return "redirect:/announcements";
		}
		redirectAttributes.addFlashAttribute("success",usersResponse.isSuccess());
		redirectAttributes.addFlashAttribute("mssg", usersResponse.getMessage());
		return "redirect:/login";
	}
}
