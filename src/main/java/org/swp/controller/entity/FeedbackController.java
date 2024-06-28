package org.swp.controller.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.feedback.FeedbackConstantNumber;
import org.swp.dto.request.FeedbackReplyRequest;
import org.swp.dto.request.FeedbackRequest;
import org.swp.service.FeedBackService;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedBackService feedbackService;

    //Feedback list
    @GetMapping("/latest-feedback/{serviceId}")
    public ResponseEntity<?> getLatestFeedbackOfServiceByServiceId(@PathVariable("serviceId") int serviceId){
        try {
            var feedbacks = feedbackService.getLatestFeedback(serviceId, FeedbackConstantNumber.NUMBER_OF_LATEST_FEEDBACK.getValue());
            if (feedbacks == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Latest feedback not found");
            }
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error while getting latest feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Create feedback
    @PostMapping("/create")
    public ResponseEntity<?> createFeedback(@RequestHeader("Authorization") String token,
                                            @RequestBody FeedbackRequest feedbackRequest) {
        try {
            boolean isCreated = feedbackService.createFeedback(token, feedbackRequest);
            if (!isCreated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create feedback");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback created successfully");
        } catch (Exception e) {
            logger.error("Error while creating feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //Feedback reply create
    @PostMapping("/create-reply")
    public ResponseEntity<?> createFeedbackReply(@RequestHeader("Authorization") String token,
                                             @RequestBody FeedbackReplyRequest feedbackRequest) {
        try {
            boolean isCreated = feedbackService.createFeedbackReply(token, feedbackRequest);
            if (!isCreated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create feedback reply");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Feedback reply created successfully");
        } catch (Exception e) {
            logger.error("Error while creating feedback reply", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Feedback reply list
    @GetMapping("/latest-feedback-reply/{feedbackId}")
    public ResponseEntity<?> getLatestFeedbackReply(@PathVariable("feedbackId") int feedbackId){
        try {
            var feedback_re = feedbackService.getLatestFeedbackRe(feedbackId, FeedbackConstantNumber.NUMBER_OF_LATEST_FEEDBACK.getValue());
            if (feedback_re == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Latest feedback reply not found");
            }
            return ResponseEntity.ok(feedback_re);
        } catch (Exception e) {
            logger.error("Error while getting latest feedback reply", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }

}
