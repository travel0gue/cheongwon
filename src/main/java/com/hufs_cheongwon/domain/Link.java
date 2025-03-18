package com.hufs_cheongwon.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id", nullable = true)
    private Petition petition;

    private String link;

    /**
     *연관관계 매핑
     */
    public void setPetition(Petition petition) {
        this.petition = petition;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
