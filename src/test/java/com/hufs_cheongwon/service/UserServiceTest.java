package com.hufs_cheongwon.service;

import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.UsersStatus;
import com.hufs_cheongwon.fixture.UserFixture;
import com.hufs_cheongwon.support.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends ServiceTestSupport {

    @DisplayName("사용자를 저장하고 이메일로 조회할 수 있다")
    @Test
    void saveAndFindUserByEmail() {
        // given
        Users user = UserFixture.createActiveUser("user@test.com", "테스트사용자");
        
        // when
        Users savedUser = usersRepository.save(user);
        flushAndClear();
        
        // then
        Optional<Users> foundUser = usersRepository.findByEmail("user@test.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("테스트사용자");
        assertThat(foundUser.get().getStatus()).isEqualTo(UsersStatus.ACTIVE);
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @DisplayName("사용자 상태를 변경할 수 있다")
    @Test
    void changeUserStatus() {
        // given
        Users user = UserFixture.createActiveUser();
        Users savedUser = usersRepository.save(user);
        flush();
        
        // when
        savedUser.changeStatus(UsersStatus.INACTIVE);
        usersRepository.save(savedUser);
        flushAndClear();
        
        // then
        Users updatedUser = usersRepository.findById(savedUser.getId()).orElse(null);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getStatus()).isEqualTo(UsersStatus.INACTIVE);
    }

    @DisplayName("사용자 정보를 삭제할 수 있다")
    @Test
    void eraseUserInfo() {
        // given
        Users user = UserFixture.createActiveUser("delete@test.com", "삭제될사용자");
        Users savedUser = usersRepository.save(user);
        flush();
        
        String originalEmail = savedUser.getEmail();
        String originalName = savedUser.getName();
        
        // when
        savedUser.eraseUserInfo();
        usersRepository.save(savedUser);
        flushAndClear();
        
        // then
        Users erasedUser = usersRepository.findById(savedUser.getId()).orElse(null);
        assertThat(erasedUser).isNotNull();
        assertThat(erasedUser.getEmail()).isNotEqualTo(originalEmail);
        assertThat(erasedUser.getName()).isNotEqualTo(originalName);
        assertThat(erasedUser.getEmail()).isEqualTo("unknown");
        assertThat(erasedUser.getName()).isNull();
        assertThat(erasedUser.getDeleteAt()).isNotNull();
    }

    @DisplayName("전체 사용자 수를 조회할 수 있다")
    @Test
    void countAllUsers() {
        // given
        usersRepository.save(UserFixture.createActiveUser("user1@test.com", "사용자1"));
        usersRepository.save(UserFixture.createActiveUser("user2@test.com", "사용자2"));
        usersRepository.save(UserFixture.createInactiveUser());
        flush();
        
        // when
        long totalCount = usersRepository.count();
        
        // then
        assertThat(totalCount).isEqualTo(3);
    }

    @DisplayName("존재하지 않는 이메일로 조회시 빈 결과를 반환한다")
    @Test
    void findByNonExistentEmail() {
        // given
        usersRepository.save(UserFixture.createActiveUser("existing@test.com", "존재하는사용자"));
        flush();
        
        // when
        Optional<Users> nonExistentUser = usersRepository.findByEmail("nonexistent@test.com");
        
        // then
        assertThat(nonExistentUser).isEmpty();
    }
}