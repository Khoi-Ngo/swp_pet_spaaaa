package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateShopRequest;
import org.swp.dto.response.ShopDetailDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.TypePet;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IUserRepository userRepository;

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

    public Object createShop(CreateShopRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }
        Shop shop = modelMapper.map(request, Shop.class);
        shop.setUser(user);
        Shop savedShop = shopRepository.save(shop);
        return modelMapper.map(savedShop, ShopDetailDto.class);
    }
}
