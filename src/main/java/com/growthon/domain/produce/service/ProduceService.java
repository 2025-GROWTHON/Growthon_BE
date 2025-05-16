package com.growthon.domain.produce.service;

import com.growthon.domain.produce.domain.Produce;
import com.growthon.domain.produce.dto.PostProduceRequest;
import com.growthon.domain.produce.repository.ProduceRepository;
import com.growthon.global.response.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ProduceService {

    private ProduceRepository produceRepository;

    // application-local에 path를 설정해야 함
    // e.g. upload.path = C:\Users\경로\Growthon_BE\resources\image\path
    @Value("${upload.path}")
    private String uploadPath;

    public ProduceService(ProduceRepository produceRepository) {
        this.produceRepository = produceRepository;
    }

    // Produce Post Service
    @Transactional
    public ResponseEntity<ApiResponse<Long>> postProduce(
            PostProduceRequest request,
            MultipartFile images
    ) throws Exception {
        // 이미지 저장, 경로 반환
        String imagePath = saveImage(images);

        Produce produce = produceRepository.save(new Produce(request, imagePath));

        return ResponseEntity.ok(ApiResponse.of(201, "상품이 정상적으로 등록되었습니다.", produce.getProduceId()));
    }

    // 이미지 저장 / 전체 경로 생성 및 반환 함수
    @Transactional
    protected String saveImage(MultipartFile images) throws Exception
    {
        // 이미지 없을 시 에러 반환 -> 나중에 에러가 아닌 디폴트 사진으로 변경 (이미지는 필수 X)
        if (images.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 없습니다.");
        }

        // 고유 파일명 생성
        String fileName = UUID.randomUUID() + images.getOriginalFilename();
        String filePath = uploadPath + File.separator + fileName;

        // 디렉토리 생성
        File dir = new File(uploadPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 파일 저장
        File dest = new File(filePath);
        images.transferTo(dest);

        // 전체 경로 반환
        return filePath;
    }
}
