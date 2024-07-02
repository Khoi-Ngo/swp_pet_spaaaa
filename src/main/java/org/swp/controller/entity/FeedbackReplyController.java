package org.swp.controller.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.feedback.FeedbackConstantNumber;
import org.swp.dto.request.FeedbackReplyCreateRequest;
import org.swp.dto.request.FeedbackReplyUpdateRequest;
import org.swp.service.FeedBackReplyService;

@RestController
@RequestMapping("api/v1/feedback-reply")
public class FeedbackReplyController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackReplyController.class);
    @Autowired
    private FeedBackReplyService feedBackReplyService;

    //Feedback reply create
    @PostMapping
    public ResponseEntity<?> createFeedbackReply(@RequestHeader("Authorization") String token,
                                                 @RequestBody FeedbackReplyCreateRequest request) {
        try {
            var response = feedBackReplyService.createFeedbackReply(token, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while creating feedback reply", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Feedback reply latest list
    @GetMapping("/latest/{feedbackId}")
    public ResponseEntity<?> getLatestFeedbackReply(@PathVariable("feedbackId") int feedbackId) {
        try {
            var response = feedBackReplyService.getLatestFeedbackReply(feedbackId, FeedbackConstantNumber.NUMBER_OF_LATEST_FEEDBACK_REPLY.getValue());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting latest feedback reply", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //All feedback reply by a feedback
    @GetMapping("/all/{feedbackId}")
    public ResponseEntity<?> getAllFeedbackReplyByFeedback(@PathVariable("feedbackId") int feedbackId) {
        try {
            var response = feedBackReplyService.getAllFeedbackReply(feedbackId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting all feedback reply", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedbackReply(@PathVariable("id") int id,
                                                 @RequestHeader("Authorization") String token) {
        try {
            var response = feedBackReplyService.deleteFeedbackReply(id, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while deleting feedback reply", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //update
    @PutMapping
    public ResponseEntity<?> updateFeedbackReply(@RequestBody FeedbackReplyUpdateRequest request,
                                                 @RequestHeader("Authorization") String token) {
        try {
            var response = feedBackReplyService.updateFeedbackReply(request, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while updating feedback reply", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
