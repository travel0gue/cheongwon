package com.hufs_cheongwon.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String departure;

    @Column
    private String role; // 부서에서의 직책

    @Column
    private String name;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phoneNumber;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Response> responses = new ArrayList<>();

    @Builder
    public Admin(String departure, String role, String name, String email, String password, String phoneNumber) {
        this.departure = departure;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 연관관계 설정 메소드
     */
    public void addResponse(Response response) {
        this.responses.add(response);
    }
}
