package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.NominationRequest;
import org.swp.dto.response.NominationDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.entity.other.Nomination;
import org.swp.enums.NominationType;
import org.swp.repository.INominationRepository;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class NominationService {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IShopRepository shopRepository;
    @Autowired
    private INominationRepository nominationRepository;

    public boolean createNomination(String token, NominationRequest nominationRequest) {
        try {
            String username = getUserNameFromToken(token);
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return false;
            }

            org.swp.entity.Service service = serviceRepository.findById(nominationRequest.getServiceId()).orElse(null);
            Shop shop = shopRepository.findById(service.getShop().getId()).orElse(null);
            NominationType nominationType = NominationType.fromValue(nominationRequest.getRating());
            Nomination nomination = modelMapper.map(nominationRequest, Nomination.class);
            nomination.setUser(user);
            nomination.setService(service);
            nomination.setShop(shop);
            nomination.setNominationType(nominationType);
            nominationRepository.save(nomination);

            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public Object getAllNominationOfService(int serviceId) {
        return nominationRepository.findAllByServiceId(serviceId).stream()
                .map(nomination -> {
                    NominationDto dto = modelMapper.map(nomination, NominationDto.class);
                    dto.setUserName(
                            Objects.nonNull(nomination.getUser()) ?
                                    nomination.getUser().getUsername()
                                    : "Khong xac dinh"
                    );
                    dto.setServiceName(
                            Objects.nonNull(nomination.getService()) ?
                                    nomination.getService().getServiceName()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(nomination.getShop()) ?
                                    nomination.getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    dto.setRating(
                            Objects.nonNull(nomination.getNominationType()) ?
                                    nomination.getNominationType().getValue()
                                    : 3 //neu ko co thi mac dinh la tot nhat
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getAllNominationOfShop(int shopId) {
        return nominationRepository.findAllByShopId(shopId).stream()
                .map(nomination -> {
                    NominationDto dto = modelMapper.map(nomination, NominationDto.class);
                    dto.setUserName(
                            Objects.nonNull(nomination.getUser()) ?
                                    nomination.getUser().getUsername()
                                    : "Khong xac dinh"
                    );
                    dto.setServiceName(
                            Objects.nonNull(nomination.getService()) ?
                                    nomination.getService().getServiceName()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(nomination.getShop()) ?
                                    nomination.getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    dto.setRating(
                            Objects.nonNull(nomination.getNominationType()) ?
                                    nomination.getNominationType().getValue()
                                    : 3 //neu ko co thi mac dinh la tot nhat
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }

}
