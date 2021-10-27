package com.kami.springboot.web.conferences.webapp.conferences.dao;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Conferences")
@Data
public class Conferences {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String country;
    private String city;
    private LocalDate startDate;
    private LocalDate finishDate;

    private String logo;
    private String type;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
}
