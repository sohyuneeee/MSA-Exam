package com.sparta.msa_exam.auth.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequestDto {

    @NotBlank(message = "MEMBER_ACCOUNT_NAME_BLANK")
    @Size(min = 2, max = 20, message = "MEMBER_ACCOUNT_NAME_LENGTH_INVALID")
    private String username;

    @NotBlank(message = "MEMBER_PASSWORD_BLANK")
    private String password;

    @Builder
    private UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static UserRequestDto of(String username, String password) {
        return UserRequestDto.builder()
                .username(username)
                .password(password).build();
    }

}
