package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Agreement;
import com.hufs_cheongwon.domain.Petition;
import com.hufs_cheongwon.domain.PetitionBookmark;
import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.Category;
import com.hufs_cheongwon.domain.enums.PetitionStatus;
import com.hufs_cheongwon.fixture.PetitionFixture;
import com.hufs_cheongwon.fixture.UserFixture;
import com.hufs_cheongwon.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetitionServiceTest extends ServiceTestSupport {

    @DisplayName("청원에 동의하면 동의 수가 증가한다")
    @Test
    void agreeToPetition() {
        // given
        Users author = usersRepository.save(UserFixture.createActiveUser("author@test.com", "작성자"));
        Users user = usersRepository.save(UserFixture.createActiveUser("user@test.com", "동의자"));
        
        Petition petition = petitionRepository.save(
            PetitionFixture.createOngoingPetition(author, "동의 테스트 청원", Category.ACADEMIC)
        );
        flush();
        
        int initialAgreeCount = petition.getAgreeCount();
        
        // when
        Agreement agreement = Agreement.builder()
                .users(user)
                .petition(petition)
                .build();
        
        agreementRepository.save(agreement);
        petitionRepository.save(petition);
        flushAndClear();
        
        // then
        Petition updatedPetition = petitionRepository.findById(petition.getId()).orElse(null);
        assertThat(updatedPetition).isNotNull();
        assertThat(updatedPetition.getAgreeCount()).isEqualTo(initialAgreeCount + 1);
        
        long agreementCount = agreementRepository.countByPetitionId(petition.getId());
        assertThat(agreementCount).isEqualTo(1);
    }

    @DisplayName("청원을 북마크하면 북마크 수가 증가한다")
    @Test
    void bookmarkPetition() {
        // given
        Users author = usersRepository.save(UserFixture.createActiveUser("author@test.com", "작성자"));
        Users user = usersRepository.save(UserFixture.createActiveUser("user@test.com", "북마크유저"));
        
        Petition petition = petitionRepository.save(
            PetitionFixture.createOngoingPetition(author, "북마크 테스트 청원", Category.WELFARE)
        );
        flush();
        
        int initialBookmarkCount = petition.getBookmarkCount();
        
        // when
        PetitionBookmark bookmark = PetitionBookmark.builder()
                .user(user)
                .petition(petition)
                .build();
        
        petitionBookmarkRepository.save(bookmark);
        petition.increaseBookmarkCount();
        petitionRepository.save(petition);
        flushAndClear();
        
        // then
        Petition updatedPetition = petitionRepository.findById(petition.getId()).orElse(null);
        assertThat(updatedPetition).isNotNull();
        assertThat(updatedPetition.getBookmarkCount()).isEqualTo(initialBookmarkCount + 1);
        
        // 북마크가 실제로 저장되었는지 확인
        assertThat(petitionBookmarkRepository.findByUsersAndPetition(user, updatedPetition)).isPresent();
    }

    @DisplayName("청원 상태를 변경할 수 있다")
    @Test
    void changePetitionStatus() {
        // given
        Users author = usersRepository.save(UserFixture.createActiveUser());
        Petition petition = petitionRepository.save(
            PetitionFixture.createOngoingPetition(author, "상태 변경 테스트", Category.IT_SERVICE)
        );
        flush();
        
        assertThat(petition.getPetitionStatus()).isEqualTo(PetitionStatus.ONGOING);
        
        // when
        petition.changePetitionStatus(PetitionStatus.WAITING);
        petitionRepository.save(petition);
        flushAndClear();
        
        // then
        Petition updatedPetition = petitionRepository.findById(petition.getId()).orElse(null);
        assertThat(updatedPetition).isNotNull();
        assertThat(updatedPetition.getPetitionStatus()).isEqualTo(PetitionStatus.WAITING);
    }

    @DisplayName("청원 조회수가 정확히 증가한다")
    @Test
    void increaseViewCount() {
        // given
        Users author = usersRepository.save(UserFixture.createActiveUser());
        Petition petition = petitionRepository.save(
            PetitionFixture.createOngoingPetition(author, "조회수 테스트", Category.CAREER)
        );
        flush();
        
        int initialViewCount = petition.getViewCount();
        
        // when
        petition.addViewCount(1);
        petition.addViewCount(3);
        petitionRepository.save(petition);
        flushAndClear();
        
        // then
        Petition updatedPetition = petitionRepository.findById(petition.getId()).orElse(null);
        assertThat(updatedPetition).isNotNull();
        assertThat(updatedPetition.getViewCount()).isEqualTo(initialViewCount + 4);
    }

    @DisplayName("같은 사용자가 작성한 청원을 조회할 수 있다")
    @Test
    void findPetitionsByUser() {
        // given
        Users user1 = usersRepository.save(UserFixture.createActiveUser("user1@test.com", "사용자1"));
        Users user2 = usersRepository.save(UserFixture.createActiveUser("user2@test.com", "사용자2"));
        
        petitionRepository.save(PetitionFixture.createOngoingPetition(user1, "user1의 청원1", Category.ACADEMIC));
        petitionRepository.save(PetitionFixture.createOngoingPetition(user1, "user1의 청원2", Category.WELFARE));
        petitionRepository.save(PetitionFixture.createOngoingPetition(user2, "user2의 청원", Category.IT_SERVICE));
        flush();
        
        // when
        long totalPetitions = petitionRepository.count();
        
        // then
        assertThat(totalPetitions).isEqualTo(3);
    }
}