package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.PetDetailDto;
import org.swp.dto.response.PetListItemDto;
import org.swp.entity.Booking;
import org.swp.entity.Pet;
import org.swp.entity.User;
import org.swp.enums.BookingStatus;
import org.swp.enums.UserRole;
import org.swp.repository.IBookingRepository;
import org.swp.repository.IPetrepository;
import org.swp.repository.IUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private IPetrepository petrepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IBookingRepository bookingRepository;
    @Autowired
    private IUserRepository userRepository;

    //get all pet for customer
    public Object getAllPets(String token) {
        String username = getUserNameFromToken(token);
        List<Pet> pets;
        pets = isAdmin(username) ?
                petrepository.findAll() :
                petrepository.findByUserName(username);
        return mapToDto(pets);
    }



    //get pet detail

    //create pet

    //update pet info

    //delete pet


    //map to list item pet dto
    private List<PetListItemDto> mapToDto(List<Pet> pets) {
        if (Objects.isNull(pets)) {
            return Collections.emptyList();
        }
        return pets.stream().map(petEntity -> {
            PetListItemDto dto = modelMapper.map(petEntity, PetListItemDto.class);
            dto.setOwnerId(petEntity.getUser().getId());
            dto.setOwnerName(petEntity.getUser().getFirstName() + petEntity.getUser().getLastName());
            List<Booking> bookings = bookingRepository.findByPetIdAndStatus(petEntity.getId(), BookingStatus.SCHEDULED.name());
            boolean doHaveUpcomingSchedule = true;
            if (Objects.isNull(bookings) || bookings.isEmpty()) doHaveUpcomingSchedule = false;
            dto.setDoHaveUpcomingSchedule(doHaveUpcomingSchedule);
            return dto;
        }).collect(Collectors.toList());

    }

    //map to pet detail dto
//    private PetDetailDto mapToDto(Pet petEntity) {
//        PetDetailDto dto = modelMapper.map(petEntity, PetDetailDto.class);
//        //mapping with Booking History
//        return null;
//    }

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }

    private boolean isAdmin(String username){
        User user = userRepository.findByUsername(username).get();
        return UserRole.ADMIN.equals(user.getRole());
    }


}
