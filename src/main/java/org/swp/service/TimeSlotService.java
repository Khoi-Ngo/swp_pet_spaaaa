package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ListTimeSlotDto;
import org.swp.entity.TimeSlot;
import org.swp.repository.ITimeSlotRepository;

import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITimeSlotRepository timeSlotRepository;

    public Object getAllTimeSlot() {
        return timeSlotRepository.findAll().stream()
                .filter(timeSlot -> !timeSlot.isDeleted())
                .map(timeSlot -> {
                    ListTimeSlotDto dto = modelMapper.map(timeSlot, ListTimeSlotDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
