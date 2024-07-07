package org.swp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DashboardDto {
    private int totalNominations;
    private int totalServices;
    private int totalBookings;
    private List<MonthlyBookingDto> monthlyBookings;
}
