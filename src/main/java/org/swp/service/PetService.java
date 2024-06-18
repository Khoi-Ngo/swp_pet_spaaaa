package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.PetDetailDto;
import org.swp.dto.response.PetListItemDto;
import org.swp.entity.Pet;
import org.swp.repository.IPetrepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class PetService {
    @Autowired
    private IPetrepository petrepository;
    @Autowired
    private ModelMapper modelMapper;

    //get all pet for customer

    //get pet detail

    //create pet

    //update pet info

    //delete pet

    //get all pet for admin

    //map to list item pet dto
    private PetListItemDto mapToDto(Collections petEntities){
        Collection<PetListItemDto> dtos = new ArrayList<>();
        return null;

    }

    //map to pet detail dto
    private PetDetailDto mapToDto(Pet petEntity){
        PetDetailDto dto = modelMapper.map(petEntity, PetDetailDto.class);
        //mapping with Booking History
        return null;
    }


}
