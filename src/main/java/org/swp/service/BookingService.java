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
import org.swp.entity.Booking;
import org.swp.enums.BookingStatus;
import org.swp.enums.UserRole;
import org.swp.repository.IBookingRepository;
import org.swp.repository.IUserRepository;

@Service
public class BookingService {
    @Autowired
    private IBookingRepository bookingRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);


    public Object getAllBookings(String userName) {
        return isCustomer(userName) ?
                bookingRepository.findALlByCustomerUserName(userName)
                : bookingRepository.findAllByShopOwnerUserName(userName);
    }

    private boolean isCustomer(String userName) {
        return userRepository.findByUsername(userName).get().getRole().equals(UserRole.CUSTOMER);
    }

    public Object getBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Object createBooking(RequestBookingRequest request) {
        Booking boking = modelMapper.map(request, Booking.class);
        return bookingRepository.save(boking);
    }


    public Object cancel(RequestCancelBookingRequest request) {
        return null;

    }

    public Object accept(RequestAcceptBooking request) {
        //change status only
        Booking booking = bookingRepository.findById(request.getBookingId()).orElse(null);
        String status = booking.getStatus();
        //WAIT FOR CUSTOMER -> ACCEPTED
        //WAIT FOR SHOP-OWNER -> ACCEPTED
        if (status.equals(BookingStatus.WAIT_FOR_SHOP_OWNER_ACCEPT.getDescription())
                || status.equals(BookingStatus.WAIT_FOR_CUSTOMER_ACCEPT.getDescription())) {
            booking.setStatus(BookingStatus.ACCEPTED.getDescription());
            return "Accept booking successfully";
        }
        return "Unidentified booking status";
    }

    public Object deny(RequestAcceptBooking request) {
        //change status only
        //WAIT FOR CUSTOMER -> DENIED
        //WAIT FOR SHOP-OWNER -> DENIED
        return null;
    }

    public Object markBooking(RequestBookingRequest request) {
        //maybe for shop-owner||customer mark done / having a batch job confirm with the customer || shop-owner
        // => after this call API -> status will be canceled or done
        return null;
    }
}
