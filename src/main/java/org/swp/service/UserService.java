package org.swp.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
        String username = getUserNameFromToken(token);
        User user = IUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        PrivateUserDto dto = modelMapper.map(user, PrivateUserDto.class);
        return dto;
    }

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }
}
