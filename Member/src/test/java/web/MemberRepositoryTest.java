package web;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.java.Log;
import web.member.model.Member;
import web.member.model.MemberRole;
import web.member.repository.MemberRepository;

// MemberRepositoryTest.java

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;
	
	@SuppressWarnings("unchecked")
	@Test
	public void insertTest() {
		for(int i=0; i<100; i++) {
			Member member = new Member();
			member.setUserName("user" + i);
			member.setUserPassword("pw" + i);
			member.setUserEmail("hihi@" + i);
			member.setPhoneNumber("+8210-1234-" +i);
			MemberRole role = new MemberRole();
			if(i <= 80) {
				role.setRoleName("BASIC");
			}else if(i <= 90) {
				role.setRoleName("MANAGER");
			}else {
				role.setRoleName("ADMIN");
			}
			member.setRoles((Set<MemberRole>) Arrays.asList(role));
			memberRepository.save(member);
		}
	}
	
	@Test
	public void testMember() {
		Long id = (long) 85 ;
		//Optional<Member> result = Optional.ofNullable(memberRepository.findById(id));
		Optional<Member> result = memberRepository.findById(id);
		result.ifPresent(member -> log.info("member " + member));
	}
}