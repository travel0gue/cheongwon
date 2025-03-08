package com.hufs_cheongwon.domain;

import com.hufs_cheongwon.domain.enums.Role;
import com.hufs_cheongwon.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

//    @Column(nullable = false)
    private String name;

    private String studentNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Users(String email, String password, String name, String studentNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentNumber = studentNumber;
    }

    public void setEncodedPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return false;
    }
}
