package com.growthon.domain.produce.dto.response;

import com.growthon.domain.produce.domain.Produce;
import com.growthon.domain.produce.model.Category;
import com.growthon.global.domain.BaseEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetProduceByIdResponse extends BaseEntity {
    private final long produceId;
    private final String title;
    private final String origin;
    private final Category category;
    private final String weight;
    private final LocalDate harvestDate;
    private final String images;
    private final String description;
    private final long userId;

    public GetProduceByIdResponse(Produce produce) {
        this.produceId = produce.getProduceId();
        this.title = produce.getTitle();
        this.origin = produce.getOrigin();
        this.category = produce.getCategory();
        this.weight = produce.getWeight();
        this.harvestDate = produce.getHarvestDate();
        this.updateAt = produce.getUpdateAt();
        this.createdAt = produce.getCreatedAt();
        this.description = produce.getDescription();
        this.images = produce.getImages();
        this.userId = produce.getUserId();
    }
}
