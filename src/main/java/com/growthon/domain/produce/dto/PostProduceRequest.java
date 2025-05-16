package com.growthon.domain.produce.dto;

import com.growthon.domain.produce.model.Category;
import com.growthon.global.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostProduceRequest extends BaseEntity {

    @NotNull
    private long userId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String origin;

    @NotNull
    private LocalDate harvestDate;

    @NotNull
    private Category category;

    private String weight;

//    private int price;
}
