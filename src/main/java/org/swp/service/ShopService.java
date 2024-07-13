package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateShopRequest;
import org.swp.dto.request.UpdateShopRequest;
import org.swp.dto.response.*;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.repository.IBookingRepository;
import org.swp.repository.ICacheShopTimeSlotRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private JWTService jwtService;

    @Autowired
    private IBookingRepository bookingRepository;

    @Autowired
    private ICacheShopTimeSlotRepository cacheShopTimeSlotRepository;

    public Object getMostRcmdShops(int numberOfRecords) {
//        return shopRepository.findMostRcmdShops(numberOfRecords);
        return shopRepository.findAll();
    }

    //map to shop detail dto
    private ShopDetailDto mapToDto(Shop shopEntity) {
        ModelMapper localModelMapper = new ModelMapper();
        localModelMapper.typeMap(LocalDateTime.class, LocalTime.class).setConverter(context ->
                context.getSource() != null ? context.getSource().toLocalTime() : null
        );

        ShopDetailDto dto = localModelMapper.map(shopEntity, ShopDetailDto.class);

        // Ensure the openTime and closeTime are mapped as LocalTime
        dto.setOpenTime(shopEntity.getOpenTime().toLocalTime());
        dto.setCloseTime(shopEntity.getCloseTime().toLocalTime());
        return dto;
    }


    public List<ShopDetailDto> getAllShops() {
        return shopRepository.findAllShops().stream()
                .map(shop -> {
                    ShopDetailDto dto = mapToDto(shop);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //get shop for shop owner
    public Object getShopDetail(String token) {
        String username = jwtService.getUserNameFromToken(token);
        Shop shop = shopRepository.findByUserName(username);
        if (shop.isDeleted() == true) {
            return "Shop is deleted!";
        }
        return mapToDto(shop);
    }

    public Object createShop(CreateShopRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }
        LocalDateTime openDateTime = LocalDateTime.of(LocalDate.now(), request.getOpenTime());
        LocalDateTime closeDateTime = LocalDateTime.of(LocalDate.now(), request.getCloseTime());
        Shop shop = modelMapper.map(request, Shop.class);
        shop.setUser(user);
        shop.setCloseTime(closeDateTime);
        shop.setOpenTime(openDateTime);
        Shop savedShop = shopRepository.save(shop);
        return modelMapper.map(savedShop, ShopDetailDto.class);
    }

    public Object updateShop(UpdateShopRequest request, String token) {
        Shop shop = shopRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Shop not found with id: " + request.getId()));
        if (!shop.getUser().getUsername().equals(jwtService.getUserNameFromToken(token)))
            throw new RuntimeException("User not shop owner");

        LocalDateTime openDateTime = LocalDateTime.of(LocalDate.now(), request.getOpenTime());
        LocalDateTime closeDateTime = LocalDateTime.of(LocalDate.now(), request.getCloseTime());
        shop.setCloseTime(closeDateTime);
        shop.setOpenTime(openDateTime);
        modelMapper.map(request, shop);
        shopRepository.save(shop);
        return mapToDto(shop);
    }

    public Object deleteShop(int id, String token) {
        Shop shop = shopRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Shop not found with id: " + id));
        String userName = jwtService.getUserNameFromToken(token);
        if (!userName.equals(shop.getUser().getUsername()) || shop.isDeleted())
            throw new RuntimeException("User not shop owner/ shop is deleted");
        shop.setDeleted(true);
        shopRepository.save(shop);
        //update all service and booking deleted also
        shopRepository.updateServiceDeleted_ByShopId(id);
        shopRepository.updateNominationDeleted_ByShopId(id);
        shopRepository.updateCacheShopTimeSlotDeleted_ByShopId(id);
        shopRepository.updateBookingDeleted_ByShopId(id);
        shopRepository.updateShopTimeSlotDeleted_ByShopId(id);
        shopRepository.updateFeedBack_ByShopId(id);
        shopRepository.updateFeedbackReplyDeleted_ByShopId(id);
        shopRepository.updateReFerPrice_DeletedByShopId(id);
        return "Deleted";
    }

    public Object getShopDetailById(int id) {
        Shop shop = shopRepository.findById(id).get();
        if (shop.isDeleted() == true) {
            return "Shop is deleted!";
        }
        return mapToDto(shop);
    }


    public Object getShopId(String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return shopRepository.getShopIdFromUserName(userName);
    }


    public Object getDashboardOfShop(String token) {
        String userName = jwtService.getUserNameFromToken(token);
        Shop shop = shopRepository.findById(shopRepository.getShopIdFromUserName(userName)).get();

        DashboardDto dto = new DashboardDto();
        dto.setTotalServices(shop.getTotalServices());
        dto.setTotalBookings(bookingRepository.findAllByShopOwnerUserName(userName).size());
        dto.setTotalNominations(shop.getNomination());

        List<Object[]> queryResult = bookingRepository.findMonthlyBookingsOfShop(shop.getId());

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

    public Object getAllInfoTimeSlotByDate(String token, LocalDate date) {
        Integer shopId = (Integer) getShopId(token);
        List<CacheShopTimeSlot> cacheShopTimeSlotList = cacheShopTimeSlotRepository.findByShopIdAndDate(shopId, date);
        return cacheShopTimeSlotList.stream().map(e -> {
            ListItemTimeSlotInfoInDateDto dto = modelMapper.map(e, ListItemTimeSlotInfoInDateDto.class);
            dto.setStartTime(e.getShopTimeSlot().getTimeSlot().getStartLocalDateTime());
            dto.setEndTime(e.getShopTimeSlot().getTimeSlot().getEndLocalDateTime());
            return dto;
        }).collect(Collectors.toList());
    }
}
