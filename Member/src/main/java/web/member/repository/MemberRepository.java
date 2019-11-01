package web.member.repository;
//MemberRepository.java

import org.springframework.data.repository.CrudRepository;

import web.member.model.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
}