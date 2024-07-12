package org.swp.service;

import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swp.dto.request.SignUpRequest;
import org.swp.dto.response.*;
import org.swp.entity.User;
import org.swp.enums.UserRole;
import org.swp.repository.IAdminRepository;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public List<ListAccountShopOwnerDto> getAllShopOwner() {
        return adminRepository.findAllShopOwnerAcc().stream()
                .map(user -> modelMapper.map(user, ListAccountShopOwnerDto.class))
                .collect(Collectors.toList());
    }

    public List<ListAccountCustomerDto> getAllCustomer() {
        return adminRepository.findAllCustomerACC().stream()
                .map(service -> modelMapper.map(service, ListAccountCustomerDto.class))
                .collect(Collectors.toList());
    }

    public Object addShopOwner(@NotNull SignUpRequest signUpRequest, String token) {
        String userName = jwtService.getUserNameFromToken(token);

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
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setTotalShop(adminRepository.countTotalShops());
        dto.setTotalServices(adminRepository.countTotalServices());
        dto.setTotalCustomer(adminRepository.countTotalCustomer());
        dto.setTotalBookings(adminRepository.countTotalBookings());
        dto.setTotalPets(adminRepository.countTotalPets());
        List<Object[]> queryResult = adminRepository.findMonthlyBookings();

        List<MonthlyBookingDto> monthlyBookings = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(currentMonth.getYear(), month);
            String monthStr = ym.format(formatter);

            Optional<Object[]> matchingResult = queryResult.stream()
                    .filter(result -> monthStr.equals(result[0]))
                    .findFirst();

            MonthlyBookingDto mbDto = new MonthlyBookingDto();
            mbDto.setMonth(monthStr);
            if (matchingResult.isPresent()) {
                mbDto.setBookings(((Number) matchingResult.get()[1]).intValue());
            } else {
                mbDto.setBookings(0);
            }
            monthlyBookings.add(mbDto);
        }

        dto.setMonthlyBookings(monthlyBookings);
        return dto;
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
