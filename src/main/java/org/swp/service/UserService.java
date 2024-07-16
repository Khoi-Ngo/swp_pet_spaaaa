package org.swp.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.dto.request.UpdatePasswordRequest;
import org.swp.dto.request.UpdateUserProfileRequest;
import org.swp.dto.response.AvataDto;
import org.swp.dto.response.PrivateUserDto;
import org.swp.dto.response.PublicUserDto;
import org.swp.entity.User;
import org.swp.repository.IUserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final IUserRepository IUserRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) {
                return IUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
            }

        };
    }

    public PublicUserDto getUserByUsername(String username) {
        User user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        PublicUserDto dto = modelMapper.map(user, PublicUserDto.class);
        return dto;
    }


    public Object getUserProfile(String token) {
        String username = jwtService.getUserNameFromToken(token);
        User user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        PrivateUserDto dto = modelMapper.map(user, PrivateUserDto.class);
        return dto;
    }

    public Object updateUserProfile(UpdateUserProfileRequest request) {
        User user = IUserRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!request.getEmail().equals(user.getEmail())) {
            if (IUserRepository.findByEmail(request.getEmail()).isPresent()) {
                return "Email already exists";
            }
        }

        if (request.getPhone() == null || !request.getPhone().matches("^0\\d{9}$")) {
            return "Phone number must be a 10-digit number starting with 0 and contain only numbers";
        }

        if (request.getPhone() == null || request.getPhone().length() != 10) {
            return "Phone number must be exactly 10 number";
        }

        modelMapper.map(request, user);
        IUserRepository.save(user);
        return modelMapper.map(user, UpdateUserProfileRequest.class);
    }

    public Object updatePassword(String token, UpdatePasswordRequest request) {
            String username = jwtService.getUserNameFromToken(token);
            User user = IUserRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new RuntimeException("Old password is incorrect");
            }
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("New password and confirm password do not match");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            IUserRepository.save(user);
            return "Password updated successfully";

    }

    public Object getUserAvata(String token) {
        String username = jwtService.getUserNameFromToken(token);
        User user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        AvataDto dto = modelMapper.map(user, AvataDto.class);
        return dto;
    }
}
