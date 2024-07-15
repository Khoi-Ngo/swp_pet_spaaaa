package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swp.dto.request.RequestBookingRequest;
import org.swp.dto.request.RequestCancelBookingRequest;
import org.swp.dto.response.*;
import org.swp.entity.*;
import org.swp.enums.BookingStatus;
import org.swp.enums.UserRole;
import org.swp.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Object getAllBookings(int cacheShopTimeSlotId, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        List<Booking> bookingList = bookingRepository.findAllByShopOwnerUserNameAndTimeSlot(userName, cacheShopTimeSlotId);
        List<BookingListItemDto> dtos = new ArrayList<>();
        bookingList.forEach(b -> {
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

            if (!b.getCacheShopTimeSlot().getShopTimeSlot().isDeleted()) {
                dtos.add(dto);
            }

        });
        return dtos;
    }

    public Object getAllBookings(String token) {
        String userName = jwtService.getUserNameFromToken(token);
        List<Booking> bookingList = isCustomer(userName) ?
                bookingRepository.findALlByCustomerUserName(userName)
                : bookingRepository.findAllByShopOwnerUserName(userName);
        //mapping
        List<BookingListItemDto> dtos = new ArrayList<>();
        bookingList.forEach(b -> {
            if (!isConsiderDeleted(b)) {
                BookingListItemDto dto = modelMapper.map(b, BookingListItemDto.class);

                org.swp.entity.Service service = b.getService();
                dto.setServiceId(service.getId());
                dto.setServiceName(service.getServiceName());
                dto.setServicePhoto(service.getServicePhoto());

                Shop shop = b.getShop();
                dto.setShopName(shop.getShopName());
                dto.setShopId(shop.getId());

                User user = b.getUser();
                dto.setCustomerFullName(user.getFirstName() + " " + user.getLastName());

                Pet pet = b.getPet();
                dto.setPetId(pet.getId());
                dto.setPetName(pet.getPetName());
                dto.setPetPhoto(pet.getPetPhoto());

                //local date + time slot
                CacheShopTimeSlot cacheShopTimeSlot = b.getCacheShopTimeSlot();
                dto.setLocalDate(cacheShopTimeSlot.getLocalDate());
                dto.setTimeSlotDto(modelMapper.map(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot(), TimeSlotDto.class));

                dtos.add(dto);
            }
        });
        return dtos;
    }

    private boolean isConsiderDeleted(Booking booking) {
        //any table relating to Booking if deleted then the booking will be considered to be deleted also.
        return booking.getService().isDeleted() || booking.getUser().isDeleted() || booking.getShop().isDeleted()
                || booking.getCacheShopTimeSlot().getShopTimeSlot().isDeleted();
    }

    private boolean isCustomer(String userName) {
        return userRepository.findByUsername(userName).get().getRole().equals(UserRole.CUSTOMER);
    }

    public Object getBookingById(int id) {
        Booking booking = bookingRepository.findById(id).get();
        if (booking.isDeleted()) {
            return "Booking is deleted";
        }
        BookingDetailDto dto = modelMapper.map(booking, BookingDetailDto.class);

        CacheShopTimeSlot cacheShopTimeSlot = booking.getCacheShopTimeSlot();
        dto.setLocalDate(cacheShopTimeSlot.getLocalDate());
        dto.setStartTime(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot().getStartLocalDateTime());
        dto.setEndTime(cacheShopTimeSlot.getShopTimeSlot().getTimeSlot().getEndLocalDateTime());

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
        dto.setPetPhoto(pet.getPetPhoto());
        //shop info
        Shop shop = booking.getShop();
        dto.setShopAddress(shop.getShopAddress());
        dto.setShopId(shop.getId());
        dto.setShopName(shop.getShopName());
        //service info
        org.swp.entity.Service service = booking.getService();
        dto.setServiceId(service.getId());
        dto.setServiceName(service.getServiceName());
        dto.setServicePhoto(service.getServicePhoto());
        return dto;
    }

    @Transactional
    public Object createBooking(RequestBookingRequest request) {
        User customer = userRepository.findById(request.getCustomerId()).get();
        Pet pet = getPet(request, customer);
        var service = serviceRepository.findById(request.getServiceId()).get();
        CacheShopTimeSlot cacheShopTimeSlot = getCacheShopTimeSlot(request.getLocalDate(), request.getTimeSlotDto().getStartLocalDateTime(), request.getTimeSlotDto().getEndLocalDateTime(), service.getShop().getId());

        if (!isValidPet(pet.getId(), cacheShopTimeSlot.getId()))
            throw new RuntimeException("The pet is already booked this timeslot");

        createBooking(request.getAdditionalMessage(), service, customer, pet, cacheShopTimeSlot);
        return "Create booking ok!";
    }

    private boolean isValidPet(Integer petId, Integer cacheId) {
        return Objects.isNull(bookingRepository.findAnyPetScheduled(petId, cacheId));
    }

    private Pet getPet(RequestBookingRequest request, User customer) {
        if (Objects.nonNull(request.getPetId())) {
            return petrepository.findById(request.getPetId()).get();
        }
        Pet pet = modelMapper.map(request, Pet.class);
        pet.setUser(customer);
        return petrepository.save(pet);
    }

    private void createBooking(String additionalMessage, org.swp.entity.Service service,
                               User customer, Pet pet, CacheShopTimeSlot cacheShopTimeSlot) {
        Booking booking = new Booking();
        booking.setBookingNote(additionalMessage);
        booking.setStatus(BookingStatus.SCHEDULED.name());
        booking.setShop(service.getShop());
        booking.setService(service);
        booking.setUser(customer);
        booking.setPet(pet);
        booking.setCacheShopTimeSlot(cacheShopTimeSlot);
        bookingRepository.save(booking);
    }

    private CacheShopTimeSlot getCacheShopTimeSlot(LocalDate localDate, LocalTime startLocalTime, LocalTime endLocalTime, Integer shopId) {
        TimeSlot timeSlot = timeSlotRepository.findByStartAndEnd(startLocalTime, endLocalTime);
        ShopTimeSlot shopTimeSlot = shopTimeSlotRepository.findByShopIdAndTimeSlot(shopId, timeSlot.getStartLocalDateTime(), timeSlot.getEndLocalDateTime());
        CacheShopTimeSlot cacheShopTimeSlot = cacheShopTimeSlotRepository
                .findByShopDateAndTimeSlot(shopId, localDate, shopTimeSlot);
        cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() + 1);
        cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() - 1);
        return cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
    }

    public Object cancel(@NotNull RequestCancelBookingRequest request, String token) {
        Booking booking = bookingRepository.findById(request.getBookingId()).get();
        if (!doInvoleBooking(booking, token) || BookingStatus.CANCELLED.name().equals(booking.getStatus()))
            throw new RuntimeException("User not invole the booking/ booking is deleted before");
        booking.setStatus(BookingStatus.CANCELLED.name());
        booking.setAdditionalMessage(request.getAdditionalMessage());
        CacheShopTimeSlot cacheShopTimeSlot = booking.getCacheShopTimeSlot();
        cacheShopTimeSlot.setAvailableSlots(cacheShopTimeSlot.getAvailableSlots() + 1);
        cacheShopTimeSlot.setUsedSlots(cacheShopTimeSlot.getUsedSlots() - 1);
        cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
        bookingRepository.save(booking);
        return "Canceled";
    }


    public Object markBooking(int bookingId, BookingStatus bookingStatus, String token) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!doInvoleBooking(booking, token) || isOverdueBooking(booking)) throw new RuntimeException("Cannot mark");
        booking.setStatus(bookingStatus.name());
        bookingRepository.save(booking);
        return "Booking marked!";
    }

    private boolean isOverdueBooking(Booking booking) {
        return (booking.getCacheShopTimeSlot().getLocalDate().isAfter(LocalDate.now()))
                || (booking.getCacheShopTimeSlot().getLocalDate().isEqual(LocalDate.now())
                && booking.getCacheShopTimeSlot().getShopTimeSlot().getTimeSlot().getEndLocalDateTime().isAfter(LocalTime.now())
        );
    }

    private boolean doInvoleBooking(Booking booking, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        Integer role = userRepository.findRoleByUserName(userName); // just need role
        return role.equals(UserRole.CUSTOMER.getValue()) ?
                booking.getUser().getUsername().equals(userName) :
                booking.getShop().getUser().getUsername().equals(userName);
    }

    public void trackBookingStatus(LocalDateTime now) {
        List<Booking> bookingList = bookingRepository.findAllScheduledBoookingsAndLock(now.toLocalTime(), now.toLocalDate());
        bookingList.forEach(b -> b.setStatus(BookingStatus.NEED_CONFIRM.name()));
        bookingRepository.saveAll(bookingList);
    }

}
