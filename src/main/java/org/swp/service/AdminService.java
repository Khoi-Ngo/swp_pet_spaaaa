package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.dto.request.SignUpRequest;
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

    public List<ListAccountShopOwnerDto> getAllShopOwner(){
        return adminRepository.findAllShopOwnerAcc().stream()
                .map(user ->{
                    ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ListAccountCustomerDto> getAllCustomer() {
        return adminRepository.findAllCustomerACC().stream()
                .map(service -> {
                    ListAccountCustomerDto dto = modelMapper.map(service, ListAccountCustomerDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public User addShopOwner(@NotNull SignUpRequest signUpRequest) {
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

}
