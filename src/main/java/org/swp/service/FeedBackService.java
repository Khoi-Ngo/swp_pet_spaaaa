package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.FeedbackReplyRequest;
import org.swp.dto.request.FeedbackRequest;
import org.swp.dto.response.FeedbackListDto;
import org.swp.entity.Shop;
import org.swp.entity.User;
import org.swp.entity.other.Feedback;
import org.swp.entity.other.FeedbackReply;
import org.swp.enums.UserRole;
import org.swp.repository.*;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FeedBackService {

    @Autowired
    private IFeedbackRepository feedbackRepository;
    @Autowired
    private IFeedBackReplyRepository feedBackReplyRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IServiceRepository serviceRepository;
    @Autowired
    private IShopRepository shopRepository;



    public Object getAllFeedbacks(int serviceId) {
        return feedbackRepository.findAllFeedbackByServiceId(serviceId).stream()
                .map(feedback -> {
                    FeedbackListDto dto = modelMapper.map(feedback, FeedbackListDto.class);
                    dto.setUserName(
                            Objects.nonNull(feedback.getUser()) ?
                                    feedback.getUser().getUsername()
                                    : "Khong xac dinh"
                    );
                    dto.setServiceName(
                            Objects.nonNull(feedback.getService()) ?
                                    feedback.getService().getServiceName()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(feedback.getService()) ?
                                    feedback.getService().getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getLatestFeedback(int serviceId, int numberOfRecords) {
        return feedbackRepository.findLatestFeedbackByServiceId(serviceId, numberOfRecords).stream()
                .map(feedback -> {
                    FeedbackListDto dto = modelMapper.map(feedback, FeedbackListDto.class);
                    dto.setUserName(
                            Objects.nonNull(feedback.getUser()) ?
                                    feedback.getUser().getUsername()
                                    : "Khong xac dinh"
                    );
                    dto.setServiceName(
                            Objects.nonNull(feedback.getService()) ?
                                    feedback.getService().getServiceName()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(feedback.getService()) ?
                                    feedback.getService().getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Object getLatestFeedbackRe(int feedbackId, int numberOfRecords) {
        return feedBackReplyRepository.findFeedbackRe(feedbackId, numberOfRecords).stream()
                .map(feedbackReply -> {
                    FeedbackListDto dto = modelMapper.map(feedbackReply, FeedbackListDto.class);
                    dto.setUserName(
                            Objects.nonNull(feedbackReply.getUser()) ?
                                    feedbackReply.getUser().getUsername()
                                    : "Khong xac dinh"
                    );
                    dto.setServiceName(
                            Objects.nonNull(feedbackReply.getFeedback()) ?
                                    feedbackReply.getFeedback().getService().getServiceName()
                                    : "Khong xac dinh"
                    );
                    dto.setShopName(
                            Objects.nonNull(feedbackReply.getFeedback()) ?
                                    feedbackReply.getFeedback().getService().getShop().getShopName()
                                    : "Khong xac dinh"
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }


    public boolean createFeedback(String token, FeedbackRequest feedbackRequest) {
        try {
            String username = getUserNameFromToken(token);
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null) {
                return false;
            }

            org.swp.entity.Service service = serviceRepository.findById(feedbackRequest.getServiceId()).orElse(null);
            Shop shop = shopRepository.findById(feedbackRequest.getShopId()).orElse(null);

            if (service == null || shop == null || !isServiceLinkedToShop(service, shop)) {
                return false;
            }

            Feedback feedback = modelMapper.map(feedbackRequest, Feedback.class);
            feedback.setUser(user);
            feedback.setService(service);
            feedback.setShop(shop);
            feedbackRepository.save(feedback);

            return true;
        } catch (Exception e) {
            // Log the exception
            return false;
        }
    }

    public boolean createFeedbackReply(String token, FeedbackReplyRequest feedbackRequest) {
        try {
            String username = getUserNameFromToken(token);
            User user = userRepository.findByUsername(username).orElse(null);

            //check role of user
            if (!isShopOwner(username)) {
                return false;
            }
            //check feedback exist
            Feedback feedback = feedbackRepository.findById(feedbackRequest.getFeedbackId()).orElse(null);
            if (feedback == null) {
                return false;
            }
            FeedbackReply feedbackReply = modelMapper.map(feedbackRequest, FeedbackReply.class);
            feedbackReply.setUser(user);
            feedbackReply.setFeedback(feedback);
            feedbackReply.setContent(feedbackRequest.getContent());
            feedBackReplyRepository.save(feedbackReply);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isServiceLinkedToShop(org.swp.entity.Service service, Shop shop) {
        return service.getShop().getId() == shop.getId();
        }

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }

    private boolean isShopOwner(String username) {
        User user = userRepository.findByUsername(username).get();
        return UserRole.SHOP_OWNER.equals(user.getRole());
    }

}
