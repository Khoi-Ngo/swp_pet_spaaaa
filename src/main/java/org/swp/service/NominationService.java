package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.NomiCreateRequest;
import org.swp.dto.request.NominationDeleteRequest;
import org.swp.dto.response.AggregateNominationListItemDto;
import org.swp.dto.response.NominationServiceListItemDto;
import org.swp.dto.response.NominationShopListItemDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.entity.other.Nomination;
import org.swp.repository.INominationRepository;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NominationService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private INominationRepository nominationRepository;
    @Autowired
    private IShopRepository shopRepository;
    @Autowired
    private ModelMapper modelMapper;

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }

    public Object createNomination(String token, NomiCreateRequest request) {//todo: this action need refresh page if no using websocket
        String userName = getUserNameFromToken(token);
        User user = userRepository.findByUsername(userName).get();
        Nomination nomination = new Nomination(user, request.getNominationType());
        if (Objects.nonNull(request.getServiceId())) {
            org.swp.entity.Service service = serviceRepository.findById(request.getServiceId()).get();
            nomination.setService(service);
            service.setNomination(service.getNomination() + request.getNominationType().getValue());
            serviceRepository.save(service);
        } else if (Objects.nonNull(request.getShopId())) {
            Shop shop = shopRepository.findById(request.getShopId()).get();
            nomination.setShop(shop);
            shop.setNomination(shop.getNomination() + request.getNominationType().getValue());
            shopRepository.save(shop);
        }
        nominationRepository.save(nomination);
        return "Create nomination successfully";
    }

    public Object deleteNomination(String token, NominationDeleteRequest request) {//todo: this action need refresh page if no using websocket
        String userName = getUserNameFromToken(token);
        User user = userRepository.findByUsername(userName).get();
        Nomination nomination = null;

        if (Objects.nonNull(request.getServiceId())) {
            nomination = nominationRepository.findByServiceIdAndUserId(request.getServiceId(), user.getId());
            org.swp.entity.Service service = nomination.getService();
            service.setNomination(service.getNomination() - request.getNominationType().getValue());
            serviceRepository.save(service);
        } else if (Objects.nonNull(request.getShopId())) {
            nomination = nominationRepository.findByShopIdAndUserId(request.getShopId(), user.getId());
            Shop shop = nomination.getShop();
            shop.setNomination(shop.getNomination() - request.getNominationType().getValue());
            shopRepository.save(shop);
        }
        nominationRepository.delete(nomination);
        return "Remove nomination successfully";
    }

    public Object getNominationHistory(String token) {
        String userName = getUserNameFromToken(token);
        Integer userId = userRepository.findByUsername(userName).get().getId();
        List<Nomination> nominationList = nominationRepository.findAllByUserId(userId);
        AggregateNominationListItemDto dto = getEmptyNominationDto();
        nominationList.forEach(entity ->
                {
                    if (Objects.nonNull(entity.getService())) {
                        dto.getNomiServiceList().add(createNominationServiceListItemDto(entity));
                    } else {
                        dto.getNomiShopList().add(createNominationShopListItemDto(entity));
                    }
                }
        );
        return dto;
    }

    private AggregateNominationListItemDto getEmptyNominationDto() {
        return new AggregateNominationListItemDto(new ArrayList<>(), new ArrayList<>());
    }

    private NominationServiceListItemDto createNominationServiceListItemDto(Nomination nomination) {
        NominationServiceListItemDto dto = modelMapper.map(nomination, NominationServiceListItemDto.class);
        dto.setServiceName(nomination.getService().getServiceName());
        dto.setServiceId(nomination.getService().getId());
        dto.setShopName(nomination.getShop().getShopName());
        return dto;
    }

    private NominationShopListItemDto createNominationShopListItemDto(Nomination nomination) {
        NominationShopListItemDto dto = modelMapper.map(nomination, NominationShopListItemDto.class);
        dto.setShopId(nomination.getShop().getId());
        dto.setShopName(nomination.getShop().getShopName());
        return dto;
    }
}
