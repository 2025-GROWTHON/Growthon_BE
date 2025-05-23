package com.growthon.domain.produce.service;

import com.growthon.domain.produce.domain.Produce;
import com.growthon.domain.produce.dto.request.PostProduceRequest;
import com.growthon.domain.produce.dto.request.UpdateProduceRequest;
import com.growthon.domain.produce.dto.response.GetProduceByIdResponse;
import com.growthon.domain.produce.dto.response.GetProducesResponse;
import com.growthon.domain.produce.dto.response.UpdateProduceResponse;
import com.growthon.domain.produce.exception.AccessDeniedException;
import com.growthon.domain.produce.exception.NoImageException;
import com.growthon.domain.produce.exception.NotFoundProduceException;
import com.growthon.domain.produce.repository.ProduceRepository;
import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.exception.NotFoundUserException;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.global.error.ErrorCode;
import com.growthon.global.error.exception.BusinessException;
import com.growthon.global.response.ApiResponse;
import com.growthon.global.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProduceService {

    private final ProduceRepository produceRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public ProduceService(ProduceRepository produceRepository, UserRepository userRepository) {
        this.produceRepository = produceRepository;
        this.userRepository = userRepository;
    }

    // 이미지 저장 / 전체 경로 생성 및 반환 함수
    @Transactional
    public String saveImage(MultipartFile images) throws BusinessException, IOException
    {
        // 이미지 없을 시 에러 반환 -> 나중에 에러가 아닌 디폴트 사진으로 변경 (이미지는 필수 X)
        if (images.isEmpty()) {
            throw new NoImageException(ErrorCode.NO_IMAGE_FILE);
        }

        // 고유 파일명 생성
        String fileName = images.getOriginalFilename();
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
        return fileName;
    }

    // 상품 조회 및 권한 확인 함수
    private Produce getProduce(long produceId, CustomUserDetails userDetails) {
        // User 불러오기
        Long userId = userDetails.getUser() != null ? userDetails.getId() : null;
        if (userId == null) {
            throw new NotFoundUserException(ErrorCode.NOT_FOUND_USER);
        }

        // 상품 조회
        Produce produce = produceRepository.findByProduceId(produceId)
                .orElseThrow(() -> new NotFoundProduceException(ErrorCode.NOT_FOUND_PRODUCE));

        // 권한 확인
        if (!Objects.equals(produce.getUser().getId(), userId)) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
        return produce;
    }

    // Produce Post Service
    @Transactional
    public ResponseEntity<ApiResponse<Long>> postProduce(
            PostProduceRequest request,
            MultipartFile images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException, IOException {
        // User 불러오기
        Long userId = userDetails.getUser() != null ? userDetails.getId() : null;
        if (userId == null) {
            throw new NotFoundUserException(ErrorCode.NOT_FOUND_USER);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_USER));

        // 이미지 저장, 경로 반환
        String imagePath = saveImage(images);

        Produce produce = produceRepository.save(new Produce(request, imagePath, user));

        return ResponseEntity.ok(ApiResponse.of(201, "상품이 정상적으로 등록되었습니다.", produce.getProduceId()));
    }

    // Produces Get Service (ALL)
    @Transactional
    public ResponseEntity<ApiResponse<List<GetProducesResponse>>> getProduces() {
        List<GetProducesResponse> produces = produceRepository.findAll()
                .stream()
                .map(GetProducesResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.of(200, "상품 목록 조회 성공", produces));
    }

    // Produce Get Service (Detail)
    @Transactional
    public ResponseEntity<ApiResponse<GetProduceByIdResponse>> getProduceById(long produceId) throws NotFoundProduceException {
        Produce produce = produceRepository.findByProduceId(produceId)
                .orElseThrow(() -> new NotFoundProduceException(ErrorCode.NOT_FOUND_PRODUCE));
        return ResponseEntity.ok(ApiResponse.of(200,"상품 조회 성공", new GetProduceByIdResponse(produce)));
    }

    // Produce Put Service
    @Transactional
    public ResponseEntity<ApiResponse<UpdateProduceResponse>> updateProduce (
            long produceId,
            UpdateProduceRequest request,
            MultipartFile images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException, IOException {
        // 상품 조회 및 권한 확인
        Produce produce = getProduce(produceId, userDetails);

        // 이미지 저장, 경로 반환
        String imagePath = saveImage(images);

        // 수정 및 반환
        return ResponseEntity.ok(ApiResponse.of(200,"상품 정보가 수정되었습니다.", new UpdateProduceResponse(produceId, produce.updateProduce(request, imagePath).getUpdateAt())));
    }

    // Produce Delete Service
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteProduce(
            long produceId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException {
        // 상품 조회 및 권한 확인
        Produce produce = getProduce(produceId, userDetails);

        // 삭제
        produceRepository.delete(produce);

        return ResponseEntity.ok(ApiResponse.of(200, "상품이 삭제되었습니다.", null));
    }

}
