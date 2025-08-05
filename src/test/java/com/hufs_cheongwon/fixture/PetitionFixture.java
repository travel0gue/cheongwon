package com.hufs_cheongwon.fixture;

import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Category;
import com.hufs_cheongwon.domain.enums.PetitionStatus;

public class PetitionFixture {

    public static Petition createOngoingPetition(Users user) {
        return Petition.builder()
                .user(user)
                .title("도서관 운영시간 연장 요청")
                .category(Category.ACADEMIC)
                .content("도서관 운영시간을 24시간으로 연장해주세요. 시험기간에 공부할 공간이 부족합니다.")
                .petitionStatus(PetitionStatus.ONGOING)
                .build();
    }

    public static Petition createOngoingPetition(Users user, String title, Category category) {
        return Petition.builder()
                .user(user)
                .title(title)
                .category(category)
                .content("테스트용 청원 내용입니다.")
                .petitionStatus(PetitionStatus.ONGOING)
                .build();
    }

    public static Petition createOngoingPetition(Users user, String title, Category category, String content) {
        return Petition.builder()
                .user(user)
                .title(title)
                .category(category)
                .content(content)
                .petitionStatus(PetitionStatus.ONGOING)
                .build();
    }

    public static Petition createExpiredPetition(Users user) {
        return Petition.builder()
                .user(user)
                .title("만료된 청원")
                .category(Category.WELFARE)
                .content("이미 만료된 청원입니다.")
                .petitionStatus(PetitionStatus.EXPIRED)
                .build();
    }

    public static Petition createWaitingPetition(Users user) {
        return Petition.builder()
                .user(user)
                .title("답변 대기중인 청원")
                .category(Category.IT_SERVICE)
                .content("답변을 기다리고 있는 청원입니다.")
                .petitionStatus(PetitionStatus.WAITING)
                .build();
    }

    public static Petition createAnsweredPetition(Users user) {
        return Petition.builder()
                .user(user)
                .title("답변 완료된 청원")
                .category(Category.ACTIVITIES)
                .content("이미 답변이 완료된 청원입니다.")
                .petitionStatus(PetitionStatus.ANSWER_COMPLETED)
                .build();
    }

    public static Petition createPetitionWithCounts(Users user, int agreeCount, int viewCount) {
        Petition petition = Petition.builder()
                .user(user)
                .title("인기 청원")
                .category(Category.CAREER)
                .content("많은 관심을 받고 있는 청원입니다.")
                .petitionStatus(PetitionStatus.ONGOING)
                .build();
        
        petition.addAgreeCount(agreeCount);
        petition.addViewCount(viewCount);
        
        return petition;
    }
}