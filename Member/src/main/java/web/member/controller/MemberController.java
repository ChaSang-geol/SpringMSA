package web.member.controller;
//MemberController.java

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.member.model.Member;
import web.member.model.MemberRole;
import web.member.repository.MemberRepository;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MemberRepository memberRepository;
	
	@SuppressWarnings("unchecked")
	@PostMapping("")
	public String create(Member member) {
		MemberRole role = new MemberRole();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		member.setUserPassword(passwordEncoder.encode(member.getUserPassword()));
		role.setRoleName("BASIC");
		member.setRoles((Set<MemberRole>) Arrays.asList(role));
		memberRepository.save(member);
		return "redirect:/";
	}
}