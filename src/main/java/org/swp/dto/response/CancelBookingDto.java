package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class CancelBookingDto {
    private BookingDetailDto bookingDetailDto;
    @Lob
    private String additionalMessage;
}
