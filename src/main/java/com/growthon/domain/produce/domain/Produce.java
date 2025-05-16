package com.growthon.domain.produce.domain;

import com.growthon.domain.produce.dto.PostProduceRequest;
import com.growthon.domain.produce.model.Category;
import jakarta.persistence.*;
import com.growthon.global.domain.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "produce")
public class Produce extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long produceId;

    private String images;

    @Column(length = 10)
    private String weight;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate harvestDate;

    @Column(nullable = false, length = 50)
    private String origin;

    @Column(nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('FRUIT', 'VEGETABLE', 'GRAIN', 'NONE')", nullable = false)
    private Category category;

    // private int price;

    protected Produce() {}

    public Produce(PostProduceRequest request, String imagePath) {
        this.userId = request.getUserId();
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.harvestDate = request.getHarvestDate();
        this.origin = request.getOrigin();
        this.weight = request.getWeight();
        this.images = imagePath;
        this.category = request.getCategory();
    }

    public long getProduceId() {
        return produceId;
    }
}
