package org.swp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.request.FeedbackReplyCreateRequest;
import org.swp.dto.request.FeedbackReplyUpdateRequest;
import org.swp.dto.response.FeedbackReplyDto;
import org.swp.entity.User;
import org.swp.entity.other.Feedback;
import org.swp.entity.other.FeedbackReply;
import org.swp.repository.IFeedBackReplyRepository;
import org.swp.repository.IFeedbackRepository;
import org.swp.repository.IUserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FeedBackReplyService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IFeedbackRepository feedbackRepository;
    @Autowired
    private IFeedBackReplyRepository feedBackReplyRepository;

    public Object createFeedbackReply(String token, FeedbackReplyCreateRequest request) {
        String userName = jwtService.getUserNameFromToken(token);
        User user = userRepository.findByUsername(userName).get();
        Feedback feedback = feedbackRepository.findById(request.getFeedbackId()).get();
        FeedbackReply feedbackReply = new FeedbackReply(request.getContent(), user, feedback);
        feedBackReplyRepository.save(feedbackReply);
        return "Create feedback reply successfully";
    }

    private boolean doOwnFeedbackReply(FeedbackReply feedbackReply, String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return userName.equals(feedbackReply.getUser().getUsername());
    }

    public Object deleteFeedbackReply(int id, String token) {
        FeedbackReply feedbackReply = feedBackReplyRepository.findById(id).get();
        if (!doOwnFeedbackReply(feedbackReply, token)) throw new RuntimeException("User not own feedback reply");
        feedbackReply.setDeleted(true);
        feedBackReplyRepository.save(feedbackReply);
        return "Delete feedback reply successfully";
    }

    public Object updateFeedbackReply(FeedbackReplyUpdateRequest request, String token) {
        FeedbackReply feedbackReply = feedBackReplyRepository.findById(request.getFeedbackReplyId()).get();
        if (!doOwnFeedbackReply(feedbackReply, token)) throw new RuntimeException("User not own feedback reply");
        feedbackReply.setContent(Objects.nonNull(request.getUpdateContent()) ? request.getUpdateContent() : feedbackReply.getContent());
        feedBackReplyRepository.save(feedbackReply);
        return "Update feedback reply successfully";
    }

    public List<FeedbackReplyDto> getLatestFeedbackReply(int feedbackId, int value) {
        List<FeedbackReply> feedbackReplyList = feedBackReplyRepository.findByFeedbackId(feedbackId, value);
        return feedbackReplyList.stream()
                .map(entity -> new FeedbackReplyDto(
                        entity.getUser().getUsername(),
                        entity.getContent(),
                        entity.getCreatedTime()
                ))
                .collect(Collectors.toList());
    }

    public Object getAllFeedbackReply(int feedbackId) {
        List<FeedbackReply> feedbackReplyList = feedBackReplyRepository.findByFeedbackId(feedbackId);
        return feedbackReplyList.stream()
                .map(entity -> new FeedbackReplyDto(
                        entity.getUser().getUsername(),
                        entity.getContent(),
                        entity.getCreatedTime()
                ))
                .collect(Collectors.toList());
    }
}
