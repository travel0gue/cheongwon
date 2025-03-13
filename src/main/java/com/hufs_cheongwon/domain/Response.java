package com.hufs_cheongwon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id", nullable = false)
    private Petition petition;

    @Column(nullable = false)
    private String content;

    @Builder
    public Response(Admin admin, Petition petition, String content) {
        this.admin = admin;
        admin.addResponse(this);
        this.petition = petition;
        this.content = content;
    }

    /**
     * 비즈니스 로직
     */
    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
