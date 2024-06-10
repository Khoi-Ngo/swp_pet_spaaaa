package org.swp.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.controller.booking.BookingController;
import org.swp.dto.request.RequestAcceptBooking;
import org.swp.dto.request.RequestBookingRequest;
import org.swp.dto.request.RequestCancelBookingRequest;
import org.swp.dto.response.CacheShopTimeSlotDto;
import org.swp.entity.Booking;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.Shop;
import org.swp.entity.ShopTimeSlot;
import org.swp.enums.BookingStatus;
import org.swp.enums.UserRole;
import org.swp.repository.*;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private IBookingRepository bookingRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private IShopRepository shopRepository;
    @Autowired
    private ICacheShopTimeSlotRepository cacheShopTimeSlotRepository;
    @Autowired
    private IShopTimeSlotRepository shopTimeSlotRepository;


    public Object getAllBookings(String userName) {
      //todo: check input of the endpoint then query base on role and the username
       return isCustomer(userName) ?
                bookingRepository.findALlByCustomerUserName(userName)
                : bookingRepository.findAllByShopOwnerUserName(userName);
    }

    private boolean isCustomer(String userName) {
        return userRepository.findByUsername(userName).get().getRole().equals(UserRole.CUSTOMER);
    }

    public Object getBookingById(int id) {
        //todo: mapping with dto
        return bookingRepository.findById(id).orElse(null);
    }

    public Object createBooking(RequestBookingRequest request) {
        Booking boking = modelMapper.map(request, Booking.class);
        return bookingRepository.save(boking);
    }


    public Object cancel(RequestCancelBookingRequest request) {
        //update the status -> update the cacheshoptimeslot
        return null;

    }

    public Object getSlotInfors(int id, LocalDate date) {
        List<CacheShopTimeSlotDto> dtos = null;
        var service = serviceRepository.findById(id);
        if (service.isPresent()) {
            Shop shop = service.get().getShop();
            if (Objects.nonNull(shop)) {
                //get all the Shop Time Slot Information
                Set<ShopTimeSlot> shopTimeSlot = shopTimeSlotRepository.findByShopId(shop.getId());
                Set<CacheShopTimeSlot> cacheShopTimeSlots = cacheShopTimeSlotRepository.findByShopIdAndDate(shop.getId(), date);
                dtos = new ArrayList<>();
                for (ShopTimeSlot timeSlot : shopTimeSlot) {
                    if (isEmptyTimeSlot(timeSlot, cacheShopTimeSlots)) {
                        dtos.add(new CacheShopTimeSlotDto(shop.getId(), timeSlot.getTotalSlot(), 0, timeSlot.getTotalSlot(), date.atStartOfDay()));
                    } else {
                        CacheShopTimeSlot cacheShopTimeSlot = (cacheShopTimeSlots.stream().filter(c -> c.getShopTimeSlot().equals(timeSlot))).findAny().get();
                        if (Objects.nonNull(cacheShopTimeSlot)) {
                            dtos.add(new CacheShopTimeSlotDto(shop.getId(),
                                    cacheShopTimeSlot.getTotalSlots(),
                                    cacheShopTimeSlot.getUsedSlots(),
                                    cacheShopTimeSlot.getAvailableSlots(),
                                    date.atStartOfDay()));
                        }
                    }
                }


            }
        }
        return dtos;
    }

    private boolean isEmptyTimeSlot(ShopTimeSlot timeSlot, Set<CacheShopTimeSlot> cacheShopTimeSlots) {
        return cacheShopTimeSlots.stream().noneMatch(s -> s.getShopTimeSlot().equals(timeSlot));
    }
}
