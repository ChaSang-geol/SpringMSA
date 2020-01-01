package web.member.controller;
//SecurirtyMember.java

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import web.member.model.Member;
import web.member.model.MemberRole;


@Getter
@Setter
public class SecurityMember extends User {
	private static final String ROLE_PREFIX = "ROLE_";
	private static final long serialVersionUID = 1L;
	
	public SecurityMember(Member member) {
		super(member.getUserName(), member.getUserPassword(), makeGrantedAuthority(member.getRoles()));
	}
	
	private static List<GrantedAuthority> makeGrantedAuthority(Set<MemberRole> set){
		List<GrantedAuthority> list = new ArrayList<>();
		set.forEach(role -> list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleName())));
		return list;
	}
}