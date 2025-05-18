package com.growthon.domain.produce.controller;

import com.growthon.domain.produce.dto.request.PostProduceRequest;
import com.growthon.domain.produce.dto.request.UpdateProduceRequest;
import com.growthon.domain.produce.dto.response.GetProduceByIdResponse;
import com.growthon.domain.produce.dto.response.GetProducesResponse;
import com.growthon.domain.produce.dto.response.UpdateProduceResponse;
import com.growthon.domain.produce.exception.NotFoundProduceException;
import com.growthon.domain.produce.service.ProduceService;
import com.growthon.global.response.ApiResponse;
import com.growthon.global.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class ProduceController {

    private final ProduceService produceService;

    public ProduceController(ProduceService produceService) {
        this.produceService = produceService;
    }

    // Produce Post API
    @PostMapping(value = "/api/produce", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<Long>> postProduce(
            @RequestPart("request") PostProduceRequest request,
            @RequestPart("images") MultipartFile images,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) throws Exception {
        return produceService.postProduce(request, images, userDetails);
    }

    // Produce Get API (ALL)
    @GetMapping("/api/produces")
    public ResponseEntity<ApiResponse<List<GetProducesResponse>>> getProduces()  {
        return produceService.getProduces();
    }

    // Produce Get API (Detail)
    @GetMapping("/api/produces/{produceId}")
    public ResponseEntity<ApiResponse<GetProduceByIdResponse>> getProduceById(@PathVariable long produceId
    ) throws NotFoundProduceException {
        return produceService.getProduceById(produceId);
    }

    // Produce Put API
    @PutMapping("/api/produce/{produceId}")
    public ResponseEntity<ApiResponse<UpdateProduceResponse>> putProduce(
            @PathVariable long produceId,
            @RequestBody UpdateProduceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException {
        return produceService.updateProduce(produceId, request, userDetails);
    }

    // Produce Delete API
    @DeleteMapping("/api/produce/{produceId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduce(
            @PathVariable long produceId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException {
        return produceService.deleteProduce(produceId, userDetails);
    }

}