package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateShopRequest;
import org.swp.dto.response.BookingHistoryListItemDto;
import org.swp.dto.response.ShopDetailDto;
import org.swp.entity.Booking;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.TypePet;
import org.swp.repository.IBookingRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.ArrayList;
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

    @Autowired
    private IBookingRepository bookingRepository;

    public Object getMostRcmdShops(int numberOfRecords) {
//        return shopRepository.findMostRcmdShops(numberOfRecords);
        return shopRepository.findAll();
    }

    public Object getMostRcmdShops(TypePet typePet, int numberOfRecords) {
//        return shopRepository.findMostRcmdShops(typePet, numberOfRecords);
        return shopRepository.findAll();
    }

    //map to shop detail dto
    private ShopDetailDto sMapToDto(Shop shopEntity) {
        ShopDetailDto dto = modelMapper.map(shopEntity, ShopDetailDto.class);
        //get list booking of shop
//        List<Booking> bookings = bookingRepository.findByShopId(dto.getId());
//        bookings.forEach(booking -> {
//            BookingHistoryListItemDto bookingDto = modelMapper.map(booking, BookingHistoryListItemDto.class);
//            bookingDto.setServiceName(booking.getService().getServiceName());
//            bookingDto.setServiceId(booking.getService().getId());
//            bookingDto.setShopName(booking.getShop().getShopName());
//            if (dto.getBookingHistory().get(booking.getCacheShopTimeSlot().getLocalDate()) != null) {
//                dto.getBookingHistory().get(booking.getCacheShopTimeSlot().getLocalDate()).add(bookingDto);
//            } else {
//                List<BookingHistoryListItemDto> historyListDate = new ArrayList<>();
//                historyListDate.add(bookingDto);
//                dto.getBookingHistory().put(
//                        booking.getCacheShopTimeSlot().getLocalDate().atStartOfDay(),
//                        historyListDate
//                );
//            }
//        });
        return dto;
    }



    public List<ShopDetailDto> getAllShops() {
        return shopRepository.findAll().stream()
                .map(shop -> {
                    ShopDetailDto dto = modelMapper.map(shop, ShopDetailDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getShopDetail(int id) {
        Shop shop = shopRepository.findById(id).get();
        if (shop.isDeleted() == true) {
           return "Shop is deleted!";
        }
        return sMapToDto(shop);
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

    public Object deleteShop(int id) {
        Shop shop = shopRepository.findById(id).get();
        shop.setDeleted(true);
        shopRepository.save(shop);
        return sMapToDto(shop);
    }

}
