package org.swp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.entity.ServiceCategory;
import org.swp.repository.ICategorySerivceRepository;

import java.util.List;

@Service
public class CategoryServiceService {
    @Autowired
    private ICategorySerivceRepository categorySerivceRepository;

    public List<ServiceCategory> getAll() {
        return categorySerivceRepository.findAll();
    }
}
