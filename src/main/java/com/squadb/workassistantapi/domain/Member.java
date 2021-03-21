package com.squadb.workassistantapi.domain;

import com.squadb.workassistantapi.util.HashUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType type;

    public static Member createMember(String email, String name, String passwordHash, MemberType memberType) {
        Member member = new Member();
        member.email = email;
        member.name = name;
        member.passwordHash = passwordHash;
        member.type = memberType;
        return member;
    }

    public boolean isAdmin() {
        return type.isAdmin();
    }

    public void validatePassword(String passwordInput) {
        if (!HashUtil.validatePassword(passwordInput, passwordHash)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updatePassword(String newPassword) {
        this.passwordHash = HashUtil.hashPassword(newPassword);
    }
}
