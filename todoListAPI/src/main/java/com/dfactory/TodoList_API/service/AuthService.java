package com.dfactory.TodoList_API.service;

import com.dfactory.TodoList_API.dto.LoginRequest;
import com.dfactory.TodoList_API.dto.RegisterRequest;
import com.dfactory.TodoList_API.dto.TokenResponse;
import com.dfactory.TodoList_API.repository.UserRepository;
import com.dfactory.TodoList_API.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import com.dfactory.TodoList_API.model.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email is already registered");
        }

        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new TokenResponse(token);
    }

    public TokenResponse login(LoginRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email o Password is incorrect"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Email o Password is incorrect");
        }

        String token = jwtUtil.generateToken(user);
        return new TokenResponse(token);
    }

}
