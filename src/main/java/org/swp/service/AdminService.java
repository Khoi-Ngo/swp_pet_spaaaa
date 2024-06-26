package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.dto.request.SignUpRequest;
import org.swp.dto.response.DetailAccountDto;
import org.swp.dto.response.ListAccountCustomerDto;
import org.swp.dto.response.ListAccountShopOwnerDto;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.IAdminRepository;
import org.swp.repository.IUserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<ListAccountShopOwnerDto> getAllShopOwner(){
        return adminRepository.findAllShopOwnerAcc().stream()
                .map(user ->{
                    ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
                    User shopOwner = userRepository.findById(dto.getId()).get();
                    dto.setStatus(shopOwner.isDeleted());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ListAccountCustomerDto> getAllCustomer() {
        return adminRepository.findAllCustomerACC().stream()
                .map(service -> {
                    ListAccountCustomerDto dto = modelMapper.map(service, ListAccountCustomerDto.class);
                    User user = userRepository.findById(dto.getId()).get();
                    dto.setStatus(user.isDeleted());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public User addShopOwner(@NotNull SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = modelMapper.map(signUpRequest, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.SHOP_OWNER);
        user.setPhone(signUpRequest.getPhoneNumber());
        return userRepository.save(user);
    }

    public Object deleteUserById(int id){
        User user = userRepository.findById(id).get();
        user.setDeleted(true);
        userRepository.save(user);
        ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
        dto.setStatus(true);
        return dto;
    }

    public Object viewAccById(int id){
        User user = userRepository.findById(id).get();
        DetailAccountDto dto = modelMapper.map(user, DetailAccountDto.class);
        dto.setStatus(user.isDeleted());
        return dto;
    }


}
