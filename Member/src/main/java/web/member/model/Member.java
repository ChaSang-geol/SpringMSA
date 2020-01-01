package web.member.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@ToString
public class Member {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, length=100)
	private String userName;
	
	@Column(nullable = false, length=200)
	private String userPassword;
	
	@Column(nullable = false, unique = true, length=100)
	private String userEmail;
	
	@Column(nullable = true, length=20)
	private String phoneNumber;
	
	@CreationTimestamp
	private Date regdate;
	
	@UpdateTimestamp
	private Date updatedate;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="id")
	private Set<MemberRole> roles;
	
}
