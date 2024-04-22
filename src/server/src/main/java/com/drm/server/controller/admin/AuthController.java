package com.drm.server.controller.admin;

import com.drm.server.common.APIResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.UserRequest;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("AdminAuthController")
@RequestMapping("/api/v2/auth/")
@Tag(name = "Admin-Auth")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final TokenService tokenService;
    @Operation(summary = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> sigIn(@Valid @RequestBody UserRequest.SignIn signIn){
        UserResponse.TokenInfo tokenInfo = tokenService.createAdminToken(signIn);
        APIResponse response = APIResponse.of(SuccessCode.INSERT_SUCCESS, tokenInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
