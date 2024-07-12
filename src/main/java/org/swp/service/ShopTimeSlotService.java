package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateShopTimeSlotRequest;
import org.swp.dto.request.UpdateShopTimeSlotRequest;
import org.swp.dto.response.ListShopTimeSlotDto;
import org.swp.entity.*;
import org.swp.repository.*;

import java.util.stream.Collectors;

@Service
public class ShopTimeSlotService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IShopTimeSlotRepository shopTimeSlotRepository;

    @Autowired
    private ITimeSlotRepository timeSlotRepository;

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private ICacheShopTimeSlotRepository cacheShopTimeSlotRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IBookingRepository bookingRepository;

    // create
    public Object createShopTimeSlot(CreateShopTimeSlotRequest request) {
        ShopTimeSlot shopTimeSlot = modelMapper.map(request, ShopTimeSlot.class);
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId()).get();
        shopTimeSlot.setTimeSlot(timeSlot);
        Shop shop = shopRepository.findById(request.getShopId()).get();
        shopTimeSlot.setShop(shop);
        shopTimeSlotRepository.save(shopTimeSlot);
        return "create shop time slot ok";
    }


    //delete
    public Object deleteShopTimeSlot(int id, String token) {
        ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findById(id).get();

        if (!isShopOwner(shopTimeSlot, token) || shopTimeSlot.isDeleted())
            throw new RuntimeException("User not shop owner/ shop timeslot is deleted");

        shopTimeSlot.setDeleted(true);
        shopTimeSlotRepository.save(shopTimeSlot);
        return "delete shop time slot successfully";
    }

    private boolean isShopOwner(ShopTimeSlot shopTimeSlot, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return userName.equals(shopTimeSlot.getShop().getUser().getUsername());
    }

    private boolean isShopOwner(ShopTimeSlot shopTimeSlot, Integer userId) {
        return userId.equals(shopTimeSlot.getShop().getUser().getId());
    }


    //update
    public Object updateShopTimeSlot(UpdateShopTimeSlotRequest request) {
        ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findById(request.getId()).get();
        if (!isShopOwner(shopTimeSlot, request.getUserId())) throw new RuntimeException("User not shop owner");
        modelMapper.map(request, shopTimeSlot);
        Shop shop = shopRepository.findById(request.getShopId()).get();
        shopTimeSlot.setShop(shop);
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId()).get();
        shopTimeSlot.setTimeSlot(timeSlot);
        shopTimeSlotRepository.save(shopTimeSlot);
        return "update shop time slot successful";
    }


    //get all
    public Object getAllShopTimeSlot(String token) {
        String username = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username).get();
        Shop shop = shopRepository.findByShopOwnerId(user.getId());

        return shopTimeSlotRepository.findByShopId(shop.getId()).stream()
                .filter(shopTimeSlot -> !shopTimeSlot.getTimeSlot().isDeleted())
                .map(shopTimeSlot -> {
                    ListShopTimeSlotDto dto = modelMapper.map(shopTimeSlot, ListShopTimeSlotDto.class);

                    dto.setShopId(shopTimeSlot.getShop().getId());

                    TimeSlot timeSlot = shopTimeSlot.getTimeSlot();
                    dto.setStartLocalTime(timeSlot.getStartLocalDateTime());
                    dto.setEndLocalTime(timeSlot.getEndLocalDateTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}


