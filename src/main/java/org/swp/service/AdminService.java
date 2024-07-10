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

    public List<ListAccountShopOwnerDto> getAllShopOwner(){
        return adminRepository.findAllShopOwnerAcc().stream()
                .map(user ->{
                    ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
                    User shopOwner = userRepository.findById(dto.getId()).get();
                    dto.setStatus(shopOwner.isDeleted());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<ListAccountCustomerDto> getAllCustomer() {
        return adminRepository.findAllCustomerACC().stream()
                .map(service -> {
                    ListAccountCustomerDto dto = modelMapper.map(service, ListAccountCustomerDto.class);
                    User user = userRepository.findById(dto.getId()).get();
                    dto.setStatus(user.isDeleted());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public User addShopOwner(@NotNull SignUpRequest signUpRequest, String token) {
        String userName = jwtService.getUserNameFromToken(token);

        if (!isAdmin(userName)) {
            throw new IllegalArgumentException("You do not have permission to use this function");
        }
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = modelMapper.map(signUpRequest, User.class);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(UserRole.SHOP_OWNER);
        user.setCreatedTime(LocalDateTime.now());
        user.setPhone(signUpRequest.getPhone());
        return userRepository.save(user);
    }

    public Object deleteUserById(int id, String token) {
        String userName = jwtService.getUserNameFromToken(token);

        if(userRepository.findById(id).isEmpty()){
            return "user not found";
        }

        if (!isAdmin(userName)) {
            throw new IllegalArgumentException("You do not have permission to use this function");
        }

        User user = userRepository.findById(id).get();
        user.setDeleted(true);
        userRepository.save(user);
        ListAccountShopOwnerDto dto = modelMapper.map(user, ListAccountShopOwnerDto.class);
        dto.setStatus(true);
        return dto;
    }

    public Object viewAccById(int id){
        User user = userRepository.findById(id).get();
        if (user.isDeleted()){
            return "user is deleted";
        }
        DetailAccountDto dto = modelMapper.map(user, DetailAccountDto.class);
        dto.setStatus(user.isDeleted());
        return dto;
    }

    public Object getDashboardOfAdmin(){
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
        TotalAccountDto dto = new TotalAccountDto();
        dto.setTotalAccount(adminRepository.countTotalCustomer());
        return dto;
    }


    public Object getTotalAccountShopOwner() {
        TotalAccountDto dto = new TotalAccountDto();
        dto.setTotalAccount(adminRepository.countTotalShopOwners());
        return dto;
    }
}
