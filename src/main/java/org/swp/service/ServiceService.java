package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateServiceRequest;
import org.swp.dto.request.DeleteServiceRequest;
import org.swp.dto.response.ListServiceDto;
import org.swp.dto.response.ServiceDetailDto;
import org.swp.dto.response.ServiceListItemDto;
import org.swp.entity.ServiceCategory;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.TypePet;
import org.swp.repository.ICategorySerivceRepository;
import org.swp.repository.IServiceRepository;
import org.swp.repository.IShopRepository;
import org.swp.repository.IUserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.swp.enums.UserRole.SHOP_OWNER;

@Service
public class ServiceService {
    @Autowired
    private IServiceRepository serviceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IShopRepository shopRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategorySerivceRepository categorySerivceRepository;

    public List<ServiceListItemDto> getAll() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);


                    dto.setAddress(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopAddress()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getServiceById(int id) {
        //service detail
        org.swp.entity.Service service = serviceRepository.findById(id).orElse(null);// Return null if service is not found
        ServiceDetailDto dto = modelMapper.map(service, ServiceDetailDto.class);
        dto.setShopId(Objects.nonNull(service.getShop()) ? service.getShop().getId() : -1);
        dto.setShopName(Objects.nonNull(service.getShop()) ? service.getShop().getShopName() : "Khong xac dinh shop");
        dto.setCategoryName((Objects.nonNull(service.getCategory()) ? service.getCategory().getCategoryName() : "Khong xac dinh category"));
        dto.setShopAddress(Objects.nonNull(service.getShop()) ? service.getShop().getShopAddress() : "Khong xac dinh");
        //date for front end create
        //todo: price more dynamical and response the list basing on the threshold of typepet and weight
        return dto;
    }

    public Object getMostRcmdServices(TypePet typePet, int numberOfRecords) {
        return serviceRepository.findMostRcmdServices(typePet, numberOfRecords).stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);
                    dto.setAddress(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopAddress()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getMostRcmdServices(int numberOfRecords) {
        return serviceRepository.findMostRcmdServices(numberOfRecords).stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);
                    dto.setAddress(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopAddress()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());


    }

    public Object getLatestServices(int numberOfRecords) {
        return serviceRepository.findLatestServices(numberOfRecords).stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);
                    dto.setAddress(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopAddress()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getAllServiceByShopId(int shopId) {
        return serviceRepository.findAllByShopId(shopId).stream()
                .map(service -> {
                    ServiceListItemDto dto = modelMapper.map(service, ServiceListItemDto.class);
                    dto.setAddress(
                            Objects.nonNull(service.getShop()) ?
                                    service.getShop().getShopAddress()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object createService(CreateServiceRequest request){

        org.swp.entity.Service service = modelMapper.map(request, org.swp.entity.Service.class);

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        int shopOwnerId = user.getId();
        service.setCreatedBy(String.valueOf(user.getRole()));

        Shop shop = shopRepository.findByShopOwnerId(shopOwnerId);
        service.setShop(shop);

        service.setCategory(categorySerivceRepository.findById(request.getServiceCategoryId()).orElseThrow(() -> new RuntimeException("Service category not found")));

        serviceRepository.save(service);

        shop.setTotalServices(shop.getTotalServices() + 1);
        shopRepository.save(shop);

        return service.getId();
    }

    public Object deleteService(DeleteServiceRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Shop shop = shopRepository.findByShopOwnerId(user.getId());
        serviceRepository.deleteById(request.getServiceId());
        shop.setTotalServices(shop.getTotalServices()-1);
        shopRepository.save(shop);

        return "delete service successful!";
    }

    public Object getAllOfShopowner(int id){
        User user = userRepository.findById(id).get();
        Shop shop = shopRepository.findByShopOwnerId(user.getId());
        return serviceRepository.findAllByShopId(shop.getId()).stream()
                .map(service -> {
                    ListServiceDto dto = modelMapper.map(service, ListServiceDto.class);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
