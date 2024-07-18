package org.swp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.swp.dto.response.NotificationDto;
import org.swp.dto.response.TotalUnreadNotificationDto;
import org.swp.entity.Notification;
import org.swp.repository.INotificationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    public Object getNotificationByUser(String token) {
        String userName = jwtService.getUserNameFromToken(token);
        return notificationRepository.findAllByUser(userName).stream()
                .map(e -> {
                    NotificationDto dto = modelMapper.map(e, NotificationDto.class);
                    dto.setBookingId(e.getBooking().getId());
                    return dto;
                }).collect(Collectors.toList());
    }

    public void updateStatusNotification(String token, int id) {
        Notification notification = notificationRepository.findById(id).get();
        if (!isValidUser(token, notification)) throw new RuntimeException("Not valid user");
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private boolean isValidUser(String token, Notification notification) {
        return notification.getUser().getUsername().equals(jwtService.getUserNameFromToken(token));
    }

    public TotalUnreadNotificationDto getTotalUnreadNotification(String token) {
        Integer totalUnread = notificationRepository.getTotalUnread(jwtService.getUserNameFromToken(token));
        TotalUnreadNotificationDto dto = new TotalUnreadNotificationDto();
        dto.setTotalUnreadNotification(totalUnread);
        return dto;
    }

//    @Scheduled(fixedRate = 10000)
//    public void checkNewNotificationOfUser() throws Exception {
//        List<NotificationDto> notificationDtoList = notificationRepository.findAllNotRead().stream()
//                .map(e -> {
//                    NotificationDto dto = modelMapper.map(e, NotificationDto.class);
//                    dto.setBookingId(e.getBooking().getId());
//                    return dto;
//                }).collect(Collectors.toList());
//        String payload = objectMapper.writeValueAsString(notificationDtoList);
//        simpMessagingTemplate.convertAndSend("/topic/notifications", payload);
//    }
}
