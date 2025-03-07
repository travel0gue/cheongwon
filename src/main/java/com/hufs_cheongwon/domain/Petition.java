package com.hufs_cheongwon.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
public class Petition extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetitionStatus petitionStatus;

    @Column
    private Integer agree_count = 0;

    @Column
    private Integer view_count = 0;

    @Column
    private Integer report_count = 0;

    //userId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users; // 작성자

    @OneToMany(mappedBy = "petition", cascade = CascadeType.ALL)
    private List<Agreement> agreements = new ArrayList<>();

    @OneToMany(mappedBy = "petition", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Petition(Users user, String title, Category category, String content, PetitionStatus petitionStatus) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.petitionStatus = petitionStatus;
        this.users = user;
        user.addPetition(this);
    }

    /**
     * 비즈니스 메소드
     */
    public void addAgreeCount(int count) {
        this.agree_count += count;
    }
    public void addViewCount(int count) {
        this.view_count += count;
    }
    public void addReportCount(int count) {
        this.report_count += count;
    }

    /**
     * 연관관계 메소드
     */
    public void addReport(Report report) {
        this.reports.add(report);
    }

    public void addAgreement(Agreement agreement) {
        this.agreements.add(agreement);
    }
}
