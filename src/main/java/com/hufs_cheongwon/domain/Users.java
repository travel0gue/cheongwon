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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column
    private String studentNumber;

    @Column
    @Setter
    private String status;  //TODO: enum화 하기

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Petition> petitions = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Agreement> agreements = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Users(String email, String password, String name, String studentNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.studentNumber = studentNumber;
        this.status = "Active";
    }

    /**
     * 연관관계 설정 메소드
     */
    public void addPetition(Petition petition) {
        petitions.add(petition);
    }

    public void addAgreement(Agreement agreement) {
        agreements.add(agreement);
    }

    public void addReport(Report report) {
        reports.add(report);
    }

    public void setEncodedPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return "ROLE_USER";
    }
}
