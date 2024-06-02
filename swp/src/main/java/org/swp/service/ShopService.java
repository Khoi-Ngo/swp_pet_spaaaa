package org.swp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.enums.TypePet;
import org.swp.repository.IShopRepository;

@Service
public class ShopService {
    @Autowired
    private IShopRepository shopRepository;

    public Object getMostRcmdShops(int numberOfRecords) {
        return shopRepository.findMostRcmdShops(numberOfRecords);
    }

    public Object getMostRcmdShops(TypePet typePet, int numberOfRecords) {
        return shopRepository.findMostRcmdShops(typePet, numberOfRecords);
    }
}
