package com.dfactory.TodoList_API.controller;

import com.dfactory.TodoList_API.dto.RegisterRequest;
import com.dfactory.TodoList_API.dto.TokenResponse;
import com.dfactory.TodoList_API.model.UserEntity;
import com.dfactory.TodoList_API.repository.UserRepository;
import com.dfactory.TodoList_API.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        UserEntity user = UserEntity
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody RegisterRequest request){
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return ResponseEntity.badRequest().build();
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
