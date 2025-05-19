package com.growthon.domain.produce.service;

import com.growthon.domain.produce.domain.Produce;
import com.growthon.domain.produce.dto.request.PostProduceRequest;
import com.growthon.domain.produce.dto.request.UpdateProduceRequest;
import com.growthon.domain.produce.dto.response.GetProduceByIdResponse;
import com.growthon.domain.produce.dto.response.GetProducesResponse;
import com.growthon.domain.produce.dto.response.UpdateProduceResponse;
import com.growthon.domain.produce.exception.NotFoundProduceException;
import com.growthon.domain.produce.repository.ProduceRepository;
import com.growthon.domain.user.domain.User;
import com.growthon.domain.user.repository.UserRepository;
import com.growthon.domain.user.service.UserService;
import com.growthon.global.error.ErrorCode;
import com.growthon.global.response.ApiResponse;
import com.growthon.global.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.growthon.domain.produce.exception.AccessDeniedException;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProduceService {

    private final ProduceRepository produceRepository;
    private final UserRepository userRepository;

    // application-local에 path를 설정해야 함
    // e.g. upload.path = C:\Users\경로...
    @Value("${upload.path}")
    private String uploadPath;

    public ProduceService(ProduceRepository produceRepository, UserRepository userRepository) {
        this.produceRepository = produceRepository;
        this.userRepository = userRepository;
    }

    // Produce Post Service
    @Transactional
    public ResponseEntity<ApiResponse<Long>> postProduce(
            PostProduceRequest request,
            MultipartFile images,
            @AuthenticationPrincipal Object userDetails // Object로 주입된 경우
    ) throws Exception {
        // userDetails 타입 확인 및 캐스팅
        if (!(userDetails instanceof CustomUserDetails)) {
            throw new IllegalArgumentException("userDetails가 CustomUserDetails 타입이 아닙니다.");
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        System.out.println("customUserDetails: " + customUserDetails);
        System.out.println("customUserDetails.getUser(): " + customUserDetails.getUser());
        System.out.println("customUserDetails.getId(): " + customUserDetails.getId());

        // getUser() null 확인
        System.out.println("CustomUserDetails.getUser(): " + customUserDetails.getUser());
        if (customUserDetails.getUser() == null) {
            throw new IllegalArgumentException("CustomUserDetails.getUser()가 null입니다.");
        }

        // userId 가져오기
        Long userId = customUserDetails.getId();
        if (userId == null) {
            throw new IllegalArgumentException("인증 정보에 userId가 없습니다.");
        }

        // 이후 로직
        String imagePath = saveImage(images);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No Such User Found"));

        Produce produce = produceRepository.save(new Produce(request, imagePath, user));

        return ResponseEntity.ok(ApiResponse.of(201, "상품이 정상적으로 등록되었습니다.", produce.getProduceId()));
    }

    // 이미지 저장 / 전체 경로 생성 및 반환 함수
    @Transactional
    public String saveImage(MultipartFile images) throws Exception
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
    public ResponseEntity<ApiResponse<GetProduceByIdResponse>> getProduceById(Long produceId) throws NotFoundProduceException {
        Produce produce = produceRepository.findByProduceId(produceId)
                .orElseThrow(() -> new NotFoundProduceException(ErrorCode.NOT_FOUND_PRODUCE));
        return ResponseEntity.ok(ApiResponse.of(200,"상품 조회 성공", new GetProduceByIdResponse(produce)));
    }

    // Produce Put Service
    @Transactional
    public ResponseEntity<ApiResponse<UpdateProduceResponse>> updateProduce (
            Long produceId,
            UpdateProduceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException {
        // 상품 조회
        Produce produce = produceRepository.findByProduceId(produceId)
                .orElseThrow(() -> new NotFoundProduceException(ErrorCode.NOT_FOUND_PRODUCE));
        // 권한 확인
        if (!Objects.equals(produce.getUser().getId(), userDetails.getUser().getId())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
        // 수정 및 반환
        return ResponseEntity.ok(ApiResponse.of(200,"상품 정보가 수정되었습니다.", new UpdateProduceResponse(produceId, produce.updateProduce(request).getUpdateAt())));
    }

    // Produce Delete Service
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteProduce(
            Long produceId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws RuntimeException {

        // 상품 조회
        Produce produce = produceRepository.findByProduceId(produceId)
                .orElseThrow(() -> new NotFoundProduceException(ErrorCode.NOT_FOUND_PRODUCE));

        // 권한 확인
        if (!Objects.equals(produce.getUser().getId(), userDetails.getUser().getId())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }

        // 삭제
        produceRepository.delete(produce);

        return ResponseEntity.ok(ApiResponse.of(200, "상품이 삭제되었습니다.", null));
    }
}