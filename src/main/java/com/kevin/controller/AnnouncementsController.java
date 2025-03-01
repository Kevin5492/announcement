package com.kevin.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kevin.dto.AnnouncementsDTO;
import com.kevin.dto.GenericDTO;
import com.kevin.service.AnnouncementsService;
import com.kevin.util.XSSFilter;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@Controller
@RequestMapping("/announcements")
public class AnnouncementsController {
	@Autowired
	private AnnouncementsService announcementsService;

	@GetMapping
	public String showAllActiveAnnouncements(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "1") Integer currentPage, Model model) {

		GenericDTO<Page<AnnouncementsDTO>> result = announcementsService.showAllActiveAnnouncements(search, currentPage);
		Page<AnnouncementsDTO> resultPgb = result.getData();
		if (result.isSuccess()) { // 如果成功
			model.addAttribute("announcements", resultPgb.getContent());
			model.addAttribute("search", search);
			model.addAttribute("currentPage", currentPage);
			model.addAttribute("totalPages", resultPgb.getTotalPages());
			model.addAttribute("totalElements", resultPgb.getTotalElements());

		} else {
			model.addAttribute("success", false);
			model.addAttribute("mssg", result.getMessage());
		}
		return "announcements/allAnnouncements";

	}

	@GetMapping("/create") // 用來跳轉到發布公告頁面
	public String showCreateForm(HttpSession session,RedirectAttributes redirectAttributes) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您並未登入帳號");
			return "redirect:/login";
		}
		return "announcements/createAnnouncement";
	}
	
	@GetMapping("/show/{id}")
	public String showAnAnnouncement(@PathVariable("id") Integer announcementId,HttpSession session,Model model,RedirectAttributes redirectAttributes) { // 用來跳轉到詳細的公告頁面
		GenericDTO<AnnouncementsDTO> responseDTO = announcementsService.showAnAnnouncement(announcementId);
		if (responseDTO.isSuccess()) {
			model.addAttribute("announcement", responseDTO.getData());
			return "announcements/showAnAnnouncement";
		}
		redirectAttributes.addFlashAttribute("success", false);
		redirectAttributes.addFlashAttribute("mssg", responseDTO.getMessage());
		return "redirect:/announcements";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditAnnouncementPage(@PathVariable("id") Integer announcementId,HttpSession session, Model model,RedirectAttributes redirectAttributes) { // 用來跳轉到修改公告頁面
		Integer userId = (Integer) session.getAttribute("userId");
		
		if (userId == null) {
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您並未登入帳號");
			return "redirect:/login";
		}
		
		GenericDTO<AnnouncementsDTO> responseDTO = announcementsService.showAnAnnouncement(announcementId);
		if (!responseDTO.isSuccess() || responseDTO.getData() == null) {
		    redirectAttributes.addFlashAttribute("success", false);
		    redirectAttributes.addFlashAttribute("mssg", "公告不存在或已被刪除");
		    return "redirect:/announcements";
		}
		if (!responseDTO.getData().getUserId().equals(userId)) { // 確認userId 與該公告相同
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您無權修改該公告");
			return "redirect:/announcements";
		}
		if (responseDTO.isSuccess()) {
			model.addAttribute("announcement", responseDTO.getData());
			return "announcements/editAnAnnouncement";
		}
		redirectAttributes.addFlashAttribute("success", false);
		redirectAttributes.addFlashAttribute("mssg", responseDTO.getMessage());
		return "redirect:/announcements";
	}

	@PostMapping("/create")
	public String createAnnouncement(@RequestParam String title, @RequestParam String content,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date postDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expireDate,
			@RequestParam(required = false) List<MultipartFile> files, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			
			return "redirect:/login";
		}
		if (title.trim().isEmpty() || content.trim().isEmpty()) {
		    redirectAttributes.addFlashAttribute("success", false);
		    redirectAttributes.addFlashAttribute("mssg", "標題與內容不可為空");
		    return "redirect:/announcements/create";
		}
		if (expireDate.before(postDate)) {
		    redirectAttributes.addFlashAttribute("success", false);
		    redirectAttributes.addFlashAttribute("mssg", "公告的結束日期不能早於發布日期");
		    return "redirect:/announcements/create";
		}

		GenericDTO<Void> result = announcementsService.createAnnouncement(userId, XSSFilter.sanitize(title) /*避免XSS*/, XSSFilter.sanitize(content)/*避免XSS*/, postDate, expireDate,
				files);
		redirectAttributes.addFlashAttribute("success", result.isSuccess());
		redirectAttributes.addFlashAttribute("mssg", result.getMessage());

		return "redirect:/announcements";
	}
	

	@PostMapping("/edit")
	public String editAnAnnouncement(@RequestParam Integer announcementId,@RequestParam  String title,@RequestParam  String content,    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date postDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expireDate, HttpSession session, @RequestParam(required = false) List<Integer> fileIdsToDelete,
			@RequestParam(required = false) List<MultipartFile> files,RedirectAttributes redirectAttributes) {
		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/login";
		}
		if (expireDate.before(postDate)) {
		    redirectAttributes.addFlashAttribute("success", false);
		    redirectAttributes.addFlashAttribute("mssg", "公告的結束日期不能早於發布日期");
		    if (announcementId != null) {
		        return "redirect:/announcements/edit/" + announcementId;
		    }
		    return "redirect:/announcements";
		}
		GenericDTO<Void> result = announcementsService.updateAnnouncement(userId, announcementId,  XSSFilter.sanitize(title) /*避免XSS*/, XSSFilter.sanitize(content)/*避免XSS*/, postDate, expireDate, fileIdsToDelete, files);
		redirectAttributes.addFlashAttribute("success", result.isSuccess());
		redirectAttributes.addFlashAttribute("mssg", result.getMessage());
		return "redirect:/announcements";
	}
	@PostMapping("/delete")
	public String deleteAnAnnouncement(@RequestParam Integer announcementId,HttpSession session,RedirectAttributes redirectAttributes) {
		Integer userId = (Integer) session.getAttribute("userId");
		
		if (userId == null) {
			redirectAttributes.addFlashAttribute("success", false);
			redirectAttributes.addFlashAttribute("mssg", "您並未登入帳號");
			return "redirect:/login";
		}
		System.out.println("有呼叫");
		GenericDTO<Void> result = announcementsService.deleteAnnouncement(userId, announcementId);
		redirectAttributes.addFlashAttribute("success", result.isSuccess());
		redirectAttributes.addFlashAttribute("mssg", result.getMessage());
		return "redirect:/announcements";
	}
}
