package org.swp.controller.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.feedback.FeedbackConstantNumber;
import org.swp.dto.request.FeedbackCreateRequest;
import org.swp.dto.request.FeedbackUpdateRequest;
import org.swp.service.FeedBackService;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedBackService feedbackService;

    //Feedback all
    @GetMapping("/all/{serviceId}")
    public ResponseEntity<?> getAllFeedbacks(@PathVariable("serviceId") int serviceId) {
        try {
            var feedbacks = feedbackService.getAllFeedbacks(serviceId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error while getting all feedbacks", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Feedback latest list
    @GetMapping("/latest/{serviceId}")
    public ResponseEntity<?> getLatestFeedbackOfServiceByServiceId(@PathVariable("serviceId") int serviceId) {
        try {
            var feedbacks = feedbackService.getLatestFeedback(serviceId, FeedbackConstantNumber.NUMBER_OF_LATEST_FEEDBACK.getValue());
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            logger.error("Error while getting latest feedbacks", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Create feedback
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestHeader("Authorization") String token,
                                            @RequestBody FeedbackCreateRequest request) {
        try {
            var response = feedbackService.createFeedback(token, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while creating feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //feedback detail
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable("id") int id) {
        try {
            var response = feedbackService.getDetailFeedback(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting feedback", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable("id") int id,
                                            @RequestHeader("Authorization") String token) {
        try {
            var response = feedbackService.deleteFeedback(id, token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while deleting feedback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    //update
//    @PutMapping
//    public ResponseEntity<?> updateFeedback(@RequestBody FeedbackUpdateRequest request,
//                                            @RequestHeader("Authorization") String token) {
//        try {
//            var response = feedbackService.updateFeedback(request, token);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            logger.error("Error while updating feedback", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }

}
