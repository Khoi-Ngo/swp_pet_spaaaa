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
    private Integer petId;
    private String petName;
    private TypePet petType;
    private int petAge;
    private String petGender;
    private int petWeight;
    private String petDescription;
    private String petPhoto;
    private String petNote;


}
