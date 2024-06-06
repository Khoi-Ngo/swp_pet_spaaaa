package org.swp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.ServiceCategoryDto;
import org.swp.entity.ServiceCategory;
import org.swp.repository.ICategorySerivceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceService {
    @Autowired
    private ICategorySerivceRepository categorySerivceRepository;

    public List<ServiceCategoryDto> getAll() {
        List<ServiceCategory> serviceCategories = categorySerivceRepository.findAll();
        List<ServiceCategoryDto> serviceCategoryDtos = new ArrayList<ServiceCategoryDto>();
        serviceCategories.forEach(sc -> serviceCategoryDtos.add(new ServiceCategoryDto()));
        return serviceCategoryDtos;
    }
}
