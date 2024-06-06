package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ServiceListItemDto;
import org.swp.enums.TypePet;
import org.swp.repository.IServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceService {
    @Autowired
    private IServiceRepository serviceRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ServiceListItemDto> getAll() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);
                    dto.setTypePetString(dto.getTypePet().getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public org.swp.entity.Service getServiceById(int id) {
        return serviceRepository.findById(id).orElse(null); // Return null if service is not found
    }

    public List<org.swp.entity.Service> getMostRcmdServices(TypePet typePet, int numberOfRecords) {
        return serviceRepository.findMostRcmdServices(typePet, numberOfRecords);
    }

    public List<org.swp.entity.Service> getMostRcmdServices(int numberOfRecords) {
        return serviceRepository.findMostRcmdServices(numberOfRecords);
    }

    public List<org.swp.entity.Service> getLatestServices(int numberOfRecords) {
        return serviceRepository.findLatestServices(numberOfRecords);
    }
}
