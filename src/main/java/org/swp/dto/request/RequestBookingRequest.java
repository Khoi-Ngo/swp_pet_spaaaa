package org.swp.dto.request;

import lombok.Data;
import org.swp.dto.response.TimeSlotDto;
import org.swp.enums.TypePet;

import java.time.LocalDate;

@Data
public class RequestBookingRequest {

    private int customerId;
    private String additionalMessage;
    private int serviceId;

    //date - timeslot
    private LocalDate localDate;
    private TimeSlotDto timeSlotDto;

    //pet information
    private String petName;
    private int petAge;
    private TypePet typePet;
    private int petWeight;
    private Integer petId;
    private String petGender;


}
