package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ListAccountShopOwnerDto;
import org.swp.repository.IAdminRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ListAccountShopOwnerDto> getAllShopOwner(){
        return adminRepository.findAll().stream()
                .map(user ->{
                    ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
