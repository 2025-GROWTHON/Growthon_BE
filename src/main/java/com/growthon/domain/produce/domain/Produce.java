package com.growthon.domain.produce.domain;

import com.growthon.domain.produce.dto.request.PostProduceRequest;
import com.growthon.domain.produce.dto.request.UpdateProduceRequest;
import com.growthon.domain.produce.model.Category;
import com.growthon.domain.user.domain.User;
import jakarta.persistence.*;
import com.growthon.global.domain.BaseEntity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "produce")
@Getter
public class Produce extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produceId;

    private String images;

    @Column(length = 10)
    private String weight;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate harvestDate;

    @Column(nullable = false, length = 50)
    private String origin;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('FRUIT', 'VEGETABLE', 'GRAIN', 'NONE')", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Produce() {}

    public Produce(PostProduceRequest request, String imagePath, User user) {
        this.user = user;
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.harvestDate = request.getHarvestDate();
        this.origin = request.getOrigin();
        this.weight = request.getWeight();
        this.images = imagePath;
        this.category = request.getCategory();
        this.price = request.getPrice();
    }

    public Produce updateProduce(UpdateProduceRequest request) {
        this.description = request.getDescription();
        this.title = request.getTitle();
        this.harvestDate = request.getHarvestDate();
        this.origin = request.getOrigin();
        this.weight = request.getWeight();
        this.category = request.getCategory();
        this.images = request.getImages();
        return this;
    }

}
