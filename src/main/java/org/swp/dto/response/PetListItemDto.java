package org.swp.dto.response;

import lombok.Data;
import org.swp.enums.TypePet;

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

    //booking overview detail
    private boolean doHaveUpcomingSchedule;
}
