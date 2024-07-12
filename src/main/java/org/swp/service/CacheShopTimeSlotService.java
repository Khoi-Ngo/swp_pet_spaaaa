package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.CacheShopTimeSlotDto;
import org.swp.dto.response.TimeSlotDto;
import org.swp.entity.CacheShopTimeSlot;
import org.swp.entity.Shop;
import org.swp.entity.ShopTimeSlot;
import org.swp.repository.ICacheShopTimeSlotRepository;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopTimeSlotRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CacheShopTimeSlotService {

    @Autowired
    private IShopTimeSlotRepository shopTimeSlotRepository;
    @Autowired
    private ICacheShopTimeSlotRepository cacheShopTimeSlotRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IServiceRepository serviceRepository;

    public Object getSlotInfors(int id, LocalDate date) {
        List<CacheShopTimeSlotDto> dtos = null;
        org.swp.entity.Service service = serviceRepository.findById(id).get();
        Shop shop = service.getShop();
        if (Objects.nonNull(shop)) {
            List<ShopTimeSlot> shopTimeSlot = shopTimeSlotRepository.findByShopId(shop.getId());
            List<CacheShopTimeSlot> cacheShopTimeSlots = cacheShopTimeSlotRepository.findByShopIdAndDate(shop.getId(), date);
            dtos = new ArrayList<>();
            for (ShopTimeSlot timeSlot : shopTimeSlot) {
                CacheShopTimeSlot cacheShopTimeSlot = null;

                if (isEmptyTimeSlot(timeSlot, cacheShopTimeSlots)) {
                    //silently creating cacheshoptimeslot here
                    cacheShopTimeSlot = new CacheShopTimeSlot(timeSlot.getTotalSlot(), 0, timeSlot.getTotalSlot(), date, timeSlot, shop);
                    cacheShopTimeSlotRepository.save(cacheShopTimeSlot);
                } else {
                    cacheShopTimeSlot = (cacheShopTimeSlots.stream().filter(c -> c.getShopTimeSlot().equals(timeSlot))).findAny().get();

                }

                dtos.add(new CacheShopTimeSlotDto(
                        cacheShopTimeSlot.getTotalSlots(),
                        cacheShopTimeSlot.getUsedSlots(),
                        cacheShopTimeSlot.getAvailableSlots(),
                        modelMapper.map(timeSlot.getTimeSlot(), TimeSlotDto.class)));
            }
        }
        return dtos;
    }

    private boolean isEmptyTimeSlot(ShopTimeSlot timeSlot, List<CacheShopTimeSlot> cacheShopTimeSlots) {
        return cacheShopTimeSlots.stream().noneMatch(s -> s.getShopTimeSlot().equals(timeSlot));
    }
}
