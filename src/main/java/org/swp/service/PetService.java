package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreatePetRequest;
import org.swp.dto.request.UpdatePetRequest;
import org.swp.dto.response.BookingHistoryListItemDto;
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

import java.time.LocalDate;
import java.time.LocalTime;
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
        String username = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username).get();
        if (user.isDeleted()) {
            return "user is deleted";
        }
        List<Pet> pets;
        pets = isAdmin(username) ?
                petrepository.findAllPet() :
                petrepository.findByUserName(username);
        return mapToDto(pets);
    }


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
            boolean doHaveUpcomingSchedule = false;
            LocalDate localDate = null;
            LocalTime startTime = null;
            LocalTime endTime = null;
            if (Objects.nonNull(bookings) && !bookings.isEmpty()) {
                doHaveUpcomingSchedule = true;
                localDate = bookings.get(0).getCacheShopTimeSlot().getLocalDate();
                startTime = bookings.get(0).getCacheShopTimeSlot().getShopTimeSlot().getTimeSlot().getStartLocalDateTime();
                endTime = bookings.get(0).getCacheShopTimeSlot().getShopTimeSlot().getTimeSlot().getEndLocalDateTime();
            }
            dto.setDoHaveUpcomingSchedule(doHaveUpcomingSchedule);
            dto.setNearestBookingDate(localDate);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            return dto;
        }).collect(Collectors.toList());

    }

    //map to pet detail dto
    private PetDetailDto mapToDto(Pet petEntity) {
        PetDetailDto dto = modelMapper.map(petEntity, PetDetailDto.class);
        //mapping with Booking History -> find all booking
        List<Booking> bookings = bookingRepository.findByPetId(dto.getId());
        bookings.forEach(booking -> {
            BookingHistoryListItemDto historyInDate = modelMapper.map(booking, BookingHistoryListItemDto.class);
            historyInDate.setServiceName(booking.getService().getServiceName());
            historyInDate.setServiceId(booking.getService().getId());
            historyInDate.setShopId(booking.getShop().getId());
            historyInDate.setShopName(booking.getShop().getShopName());
            if (dto.getBookingHistory().get(booking.getCacheShopTimeSlot().getLocalDate()) != null) {
                dto.getBookingHistory().get(booking.getCacheShopTimeSlot().getLocalDate()).add(historyInDate);
            } else {
                List<BookingHistoryListItemDto> historyListDate = new ArrayList<>();
                historyListDate.add(historyInDate);
                dto.getBookingHistory().put(
                        booking.getCacheShopTimeSlot().getLocalDate(),
                        historyListDate
                );
            }
        });
        return dto;
    }

    private boolean isAdmin(String username) {
        User user = userRepository.findByUsername(username).get();
        return UserRole.ADMIN.equals(user.getRole());
    }


    public Object getPetDetail(int id) {
        Pet pet = petrepository.findById(id).get();
        if (pet.isDeleted() == true)
            return "pet is deleted!";
        return mapToDto(pet);
    }

    public Object deletePet(int id, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        Pet pet = petrepository.findById(id).get();
        if (!pet.getUser().getUsername().equals(userName) || pet.isDeleted())
            throw new RuntimeException("User not own the pet / pet is deleted");
        pet.setDeleted(true);
        petrepository.save(pet);
        //update booking also
        return "Deleted";
    }

    public Object updatePet(UpdatePetRequest request) {
        Pet pet = petrepository.findById(request.getId()).get();
        if (!pet.getUser().getId().equals(request.getUserId())) throw new RuntimeException("User not own the pet");
        if (request.getPetName() != null) {
            pet.setPetName(request.getPetName());
        }
        if (request.getPetType() != null) {
            pet.setPetType(request.getPetType());
        }
        if (request.getPetAge() != 0) {
            pet.setPetAge(request.getPetAge());
        }
        if (request.getPetGender() != null) {
            pet.setPetGender(request.getPetGender());
        }
        if (request.getPetWeight() != 0) {
            pet.setPetWeight(request.getPetWeight());
        }
        if (request.getPetDescription() != null) {
            pet.setPetDescription(request.getPetDescription());
        }
        if (request.getPetPhoto() != null) {
            pet.setPetPhoto(request.getPetPhoto());
        }
        if (request.getPetNote() != null) {
            pet.setPetNote(request.getPetNote());
        }
        petrepository.save(pet);
        return "Updated";
    }

    public Object createPet(CreatePetRequest request) {
        Pet pet = modelMapper.map(request, Pet.class);
        User user = userRepository.findById(request.getUserId()).get();
        pet.setUser(user);
        petrepository.save(pet);
        return modelMapper.map(pet, PetDetailDto.class);
    }
}
