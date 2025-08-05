package com.hufs_cheongwon.fixture;

import com.hufs_cheongwon.domain.Users;
import com.hufs_cheongwon.domain.enums.UsersStatus;

public class UserFixture {

    public static Users createActiveUser() {
        return new Users("test@hufs.ac.kr", "encodedPassword123", "테스트유저", "20241234");
    }

    public static Users createActiveUser(String email, String name) {
        return new Users(email, "encodedPassword123", name, "20241234");
    }

    public static Users createActiveUser(String email, String name, String studentNumber) {
        return new Users(email, "encodedPassword123", name, studentNumber);
    }

    public static Users createInactiveUser() {
        Users user = new Users("inactive@hufs.ac.kr", "encodedPassword123", "비활성유저", "20241235");
        user.changeStatus(UsersStatus.INACTIVE);
        return user;
    }

    public static Users createDeletedUser() {
        Users user = new Users("deleted@hufs.ac.kr", "encodedPassword123", "삭제유저", "20241236");
        user.changeStatus(UsersStatus.DELETED);
        user.eraseUserInfo();
        return user;
    }
}