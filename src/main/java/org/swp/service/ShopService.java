package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ShopDetailDto;
import org.swp.enums.TypePet;
import org.swp.repository.IShopRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Object getMostRcmdShops(int numberOfRecords) {
//        return shopRepository.findMostRcmdShops(numberOfRecords);
        return shopRepository.findAll();
    }

    public Object getMostRcmdShops(TypePet typePet, int numberOfRecords) {
//        return shopRepository.findMostRcmdShops(typePet, numberOfRecords);
        return shopRepository.findAll();
    }

    public List<ShopDetailDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shop -> {
                    ShopDetailDto dto = modelMapper.map(shop, ShopDetailDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
