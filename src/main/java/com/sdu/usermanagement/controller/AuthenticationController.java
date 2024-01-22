package com.sdu.usermanagement.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdu.usermanagement.dto.JwtAuthResponse;
import com.sdu.usermanagement.dto.RefreshTokenRequest;
import com.sdu.usermanagement.dto.SignInRequest;
import com.sdu.usermanagement.dto.SignUpRequest;
import com.sdu.usermanagement.service.AuthenticationService;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000") 
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authenticationService.register(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody SignInRequest signInRequest) {
        return authenticationService.login(signInRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
