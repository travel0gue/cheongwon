package com.hufs_cheongwon.domain;

import com.hufs_cheongwon.domain.enums.Role;
import com.hufs_cheongwon.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Admin extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    //    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    public boolean isAdmin() {
        return true;
    }
}
