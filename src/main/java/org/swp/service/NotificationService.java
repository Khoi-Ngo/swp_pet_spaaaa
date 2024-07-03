package org.swp.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.swp.dto.response.NotificationDto;
import org.swp.repository.INotificationRepository;

import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ModelMapper modelMapper;

    public Object getNotificationByUser(String token) {
        String userName = getUserNameFromToken(token);
        return notificationRepository.findAllByUser(userName).stream()
                .map(e -> {
                    NotificationDto dto = modelMapper.map(e, NotificationDto.class);
                    dto.setBookingId(e.getBooking().getId());
                    return dto;
                }).collect(Collectors.toList());
    }

    private String getUserNameFromToken(String token) {
        String userName = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7); // Remove "Bearer " prefix
            userName = jwtService.extractUserName(jwtToken);
        }
        return userName;
    }
}
