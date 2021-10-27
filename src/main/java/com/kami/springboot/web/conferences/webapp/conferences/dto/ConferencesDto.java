package com.kami.springboot.web.conferences.webapp.conferences.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ConferencesDto {
    @NotNull
    @Size(min = 1, message = "{size.conferenceName}")
    private String name;
    @NotNull
    @Size(min = 1, message = "{size.conferenceCountry}")
    private String country;
    @NotNull
    @Size(min = 1, message = "{size.conferenceName}")
    private String city;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate finishDate;
    @Size(min = 1, message = "{size.conferenceName}")
    private String logo;
    @Size(max = 128, message ="{size.description}")
    private String description;
    @NotNull
    private String type;
}
