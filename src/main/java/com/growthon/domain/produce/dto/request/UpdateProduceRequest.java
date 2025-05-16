package com.growthon.domain.produce.dto.request;

import com.growthon.domain.produce.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateProduceRequest {

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

    private String images;

    private String weight;

//  private int price;
}
