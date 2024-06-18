package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingDetailDto {
    private Integer id;
    @Lob
    private String bookingNote;
    private boolean isDone;
    private boolean isCanceled;
    private String status;
    private ShopDetailDto shopDetailDto;
    private UserDto userDto;
    private ServiceDetailDto serviceDetailDto;
    private LocalDate localDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
