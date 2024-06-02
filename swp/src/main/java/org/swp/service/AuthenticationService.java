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
import org.swp.repository.IUserRepository;

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
        var jwt = jwtService.generrateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        return new JwtAuthenticationResponse(jwt, refreshToken);

    }

    public JwtAuthenticationResponse refresh(@NotNull RefreshRequest refreshRequest) {
        String username = jwtService.extractUserName(refreshRequest.getToken());
        User user = IUserRepository.findByUsername(username).orElseThrow();
        if (jwtService.validateToken(refreshRequest.getToken(), user)) {
            var jwt = jwtService.generrateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            return new JwtAuthenticationResponse(jwt, refreshToken);

        }
        return null;
    }
}
