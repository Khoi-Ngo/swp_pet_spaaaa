package org.swp.controller.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swp.configuration.constant.feedback.FeedbackConstantNumber;
import org.swp.service.FeedBackService;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedBackService feedbackService;

    //Feedback

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


    //Feedback reply

    @GetMapping("/latest-feedback-reply/{feedbackId}")
    public ResponseEntity<?> getLatestFeedbackRe(@PathVariable("feedbackId") int feedbackId){
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
