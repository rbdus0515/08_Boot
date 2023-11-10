package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/member")
@Controller
@SessionAttributes({"loginMember"})
public class MemberController {
	
	@Autowired
	private MemberService service;

	@PostMapping("/login")
	public String login(Member inputMember, Model model,
						@RequestHeader("referer") String referer,
						RedirectAttributes ra,
						@RequestParam(value="saveId", required = false) String saveId,
						HttpServletResponse resp
						) {
		
		
		Member loginMember = service.login(inputMember);
		
		String path = "redirect:";
		
		if(loginMember != null) { 	// 로그인 성공시
			path += "/";  // 메인페이지로 리다이렉트
			
			model.addAttribute("loginMember", loginMember);
			
			// 쿠키 생성(해당 쿠키에 담을 데이터를 k:v 로 지정)
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			if(saveId != null) { // 체크가 되었을 때
				
				// 한 달(30일) 동안 유지되는 쿠키 생성
				cookie.setMaxAge(60*60*24*30); // 초단위 지정
				
				
			} else { // 체크가 안되었을 때 
				
				// 0초 동안 유지되는 쿠키 생성
				// -> 기존에 쿠기가 지정되어있었다면 해당 쿠키를 삭제
				cookie.setMaxAge(0);
				
			}
			
			cookie.setPath("/");
			
			resp.addCookie(cookie);
			
			
		} else { // 로그인 실패
			path += referer;
			
			ra.addFlashAttribute("message", "아이디 또는 비밀번호 불일치");
		}
		
		return path;
	}
	
	@GetMapping("/logout")
	public String logout(SessionStatus status, HttpSession session) {
		status.setComplete();
		return "redirect:/";
	}
	
}
