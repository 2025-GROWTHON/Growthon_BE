package com.growthon.domain.produce.controller;

import com.growthon.domain.produce.dto.PostProduceRequest;
import com.growthon.domain.produce.service.ProduceService;
import com.growthon.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProduceController {

    private ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    //Test Code
    @GetMapping("/api/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.of(200, "테스트 성공", "test"));
    }

    //TODO: Exception 구현, price 추가 고려
    //Produce Post API
    @PostMapping(value = "/api/produce", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Long>> postProduce(
            @RequestPart("request") PostProduceRequest request,
            @RequestPart("images") MultipartFile images
    ) throws Exception {
        return produceService.postProduce(request, images);
    }

}