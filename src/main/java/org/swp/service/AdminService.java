package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.configuration.constant.MonthConstant;
import org.swp.dto.request.SignUpRequest;
import org.swp.dto.response.*;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class AdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IPetrepository petrepository;

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private INominationRepository nominationRepository;


    public List<ListAccountShopOwnerDto> getAllShopOwner() {
        return adminRepository.findAllShopOwnerAcc().stream()
                .map(user -> modelMapper.map(user, ListAccountShopOwnerDto.class))
                .collect(Collectors.toList());
    }

    public List<ListAccountCustomerDto> getAllCustomer() {
        return adminRepository.findAllCustomerAcc().stream()
                .map(service -> modelMapper.map(service, ListAccountCustomerDto.class))
                .collect(Collectors.toList());
    }

    public Object addShopOwner(@NotNull SignUpRequest signUpRequest, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        if(userRepository.findByEmail(signUpRequest.getEmail()).isPresent()){
            return "Email already exists";
        }
        if (!isAdmin(userName)) {
            throw new IllegalArgumentException("You do not have permission to use this function");
        }
        User user = modelMapper.map(signUpRequest, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.SHOP_OWNER);
        user.setCreatedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Created";
    }

    public Object deleteUserById(int id, String token) {
        User user = userRepository.findById(id).get();
        if (Objects.isNull(user) || user.isDeleted() == true) {
            return "user not found / deleted";
        }
        if (!isAdmin(jwtService.getUserNameFromToken(token))) {
            throw new IllegalArgumentException("You do not have permission to use this function");
        }

        if (user.getRole().equals(UserRole.SHOP_OWNER)) {
            Shop shop = shopRepository.findByShopOwnerId(user.getId());
            if (shop != null && !shop.isDeleted()) {
                shop.setDeleted(true);
                shopRepository.save(shop);
                shopRepository.updateServiceDeleted_ByShopId(id);
                shopRepository.updateNominationDeleted_ByShopId(id);
                shopRepository.updateCacheShopTimeSlotDeleted_ByShopId(id);
                shopRepository.updateBookingDeleted_ByShopId(id);
                shopRepository.updateShopTimeSlotDeleted_ByShopId(id);
                shopRepository.updateFeedBack_ByShopId(id);
                shopRepository.updateFeedbackReplyDeleted_ByShopId(id);
                shopRepository.updateReFerPrice_DeletedByShopId(id);
            }
        }
        if(user.getRole().equals(UserRole.CUSTOMER)){
            petrepository.deletedAllPetByUserId(user.getId());
            feedbackRepository.deletedAllFeedBackByUserId(user.getId());
            nominationRepository.deletedAllNominationByUserId(user.getId());
        }
        user.setDeleted(true);
        userRepository.save(user);
        return "Deleted";
    }

    public Object viewAccById(int id) {
        User user = userRepository.findById(id).get();
        if (user.isDeleted()) {
            return "User is deleted";
        }
        return modelMapper.map(user, DetailAccountDto.class);
    }

    public Object getDashboardOfAdmin() {

        Map<String, Integer> monthlyBookingMap = getMonthlyBookingMap();

        List<MonthlyBookingDto> monthlyBookings = new ArrayList<>();
        IntStream.rangeClosed(MonthConstant.JANUARY, MonthConstant.DECEMBER).forEach(month -> {
            YearMonth ym = YearMonth.of(LocalDate.now().getYear(), month);
            String monthStr = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthlyBookings.add(new MonthlyBookingDto(monthStr, monthlyBookingMap.getOrDefault(monthStr, 0)));
        });

        return new AdminDashboardDto(adminRepository.countTotalShops()
                , adminRepository.countTotalServices()
                , adminRepository.countTotalCustomer()
                , adminRepository.countTotalBookings()
                , adminRepository.countTotalPets()
                , monthlyBookings);
    }

    private Map<String, Integer> getMonthlyBookingMap() {
        return adminRepository.findMonthlyBookings().stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> ((Number) result[1]).intValue()
                ));
    }

    private boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username).get();
        return UserRole.ADMIN.equals(user.getRole());
    }

    public Object getTotalAccountCustomer() {
        return new TotalAccountDto(adminRepository.countTotalCustomer());
    }


    public Object getTotalAccountShopOwner() {
        return new TotalAccountDto(adminRepository.countTotalShopOwners());
    }
}
