package com.example.backend4rate.models.dto;

import lombok.Data;

@Data
public class Restaurant {
    private Integer id;
    private String name;
    private String description;
    private String workTime;
    // Dodaj ostala polja prema potrebi
}
