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

    // create
    public Object createShopTimeSlot(CreateShopTimeSlotRequest request){
        ShopTimeSlot shopTimeSlot = modelMapper.map(request, ShopTimeSlot.class);

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId()).get();
        shopTimeSlot.setTimeSlot(timeSlot);

        Shop shop = shopRepository.findById(request.getShopId()).get();
        shopTimeSlot.setShop(shop);

        shopTimeSlotRepository.save(shopTimeSlot);

        CacheShopTimeSlot cacheShopTimeSlot = new CacheShopTimeSlot();
        cacheShopTimeSlot.setAvailableSlots(request.getTotalSlot());
        cacheShopTimeSlot.setTotalSlots(request.getTotalSlot());
        cacheShopTimeSlot.setShop(shop);
        cacheShopTimeSlot.setShopTimeSlot(shopTimeSlot);
        cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
        return "create shop time slot ok";
    }


    //delete
    public Object deleteShopTimeSlot(int id){
        ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findById(id).get();
        shopTimeSlot.setDeleted(true);
        shopTimeSlotRepository.save(shopTimeSlot);

        CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository.findByShopIdShopTimeSlotId(shopTimeSlot.getShop().getId(), shopTimeSlot.getId());
        cacheShopTimeSlot.setDeleted(true);
        cacheShopTimeSlotRepository.save(cacheShopTimeSlot);

        return "delete shop time slot successful";
    }


    //update
    public Object updateShopTimeSlot(UpdateShopTimeSlotRequest request){
        ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findById(request.getId()).get();
        modelMapper.map(request,shopTimeSlot);

        Shop shop = shopRepository.findById(request.getShopId()).get();
        shopTimeSlot.setShop(shop);

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId()).get();
        shopTimeSlot.setTimeSlot(timeSlot);

        CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository.findByShopIdShopTimeSlotId(request.getShopId(), request.getId());
        cacheShopTimeSlot.setTotalSlots(request.getTotalSlot());
        cacheShopTimeSlot.setAvailableSlots(request.getTotalSlot()-cacheShopTimeSlot.getUsedSlots());
        cacheShopTimeSlotRepository.save(cacheShopTimeSlot);

        shopTimeSlotRepository.save(shopTimeSlot);

        return "update shop time slot successful";
    }


    //get all
    public Object getAllShopTimeSlot(String token){
        String username = getUserNameFromToken(token);
        User user = userRepository.findByUsername(username).get();
        Shop shop = shopRepository.findByShopOwnerId(user.getId());

        return shopTimeSlotRepository.findByShopId(shop.getId()).stream()
                .map(shopTimeSlot -> {
                    ListShopTimeSlotDto dto = modelMapper.map(shopTimeSlot, ListShopTimeSlotDto.class);

                    TimeSlot timeSlot = shopTimeSlot.getTimeSlot();
                    dto.setStartLocalTime(timeSlot.getStartLocalDateTime());
                    dto.setEndLocalTime(timeSlot.getEndLocalDateTime());

                    CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository.findByShopIdShopTimeSlotId(shop.getId(), shopTimeSlot.getId());
                    dto.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots());
                    dto.setUsedSlots(cacheShopTimeSlot.getUsedSlots());

                    dto.setStatus(shopTimeSlot.isDeleted());

                    return dto;
                })
                .collect(Collectors.toList());
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


