package org.swp.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.dto.request.RefreshRequest;
import org.swp.dto.request.SignInRequest;
import org.swp.dto.request.SignUpRequest;
import org.swp.dto.response.JwtAuthenticationResponse;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.IUserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final IUserRepository IUserRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JWTService jwtService;

    public User signUp(@NotNull SignUpRequest signUpRequest) {
        User user = modelMapper.map(signUpRequest, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setCreatedTime(LocalDateTime.now());
        return IUserRepository.save(user);
    }

    public JwtAuthenticationResponse signIn(@NotNull SignInRequest signInRequest) {
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        var user = IUserRepository
                .findByUsername(
                        signInRequest.getUsername()).orElseThrow(()
                        -> new IllegalArgumentException("Invalid username or password."));
        if (user.isDeleted()) {
            throw new IllegalArgumentException("User account has been deleted.");
        }
        var jwt = jwtService.generrateToken(user, user.getId(), user.getEmail(), user.getRole());
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user,
                user.getId(), user.getEmail(), user.getRole()
        );

        return new JwtAuthenticationResponse(jwt, refreshToken);

    }

    public JwtAuthenticationResponse refresh(@NotNull String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }
        String username = jwtService.extractUserName(token);
        User user = IUserRepository.findByUsername(username).orElseThrow();
        if (jwtService.validateToken(token, user)) {
            var jwt = jwtService.generrateToken(user, user.getId(), user.getEmail(), user.getRole());
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user, user.getId(), user.getEmail(), user.getRole());

            return new JwtAuthenticationResponse(jwt, refreshToken);

        }
        return null;
    }
}
