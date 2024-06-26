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
import org.swp.dto.response.*;
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
    @Autowired
    private ITimeSlotRepository timeSlotRepository;
    @Autowired
    private IPetrepository petrepository;
    @Autowired
    private JWTService jwtService;

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }


    public Object getAllBookings(String token) {
        String userName = getUserNameFromToken(token);
        List<Booking> res = isCustomer(userName) ?
                bookingRepository.findALlByCustomerUserName(userName)
                : bookingRepository.findAllByShopOwnerUserName(userName);
        //mapping
        List<BookingListItemDto> dtos = new ArrayList<>();
        res.forEach(b -> {
            BookingListItemDto dto = modelMapper.map(b, BookingListItemDto.class);


            org.swp.entity.Service service = b.getService();
            if (service != null) {
                dto.setServiceId(service.getId());
                dto.setServiceName(service.getServiceName());
            }

            Shop shop = b.getShop();
            if (shop != null) {
                dto.setShopName(shop.getShopName());
                dto.setShopId(shop.getId());
            }

            User user = b.getUser();
            if (user != null) {
                dto.setCustomerFullName(user.getFirstName() + " " + user.getLastName());
            }

            Pet pet = b.getPet();
            if (pet != null) {
                dto.setPetId(pet.getId());
                dto.setPetName(pet.getPetName());
            }

            //local date + time slot
            CacheShopTimeSlot cacheShopTimeSlot = b.getCacheShopTimeSlot();
            if (cacheShopTimeSlot != null) {
                dto.setLocalDate(cacheShopTimeSlot.getLocalDate());
                dto.setTimeSlotDto(modelMapper.map(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot(), TimeSlotDto.class));

            }


            dtos.add(dto);
        });
        return dtos;
    }

    private boolean isCustomer(String userName) {
        return userRepository.findByUsername(userName).get().getRole().equals(UserRole.CUSTOMER);
    }


    private boolean isShopOwner(String userName) {
        return userRepository.findByUsername(userName).get().getRole().equals(UserRole.SHOP_OWNER);
    }

    public Object getBookingById(int id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        BookingDetailDto dto = modelMapper.map(booking, BookingDetailDto.class);
        CacheShopTimeSlot cacheShopTimeSlot = booking.getCacheShopTimeSlot();
        dto.setLocalDate(cacheShopTimeSlot.getLocalDate());
        dto.setStartTime(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot().getStartLocalDateTime());
        dto.setEndTime(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot().getEndLocalDateTime());
        dto.setBookingNote(booking.getBookingNote());

        //user info
        User user = booking.getUser();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserId(user.getId());
        dto.setUserName(user.getUsername());
        //pet info
        Pet pet = booking.getPet();
        dto.setPetName(pet.getPetName());
        dto.setTypePet(pet.getPetType());
        dto.setPetWeight(pet.getPetWeight());
        //shop info
        Shop shop = booking.getShop();
        dto.setShopAddress(shop.getShopAddress());
        dto.setShopId(shop.getId());
        dto.setShopName(shop.getShopName());
        //service info
        org.swp.entity.Service service = booking.getService();
        dto.setServiceId(service.getId());
        dto.setServiceName(service.getServiceName());
        return dto;

    }

    public Object createBooking(RequestBookingRequest request) {
        var service = serviceRepository.findById(request.getServiceId());
        if (service.isPresent()) {
            Shop shop = service.get().getShop();
            if (Objects.isNull(shop)) {
                return null;
            }

            TimeSlot timeSlot = timeSlotRepository.findByStartAndEnd(request.getTimeSlotDto().getStartLocalDateTime(), request.getTimeSlotDto().getEndLocalDateTime());
            ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findByShopIdAndTimeSlot(shop.getId(), timeSlot);


            CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository.findByShopDateAndTimeSlot(
                    shop.getId()
                    , request.getLocalDate()
                    , shopTimeSlot);
            if (Objects.nonNull(cacheShopTimeSlot)) {
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() + 1);
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() - 1);
                if (cacheShopTimeSlot.getTotalSlots() < cacheShopTimeSlot.getAvailableSlots() + cacheShopTimeSlot.getUsedSlots()) {
                    return null;
                }
            } else {
                //refer into -> Cache record and save
                cacheShopTimeSlot = new CacheShopTimeSlot();
                cacheShopTimeSlot.setTotalSlots(shopTimeSlot.getTotalSlot());
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() > 0 ? cacheShopTimeSlot.getUsedSlots() + 1 : 1);
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getTotalSlots() - cacheShopTimeSlot.getUsedSlots());
                cacheShopTimeSlot.setLocalDate(request.getLocalDate());
                cacheShopTimeSlot.setShop(shop);
                cacheShopTimeSlot.setShopTimeSlot(shopTimeSlot);
            }

            //create booking here
            Booking booking = new Booking();
            booking.setBookingNote(request.getAdditionalMessage());
            booking.setDone(false);
            booking.setCanceled(false);
            booking.setStatus(BookingStatus.SCHEDULED.name());
            booking.setShop(shop);
            booking.setService(service.get());

            //pet also
            Pet pet = null;
            if (Objects.nonNull(request.getPetId())) {
                pet = petrepository.findById(request.getPetId()).get();
            }
            if (Objects.isNull(pet)) {
                pet = modelMapper.map(request, Pet.class);
            }


            User customer = userRepository.findById(request.getCustomerId()).get();
            pet.setUser(customer);
            petrepository.save(pet);
            booking.setUser(customer);
            booking.setPet(pet);
            bookingRepository.save(booking);

            List<Booking> bookings = cacheShopTimeSlot.getBookings();
            if (CollectionUtils.isEmpty(bookings)) {
                bookings = new ArrayList<>();
            }
            bookings.add(booking);
            cacheShopTimeSlot.setBookings(bookings);
            //save
            cacheShopTimeSlotRepository.save(cacheShopTimeSlot);


            booking.setCacheShopTimeSlot(cacheShopTimeSlot);
            bookingRepository.save(booking);
            return "Create booking ok!";


        }
        return "Cannot find the service";
    }


    public Object cancel(@NotNull RequestCancelBookingRequest request) {
        var booking = bookingRepository.findById(request.getBookingId());
        if (booking.isPresent()) {
            Booking entity = booking.get();
            entity.setCanceled(true);
            entity.setDone(false);
            entity.setStatus(BookingStatus.CANCELLED.name());
            CacheShopTimeSlot cacheShopTimeSlot = entity.getCacheShopTimeSlot();
            if (Objects.nonNull(cacheShopTimeSlot)) {
                cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() + 1);
                cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() - 1);
            }
            bookingRepository.save(entity);
            cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
        }
        return "Canceled";
    }


    public Object markBooking(int id, BookingStatus bookingStatus) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking entity = booking.get();
            entity.setStatus(bookingStatus.name());
            bookingRepository.save(entity);
            return "Booking marked!";
        }
        return "Booking not found!";
    }
    public Object getAllBookingsByShop(String token) {
        String userName = getUserNameFromToken(token);
        List<Booking> res = isShopOwner(userName) ?
                bookingRepository.findAllByShopOwnerUserName(userName)
                : bookingRepository.findALlByCustomerUserName(userName);
        //mapping
        List<BookingListItemDto> dtos = new ArrayList<>();
        res.forEach(b -> {
            BookingListItemDto dto = modelMapper.map(b, BookingListItemDto.class);
            org.swp.entity.Service service = b.getService();
            if (service != null) {
                dto.setServiceId(service.getId());
                dto.setServiceName(service.getServiceName());
            }
            Shop shop = b.getShop();
            if (shop != null) {
                dto.setShopName(shop.getShopName());
                dto.setShopId(shop.getId());
            }
            User user = b.getUser();
            if (user != null) {
                dto.setCustomerFullName(user.getFirstName() + " " + user.getLastName());
            }
            Pet pet = b.getPet();
            if (pet != null) {
                dto.setPetId(pet.getId());
                dto.setPetName(pet.getPetName());
            }
            //local date + time slot
            CacheShopTimeSlot cacheShopTimeSlot = b.getCacheShopTimeSlot();
            if (cacheShopTimeSlot != null) {
                dto.setLocalDate(cacheShopTimeSlot.getLocalDate());
                dto.setTimeSlotDto(modelMapper.map(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot(), TimeSlotDto.class));

            }
            dtos.add(dto);
        });
        return dtos;
    }

}
