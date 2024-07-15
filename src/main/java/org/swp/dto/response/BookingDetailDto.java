package org.swp.dto.response;

import jakarta.persistence.Lob;
import lombok.Data;
import org.swp.enums.TypePet;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingDetailDto {
    private Integer id;
    @Lob
    private String bookingNote;
    private boolean isDone;
    private boolean isCanceled;
    private String status;

    private LocalDate localDate;
    private LocalTime startTime;
    private LocalTime endTime;


    private Integer shopId;
    private String shopName;
    private String shopAddress;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Integer serviceId;
    private String serviceName;
    private TypePet typePet;
    private String petName;
    private Integer petWeight;
    @Lob
    private String note;//bookingNote

    private String additionalMessage;//in case of view of CANCELED booking
    private String servicePhoto;
    private String petPhoto;
}
