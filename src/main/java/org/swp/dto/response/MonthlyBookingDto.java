package org.swp.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class MonthlyBookingDto {
    private String month;
    private int bookings;
}
