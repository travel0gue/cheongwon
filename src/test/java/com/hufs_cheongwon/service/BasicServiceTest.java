package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Category;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.fixture.PetitionFixture;
import com.hufs_cheongwon.fixture.UserFixture;
import com.hufs_cheongwon.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasicServiceTest extends ServiceTestSupport {

    @DisplayName("사용자와 청원을 저장하고 관계를 확인할 수 있다")
    @Test
    void saveUserAndPetition() {
        Users user = UserFixture.createActiveUser();
        Users savedUser = usersRepository.save(user);
        flush();

        Petition petition = PetitionFixture.createOngoingPetition(savedUser, "테스트 청원", Category.ACADEMIC);
        Petition savedPetition = petitionRepository.save(petition);
        flush();

        assertThat(savedPetition.getId()).isNotNull();
        assertThat(savedPetition.getUsers().getId()).isEqualTo(savedUser.getId());
        assertThat(savedPetition.getTitle()).isEqualTo("테스트 청원");
        assertThat(savedPetition.getPetitionStatus()).isEqualTo(PetitionStatus.ONGOING);
    }

    @DisplayName("청원 상태별 개수를 조회할 수 있다")
    @Test
    void countPetitionsByStatus() {
        Users user = usersRepository.save(UserFixture.createActiveUser());
        flush();

        petitionRepository.save(PetitionFixture.createOngoingPetition(user));
        petitionRepository.save(PetitionFixture.createOngoingPetition(user));
        petitionRepository.save(PetitionFixture.createExpiredPetition(user));
        flush();

        long ongoingCount = petitionRepository.countByPetitionStatus(PetitionStatus.ONGOING);
        long expiredCount = petitionRepository.countByPetitionStatus(PetitionStatus.EXPIRED);

        assertThat(ongoingCount).isEqualTo(2);
        assertThat(expiredCount).isEqualTo(1);
    }

    @DisplayName("청원의 조회수를 증가시킬 수 있다")
    @Test
    void increaseViewCount() {
        Users user = usersRepository.save(UserFixture.createActiveUser());
        Petition petition = petitionRepository.save(
            PetitionFixture.createOngoingPetition(user, "조회수 테스트", Category.WELFARE)
        );
        flush();

        int initialViewCount = petition.getViewCount();
        petition.addViewCount(1);
        petitionRepository.save(petition);
        flush();

        Petition updatedPetition = petitionRepository.findById(petition.getId()).orElse(null);
        assertThat(updatedPetition).isNotNull();
        assertThat(updatedPetition.getViewCount()).isEqualTo(initialViewCount + 1);
    }
}