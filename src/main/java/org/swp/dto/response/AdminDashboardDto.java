package org.swp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDashboardDto {
    private int totalShop;
    private int totalServices;
    private int totalCustomer;
    private int totalBookings;
    private int totalPets;
    private List<MonthlyBookingDto> monthlyBookings;
}
