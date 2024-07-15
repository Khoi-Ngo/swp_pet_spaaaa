package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.FeedbackCreateRequest;
import org.swp.dto.request.FeedbackUpdateRequest;
import org.swp.dto.response.FeedbackDetailDto;
import org.swp.dto.response.FeedbackListItemDto;
import org.swp.entity.User;
import org.swp.entity.other.Feedback;
import org.swp.entity.other.FeedbackReply;
import org.swp.repository.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

    public Object getAllFeedbacks(int serviceId) {
        List<Feedback> feedbackList = feedbackRepository.findAllFeedbackByServiceId(serviceId);
        return feedbackList.stream().map(entity -> {
            FeedbackListItemDto dto = modelMapper.map(entity, FeedbackListItemDto.class);
            dto.setUserName(entity.getUser().getUsername());
            dto.setLocalDateTime(entity.getCreatedTime());
            dto.setUserId(entity.getUser().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    public Object getLatestFeedback(int serviceId, int numberOfRecords) {
        List<Feedback> feedbackList = feedbackRepository.findLatestFeedbackByServiceId(serviceId, numberOfRecords);
        return feedbackList.stream().map(entity -> {
            FeedbackListItemDto dto = modelMapper.map(entity, FeedbackListItemDto.class);
            dto.setUserName(entity.getUser().getUsername());
            dto.setLocalDateTime(entity.getCreatedTime());
            dto.setUserId(entity.getUser().getId());
            dto.setUserPhoto(entity.getUser().getProfileImageUrl());
            return dto;
        }).collect(Collectors.toList());
    }

    public Object createFeedback(String token, FeedbackCreateRequest request) {
        String userName = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(userName).get();
        org.swp.entity.Service service = serviceRepository.findById(request.getServiceId()).get();
        Feedback feedback = new Feedback(request.getContent(), request.getRatingType(), false, user, service);
        feedback.setCreatedTime(LocalDateTime.now());
        feedbackRepository.save(feedback);
        return "Create feedback successfully";
    }

    public Object getDetailFeedback(int id) {
        Feedback feedback = feedbackRepository.findById(id).get();
        FeedbackDetailDto dto = modelMapper.map(feedback, FeedbackDetailDto.class);
        dto.setUserName(feedback.getUser().getUsername());
        dto.setLocalDateTime(feedback.getCreatedTime());
        return dto;
    }

    public Object deleteFeedback(int id, String token) {
        Feedback feedback = feedbackRepository.findById(id).get();
        if (!doOwnFeedback(feedback, token) || feedback.isDeleted())
            throw new RuntimeException("User not own the feedback/ feedback deleted before");
        feedback.setDeleted(true);
        feedBackReplyRepository.deletedAllByFeedBackId(feedback.getId());
        feedbackRepository.save(feedback);
        return "Delete feedback successfully";
    }

    private boolean doOwnFeedback(Feedback feedback, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return userName.equals(feedback.getUser().getUsername());
    }

//    public Object updateFeedback(FeedbackUpdateRequest request, String token) {
//        Feedback feedback = feedbackRepository.findById(request.getFeedbackId()).get();
//        if (!doOwnFeedback(feedback, token)) throw new RuntimeException("User not own the feedback");
//        feedback.setContent(Objects.nonNull(request.getUpdateContent()) ? request.getUpdateContent() : feedback.getContent());
//        feedback.setRatingType(Objects.nonNull(request.getUpdateRatingType()) ? request.getUpdateRatingType() : feedback.getRatingType());
//        feedback.setEdited(true);
//        feedbackRepository.save(feedback);
//        return "Update feedback successfully";
//    }

}
