package com.ssafy.enjoytrip.features.image.adapter.in.web;

import com.ssafy.enjoytrip.common.util.SecurityUtils;
import com.ssafy.enjoytrip.features.image.adapter.in.web.response.UploadImageResponse;
import com.ssafy.enjoytrip.features.image.application.port.in.UploadImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/${api.prefix}/${api.version}/images")
@RequiredArgsConstructor
@Tag(name = "ImageController", description = "이미지 관련 API 제공")
public class ImageController {
    private final UploadImageUseCase uploadImageUseCase;

    @PostMapping
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드한다.")
    public ResponseEntity<List<UploadImageResponse>> uploadImage(
            @RequestPart("files") List<MultipartFile> files) {
        String uid = SecurityUtils.getUserUidBySecurityContextHolder();

        List<UploadImageResponse> responses = files.stream().map(file -> {
            UploadImageUseCase.Command command = ImageMapper.toUploadImageCommand(uid, file);
            UploadImageUseCase.Result result = uploadImageUseCase.uploadImage(command);
            UploadImageResponse response = ImageMapper.toUploadImageResponse(result);
            return response;
        }).toList();

        return ResponseEntity.ok(responses);
    }
}
