package com.growthon.domain.produce.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateProduceResponse {
    private final long produceId;
    private final LocalDateTime updateAt;

    public UpdateProduceResponse(long produceId, LocalDateTime updateAt) {
        this.produceId = produceId;
        this.updateAt = updateAt;
    }
}
