package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.ReferPriceCreateRequest;
import org.swp.dto.request.ReferPriceUpdateRequest;
import org.swp.dto.response.ReferPriceDto;
import org.swp.entity.ReferPrice;
import org.swp.repository.IReferPriceRepository;
import org.swp.repository.IServiceRepository;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReferPriceService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private IReferPriceRepository referPriceRepository;

    public Object createReferPrice(ReferPriceCreateRequest request) {
        ReferPrice referPrice = modelMapper.map(request, ReferPrice.class);
        referPrice.setService(serviceRepository.findById(request.getServiceId()).get());
        referPriceRepository.save(referPrice);
        return "Created";
    }

    public Object updateReferPrice(ReferPriceUpdateRequest request, String token) {
        ReferPrice referPrice = referPriceRepository.findById(request.getId()).get();
        if (!referPrice.getService().getShop().getUser().getUsername().equals(jwtService.getUserNameFromToken(token)))
            throw new RuntimeException("User not shop owner");
        referPrice.setMaxWeight(Objects.nonNull(request.getMaxWeight()) ? request.getMaxWeight() : referPrice.getMaxWeight());
        referPrice.setMinWeight(Objects.nonNull(request.getMinWeight()) ? request.getMinWeight() : referPrice.getMinWeight());
        referPrice.setReferPrice(Objects.nonNull(request.getReferPrice()) ? request.getReferPrice() : referPrice.getReferPrice());
        referPriceRepository.save(referPrice);
        return "Updated";
    }

    public Object deleteReferPrice(int id, String token) {
        ReferPrice referPrice = referPriceRepository.findById(id).get();
        if (!referPrice.getService().getShop().getUser().getUsername().equals(jwtService.getUserNameFromToken(token)) || referPrice.isDeleted())
            throw new RuntimeException("User not shop owner/ referprice is deleted");
        referPrice.setDeleted(true);
        referPriceRepository.save(referPrice);
        return "Deleted";
    }

    public Object getReferPrices(int serviceId) {
        return referPriceRepository.findByServiceId(serviceId).stream().map(e -> modelMapper.map(e, ReferPriceDto.class)).collect(Collectors.toList());
    }

}
