package com.bank.external.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 쓰는 빈 생성자, 다른 패키지에서 new 못 함
public class MemberEntity {
    @Id
    private Long memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String memberNo;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneMasked;

}
