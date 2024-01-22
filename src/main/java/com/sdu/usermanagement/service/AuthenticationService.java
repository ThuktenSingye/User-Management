package com.sdu.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.sdu.usermanagement.dto.JwtAuthResponse;
import com.sdu.usermanagement.dto.RefreshTokenRequest;
import com.sdu.usermanagement.dto.SignInRequest;
import com.sdu.usermanagement.dto.SignUpRequest;
import com.sdu.usermanagement.model.Role;
import com.sdu.usermanagement.model.Token;
import com.sdu.usermanagement.model.User;
import com.sdu.usermanagement.model.Token.TokenType;
import com.sdu.usermanagement.model.User.UserStatus;
import com.sdu.usermanagement.repository.RoleRepository;
import com.sdu.usermanagement.repository.TokenRepository;
import com.sdu.usermanagement.repository.UserRepository;
import com.sdu.usermanagement.utility.JwtUtil;


@Service
public class AuthenticationService {
 
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private LogService logService;

    @Autowired 
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<String> register(SignUpRequest signUpRequest) {
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findById(2).orElseThrow();
        User user = new User();
        roles.add(role);
        user.setEmployeeId(signUpRequest.getEmployeeId());
        user.setFirstName(signUpRequest.getFirstName());
        user.setMiddleName(signUpRequest.getMiddleName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail((signUpRequest.getEmail()));
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        user.setStatus(UserStatus.REGISTERED);
        user.setPassword(new BCryptPasswordEncoder().encode("admin"));
        var savedUser = userRepository.saveAndFlush(user);
        var jwtToken = jwtUtil.generateToken(user);
        revokeAllUsersToken(user);
        saveUserToken(savedUser, jwtToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(savedUser).build();
        tokenRepository.saveAndFlush(token);
    }

    public ResponseEntity<JwtAuthResponse> login(SignInRequest signInRequest) {
        
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                User user = userRepository.findByEmail(signInRequest.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
                String jwtToken = jwtUtil.generateToken(user);
                String refreshJwtToken = jwtUtil.generateRefreshToken(user);

                JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                jwtAuthResponse.setToken(jwtToken);
                jwtAuthResponse.setRefreshToken(refreshJwtToken);
                revokeAllUsersToken(user);
                saveUserToken(user, jwtToken);
                logService.logApplicationStatus("Log In");
                return ResponseEntity.ok(jwtAuthResponse);

            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    public ResponseEntity<JwtAuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {

		try{
			String userEmail = jwtUtil.extractUsername(refreshTokenRequest.getToken());
			User user = userRepository.findByEmail(userEmail)
					.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
			if (jwtUtil.validateToken(refreshTokenRequest.getToken(), user)) {
				var jwtToken = jwtUtil.generateToken(user);
				JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
				jwtAuthResponse.setToken(jwtToken);
				jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());
				return ResponseEntity.ok(jwtAuthResponse);
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}

    private void revokeAllUsersToken(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getUserId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAllAndFlush(validUserTokens);
    }

}
