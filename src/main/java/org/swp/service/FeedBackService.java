package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.FeedbackListDto;
import org.swp.repository.IFeedBackReplyRepository;
import org.swp.repository.IFeedbackRepository;

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
}
