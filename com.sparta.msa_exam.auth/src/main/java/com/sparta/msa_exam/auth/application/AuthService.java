package com.sparta.msa_exam.auth.application;

import com.sparta.msa_exam.auth.api.UserRequestDto;
import com.sparta.msa_exam.auth.domain.User;
import com.sparta.msa_exam.auth.domain.UserRepository;
import com.sparta.msa_exam.auth.domain.exception.CustomException;
import com.sparta.msa_exam.auth.domain.exception.ErrorCode;
import com.sparta.msa_exam.auth.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signUp(UserRequestDto requestDto) {
        checkUsername(requestDto.getUsername());
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        userRepository.save(User.of(requestDto.getUsername(), encodedPassword, null));
    }

    public String signIn(@Valid UserRequestDto requestDto) {
        User user = findUser(requestDto.getUsername());
        checkPassword(requestDto.getPassword(), user);
        return jwtUtil.createToken(user.getId());
    }

    private void checkUsername(String username) {
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(ErrorCode.USER_DUPLICATED);
        }
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, username));
    }

    private void checkPassword(String password, User user) {
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

}
