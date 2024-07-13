package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ServiceCategoryDto;
import org.swp.entity.ServiceCategory;
import org.swp.repository.ICategorySerivceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceService {
    @Autowired
    private ICategorySerivceRepository categorySerivceRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ServiceCategoryDto> getAll() {
        List<ServiceCategory> serviceCategories = categorySerivceRepository.findAllServiceCategory().stream()
                .toList();
        List<ServiceCategoryDto> serviceCategoryDtos = new ArrayList<>();
        serviceCategories.forEach(sc -> serviceCategoryDtos.add(modelMapper.map(sc, ServiceCategoryDto.class)));
        return serviceCategoryDtos;
    }
}
