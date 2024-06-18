package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PetDetailDto {
    private Integer id;
    private String petName;
    private TypePet petType;
    private int petAge;
    private String petGender;
    private int petWeight;
    private String petDescription;
    private String petPhoto;
    private String petNote;

    //booking detail
    private Map<LocalDate, List<BookingHistoryListItemDto>> bookingHistory = new HashMap<>();
}
