package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.CreateServiceRequest;
import org.swp.dto.request.UpdateServiceRequest;
import org.swp.dto.response.ListServiceDto;
import org.swp.dto.response.ServiceDetailDto;
import org.swp.dto.response.ServiceListItemDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.enums.TypePet;
import org.swp.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private JWTService jwtService;

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private IReferPriceRepository referPriceRepository;

    public List<ServiceListItemDto> getAll() {
        return serviceRepository.findAllService().stream()
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
                    dto.setCategoryId(
                            Objects.nonNull(service.getCategory()) ?
                                    service.getCategory().getId()
                                    : -1
                    );
                    dto.setCategoryName(
                            Objects.nonNull(service.getCategory()) ?
                                    service.getCategory().getCategoryName()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getServiceById(int id) {
        //service detail
        org.swp.entity.Service service = serviceRepository.findById(id).orElse(null);// Return null if service is not found
        if (service.isDeleted()) {
            return "service is deleted";
        }
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

    public Object createService(CreateServiceRequest request) {

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

    public Object deleteService(int id, String token) {
        org.swp.entity.Service service = serviceRepository.findById(id).get();
        if (!isShopOwnerOfService(service, token) || service.isDeleted())
            throw new RuntimeException("User not shop owner/ service is deleted");
        service.setDeleted(true);
        serviceRepository.save(service);
        Shop shop = service.getShop();
        shop.setTotalServices(shop.getTotalServices() - 1);
        shopRepository.save(shop);
        feedbackRepository.deletedAllFeedBackByServiceId(service.getId());
        referPriceRepository.deleteAllByServiceId(service.getId());
        //update booking also
        return "Deleted";
    }

    private boolean isShopOwnerOfService(org.swp.entity.Service service, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return userName.equals(service.getShop().getUser().getUsername());
    }

    public Object updateService(UpdateServiceRequest request) {
        if (request.getId() <= 0) {
            return "serviceId is null";
        }
        if (request.getServiceCategoryId() <= 0) {
            return "categoryId is null";
        }
        org.swp.entity.Service service = modelMapper.map(request, org.swp.entity.Service.class);
        User user = userRepository.findById(request.getUserId()).get();
        Shop shop = shopRepository.findByShopOwnerId(user.getId());
        if (shop == null) {
            return "Shop not found";
        }
        service.setCategory(categorySerivceRepository.findById(request.getServiceCategoryId()).get());
        service.setCreatedTime(LocalDateTime.now());
        service.setShop(shop);
        serviceRepository.save(service);
        return request;
    }

    public Object getAllOfShopowner(String token) {
        String username = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(username).get();
        Shop shop = shopRepository.findByShopOwnerId(user.getId());
        return serviceRepository.findAllByShopId(shop.getId()).stream()
                .map(service -> {
                    ListServiceDto dto = modelMapper.map(service, ListServiceDto.class);
                    dto.setCategoryId(service.getCategory().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
