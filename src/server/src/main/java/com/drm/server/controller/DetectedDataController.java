package com.drm.server.controller;

import com.drm.server.common.APIResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.ModelRequestDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

private DetectedDataService

@RestController
@RequestMapping("/api/model")
public class DetectedDataController {

    @PostMapping("/send")
    public ResponseEntity sendReactionData(@RequestBody ModelRequestDto modelRequestDto){
        // 모델 서버가 보낸 데이터를 저장하는 컨트롤러
        APIResponse response = new APIResponse(SuccessCode.INSERT_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
