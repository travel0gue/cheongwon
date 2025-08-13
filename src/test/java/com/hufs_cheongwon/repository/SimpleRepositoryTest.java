package com.hufs_cheongwon.repository;

import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SimpleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsersRepository usersRepository;

    @DisplayName("사용자를 저장하고 조회할 수 있다")
    @Test
    void saveAndFindUser() {
        Users user = UserFixture.createActiveUser();
        
        Users savedUser = usersRepository.save(user);
        entityManager.flush();
        entityManager.clear();
        
        Users foundUser = usersRepository.findById(savedUser.getId()).orElse(null);
        
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@hufs.ac.kr");
        assertThat(foundUser.getName()).isEqualTo("테스트유저");
    }

    @DisplayName("이메일로 사용자를 조회할 수 있다")
    @Test
    void findByEmail() {
        Users user = UserFixture.createActiveUser("unique@test.com", "유니크사용자");
        usersRepository.save(user);
        entityManager.flush();

        Users foundUser = usersRepository.findByEmail("unique@test.com").orElse(null);
        
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("유니크사용자");
    }
}