package org.swp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AdminDashboardDto {
    private int totalShop;
    private int totalServices;
    private int totalCustomer;
    private int totalBookings;
    private int totalPets;
    private List<MonthlyBookingDto> monthlyBookings;
}
