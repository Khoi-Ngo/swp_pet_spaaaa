package org.swp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.enums.TypePet;
import org.swp.repository.IServiceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    @Autowired
    private IServiceRepository serviceRepository;

    public List<org.swp.entity.Service> getAll() {
        return serviceRepository.findAll();
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
