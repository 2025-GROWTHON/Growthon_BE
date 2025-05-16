package com.growthon.domain.produce.dto.response;

import com.growthon.domain.produce.domain.Produce;
import com.growthon.domain.produce.model.Category;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class GetProducesResponse {
    private final long produceId;
    private final String title;
    private final String origin;
    private final Category category;
    private final String weight;
    private final LocalDate harvestDate;
    private final LocalDateTime updateAt;

    public GetProducesResponse(long produceId, String title, String origin, Category category, String weight, LocalDate harvestDate, LocalDateTime updateAt) {
        this.produceId = produceId;
        this.title = title;
        this.origin = origin;
        this.category = category;
        this.weight = weight;
        this.harvestDate = harvestDate;
        this.updateAt = updateAt;
    }

    public static GetProducesResponse from (Produce produce) {
        return new GetProducesResponse(
                produce.getProduceId(),
                produce.getTitle(),
                produce.getOrigin(),
                produce.getCategory(),
                produce.getWeight(),
                produce.getHarvestDate(),
                produce.getUpdateAt()
        );
    }
}
