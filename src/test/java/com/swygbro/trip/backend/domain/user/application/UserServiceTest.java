package com.swygbro.trip.backend.domain.user.application;

import com.swygbro.trip.backend.domain.user.TestUserFactory;
import com.swygbro.trip.backend.domain.user.domain.User;
import com.swygbro.trip.backend.domain.user.domain.UserRepository;
import com.swygbro.trip.backend.domain.user.dto.CreateUserRequest;
import com.swygbro.trip.backend.domain.user.dto.UserInfoDto;
import com.swygbro.trip.backend.domain.user.excepiton.DuplicateDataException;
import com.swygbro.trip.backend.domain.user.excepiton.PasswordNotMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Testcontainers
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유저생성 성공")
    void createUserSuccess() {
        // given
        CreateUserRequest createUserRequest = TestUserFactory.createCreateUserRequest();

        // when
        UserInfoDto userInfo = userService.createUser(createUserRequest);

        // then
        User user = userRepository.findById(userInfo.getId()).orElse(null);

        assertThat(user).isNotNull();
        assertThat(user.getNickname()).isEqualTo(createUserRequest.getNickname());
        assertThat(user.getName()).isEqualTo(createUserRequest.getName());
        assertThat(user.getPhone()).isEqualTo(createUserRequest.getPhone());
        assertThat(user.getNationality()).isEqualTo(createUserRequest.getNationality());
        assertThat(user.getEmail()).isEqualTo(createUserRequest.getEmail());
        assertThat(user.getGender()).isEqualTo(createUserRequest.getGender());
        assertThat(passwordEncoder.matches(createUserRequest.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유저생성 실패 - 비밀번호 불일치")
    void createUserFailPasswordNotMatch() {
        // given
        CreateUserRequest createUserRequest = TestUserFactory.createCreateUserRequestWithDifferentPassword();

        // then
        assertThrows(PasswordNotMatchException.class, () -> userService.createUser(createUserRequest));
    }

    @Test
    @DisplayName("유저생성 실패 - 중복된 데이터")
    void createUserFailDuplicateData() {
        // given
        CreateUserRequest createUserRequest = TestUserFactory.createCreateUserRequest();
        CreateUserRequest sameRequest = TestUserFactory.createCreateUserRequest();

        // when
        userService.createUser(createUserRequest);

        // then
        assertThrows(DuplicateDataException.class, () -> userService.createUser(sameRequest));
    }

}
