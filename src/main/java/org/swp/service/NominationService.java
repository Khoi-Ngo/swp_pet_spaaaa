package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.NomiCreateRequest;
import org.swp.dto.request.NominationDeleteRequest;
import org.swp.dto.response.NominationListItemDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.entity.other.Nomination;
import org.swp.repository.INominationRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NominationService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private INominationRepository nominationRepository;
    @Autowired
    private IShopRepository shopRepository;
    @Autowired
    private ModelMapper modelMapper;

    public Object createNomination(String token, NomiCreateRequest request) {//todo: this action need refresh page if no using websocket
        String userName = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(userName).get();
        Nomination nomination;
        nomination = nominationRepository.findByShopIdAndUserId(request.getShopId(), user.getId());
        if (nomination != null) throw new RuntimeException("Cannot nomi more");

        nomination = new Nomination(user, request.getNominationType());
        Shop shop = shopRepository.findById(request.getShopId()).get();
        nomination.setShop(shop);
        shop.setNomination(shop.getNomination() + request.getNominationType().getValue());
        shopRepository.save(shop);
        nominationRepository.save(nomination);
        return "Create nomination successfully";
    }

    public Object deleteNomination(String token, int nominationId) {//todo: this action need refresh page if no using websocket
        Nomination nomination = nominationRepository.findById(nominationId).get();
        if (!isValidUser(token, nomination) || nomination.isDeleted())
            throw new RuntimeException("No valid user / nomi deleted before");
        nomination.setDeleted(true);
        nominationRepository.save(nomination);
        Shop shop = nomination.getShop();
        shop.setNomination(shop.getNomination() - nomination.getNominationType().getValue());
        shopRepository.save(shop);
        return "Remove nomination successfully";
    }

    public Object getNominationHistory(String token) {
        String userName = jwtService.getUserNameFromToken(token);
        Integer userId = userRepository.findByUsername(userName).get().getId();
        List<Nomination> nominationList = nominationRepository.findAllByUserId(userId);
        List<NominationListItemDto> dtos = new ArrayList<>();
        nominationList.forEach(entity -> dtos.add(createNominationListItemDto(entity)));
        return dtos;
    }

    public Object getAllNominationOfShop(int shopId) {
        List<Nomination> nominations = nominationRepository.findAllByShopId(shopId);
        return nominations.stream()
                .map(this::createNominationListItemDto)
                .collect(Collectors.toList());
    }

    private NominationListItemDto createNominationListItemDto(Nomination nomination) {
        NominationListItemDto dto = modelMapper.map(nomination, NominationListItemDto.class);
        dto.setId(nomination.getId());
        dto.setShopId(nomination.getShop().getId());
        dto.setShopName(nomination.getShop().getShopName());
        dto.setUserName(nomination.getUser().getUsername());
        dto.setNominationType(nomination.getNominationType());
        dto.setUserId(nomination.getUser().getId());
        dto.setUserPhoto(nomination.getUser().getProfileImageUrl());
        return dto;
    }

    private boolean isValidUser(String token, Nomination nomination) {
        return nomination.getUser().getUsername().equals(jwtService.getUserNameFromToken(token));
    }

    public Object getNominationByUserAndShop(String token, Integer shopId) {
        int userId = userRepository.findByUsername(jwtService.getUserNameFromToken(token)).get().getId();
        Nomination nomination = nominationRepository.findByShopIdAndUserId(shopId, userId);
        return createNominationListItemDto(nomination);
    }
}
