package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.swp.dto.request.RequestBookingRequest;
import org.swp.dto.request.RequestCancelBookingRequest;
import org.swp.dto.response.BookingDetailDto;
import org.swp.dto.response.CacheShopTimeSlotDto;
import org.swp.dto.response.CancelBookingDto;
import org.swp.dto.response.CreateBookingDto;
import org.swp.entity.*;
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
        Booking booking = bookingRepository.findById(id).orElse(null);
        return modelMapper.map(booking, BookingDetailDto.class);
    }

    public Object createBooking(RequestBookingRequest request) {
        var service = serviceRepository.findById(request.getServiceId());
        CreateBookingDto dto = null;
        if (service.isPresent()) {
            Shop shop = service.get().getShop();
            if (Objects.isNull(shop)) {
                return null;
            }
            CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository.findByShopDateAndTimeSlot(
                    shop.getId()
                    , request.getLocalDate()
                    , request.getTimeSlot());
            if (Objects.nonNull(cacheShopTimeSlot)) {
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() + 1);
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() - 1);
                if (cacheShopTimeSlot.getTotalSlots() < cacheShopTimeSlot.getAvailableSlots() + cacheShopTimeSlot.getUsedSlots()) {
                    return null;
                }
            } else {
                //find shop time slot original by ShopIdAndTimeSlot
                ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findByShopIdAndTimeSlot(
                        shop.getId()
                        , request.getTimeSlot());

                //refer into -> Cache record and save
                cacheShopTimeSlot = new CacheShopTimeSlot();
                cacheShopTimeSlot.setTotalSlots(shopTimeSlot.getTotalSlot());
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() > 0 ? cacheShopTimeSlot.getUsedSlots() + 1 : 1);
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getTotalSlots() - cacheShopTimeSlot.getUsedSlots());
                cacheShopTimeSlot.setLocalDateTime(request.getLocalDate().atStartOfDay());
                cacheShopTimeSlot.setShop(shop);
                cacheShopTimeSlot.setShopTimeSlot(shopTimeSlot);
            }
            //create booking here
            Booking booking = new Booking();
            booking.setBookingNote(request.getAdditionalMessage());
            booking.setDone(false);
            booking.setCanceled(false);
            booking.setStatus(BookingStatus.SCHEDULED.getDescription());
            booking.setShop(shop);
            booking.setService(service.get());
            booking.setCacheShopTimeSlot(cacheShopTimeSlot);

            User customer = userRepository.findById(request.getCustomerId()).get();
            booking.setUser(customer);
            List<Booking> bookings = cacheShopTimeSlot.getBookings();
            if (CollectionUtils.isEmpty(bookings)) {
                bookings = new ArrayList<>();
            }
            bookings.add(booking);
            cacheShopTimeSlot.setBookings(bookings);
            //save

            cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
            bookingRepository.save(booking);
            dto = modelMapper.map(request, CreateBookingDto.class);

        }
        return dto;
    }


    public Object cancel(@NotNull RequestCancelBookingRequest request) {
        var booking = bookingRepository.findById(request.getBookingId());
        CancelBookingDto dto = null;
        if (booking.isPresent()) {
            dto = new CancelBookingDto();
            dto.setBookingDetailDto(modelMapper.map(booking, BookingDetailDto.class));
            dto.setAdditionalMessage(request.getAdditionalMessage());
            Booking entity = booking.get();
            entity.setCanceled(true);
            entity.setDone(false);
            entity.setStatus(BookingStatus.CANCELLED.getDescription());
            CacheShopTimeSlot cacheShopTimeSlot = entity.getCacheShopTimeSlot();
            if (Objects.nonNull(cacheShopTimeSlot)) {
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() + 1);
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() - 1);
            }
            bookingRepository.save(entity);

        }
        return dto;
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
                        dtos.add(new CacheShopTimeSlotDto(shop.getId(),
                                timeSlot.getTotalSlot(),
                                0,
                                timeSlot.getTotalSlot(),
                                date.atStartOfDay(),
                                timeSlot.getTimeSlot()));
                    } else {
                        CacheShopTimeSlot cacheShopTimeSlot = (cacheShopTimeSlots.stream().filter(c -> c.getShopTimeSlot().equals(timeSlot))).findAny().get();
                        if (Objects.nonNull(cacheShopTimeSlot)) {
                            dtos.add(new CacheShopTimeSlotDto(shop.getId(),
                                    cacheShopTimeSlot.getTotalSlots(),
                                    cacheShopTimeSlot.getUsedSlots(),
                                    cacheShopTimeSlot.getAvailableSlots(),
                                    date.atStartOfDay(),
                                    timeSlot.getTimeSlot()));
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
