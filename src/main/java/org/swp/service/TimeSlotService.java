package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateTimeSlotRequest;
import org.swp.dto.request.UpdateTimeSlotRequest;
import org.swp.dto.response.ListTimeSlotDto;
import org.swp.dto.response.TimeSlotDto;
import org.swp.entity.TimeSlot;
import org.swp.repository.ITimeSlotRepository;

import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ITimeSlotRepository timeSlotRepository;

    public Object createTimeSlot(CreateTimeSlotRequest request){
        if (request.getStartLocalDateTime().isAfter(request.getEndLocalDateTime())
                || request.getStartLocalDateTime().equals(request.getEndLocalDateTime())) {
            return "Start time cannot be after or equal to end time";
        }
        TimeSlot timeSlot = modelMapper.map(request, TimeSlot.class);
        timeSlotRepository.save(timeSlot);
        return modelMapper.map(timeSlot, TimeSlotDto.class);
    }

    public Object deleteTimeSlot(int id){
        TimeSlot timeSlot = timeSlotRepository.getById(id);
        timeSlot.setDeleted(true);
        timeSlotRepository.save(timeSlot);
        return modelMapper.map(timeSlot, TimeSlotDto.class);
    }

    public Object updateTimeSlot(UpdateTimeSlotRequest request){
        if (request.getStartLocalDateTime().isAfter(request.getEndLocalDateTime())
                || request.getStartLocalDateTime().equals(request.getEndLocalDateTime())) {
            return "Start time cannot be after or equal to end time";
        }
        TimeSlot timeSlot = modelMapper.map(request, TimeSlot.class);
        timeSlotRepository.save(timeSlot);
        return modelMapper.map(timeSlot, TimeSlotDto.class);
    }

    public Object getAllTimeSlot(){
        return timeSlotRepository.findAll().stream()
                .map(timeSlot -> {
                    ListTimeSlotDto dto = modelMapper.map(timeSlot, ListTimeSlotDto.class);
                    TimeSlot ts = timeSlotRepository.findById(dto.getId()).get();
                    dto.setStatus(ts.isDeleted());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
