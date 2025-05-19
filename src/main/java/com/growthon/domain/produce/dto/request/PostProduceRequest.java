package com.growthon.domain.produce.dto.request;

import com.growthon.domain.produce.model.Category;
import com.growthon.global.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PostProduceRequest extends BaseEntity {
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

    @NotNull
    private int price;

    private String weight;
}
