package com.hufs_cheongwon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasicTest {

    @DisplayName("기본 테스트가 동작한다")
    @Test
    void basicTest() {
        String expected = "Hello World";
        String actual = "Hello World";
        
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("계산 테스트")
    @Test
    void calculationTest() {
        int result = 2 + 3;
        assertThat(result).isEqualTo(5);
    }
}