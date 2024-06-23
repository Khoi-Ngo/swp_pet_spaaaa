package org.swp.dto.request;

import lombok.Data;
import org.swp.enums.TypePet;

@Data
public class UpdatePetRequest {
    private Integer id;//petId
    private String petName;
    private TypePet petType;
    private int petAge;
    private String petGender;
    private int petWeight;
    private String petDescription;
    private String petPhoto;
    private String petNote;
    private int userId;

}
