package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PetListItemDto {
    private Integer id;
    private String petName;
    private TypePet petType;
    private int petAge;
    private String petGender;
    private String petPhoto;
    private int petWeight;
    private Integer ownerId;
    private String ownerName;
    private String petDescription;
    private String petNote;

    //booking overview detail
    private boolean doHaveUpcomingSchedule;
    //nearest booking
    private LocalDate nearestBookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
