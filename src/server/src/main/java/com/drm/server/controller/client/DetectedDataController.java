package com.drm.server.controller.client;

import com.drm.server.common.APIResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.ModelRequest;

import com.drm.server.service.DetectedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class DetectedDataController {

    private final DetectedDataService detectedDataService;

    @PostMapping()
    public ResponseEntity sendReactionData(@RequestBody ModelRequest modelRequest){
        // 모델 서버가 보낸 데이터를 저장하는 컨트롤러
        detectedDataService.processDetectedData(modelRequest);
        APIResponse response = new APIResponse(SuccessCode.INSERT_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
