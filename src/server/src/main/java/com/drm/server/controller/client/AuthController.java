package com.drm.server.controller.client;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.UserRequest;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.service.TokenService;
import com.drm.server.service.UserService;
import com.google.firebase.internal.FirebaseService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "auth",description = "인증이 필요없는 api")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;
    @Hidden
    @Operation(summary = "인증번호 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/email/verification-request")
    public ResponseEntity<APIResponse> sendAuthNumByEmail(@Valid @RequestBody UserRequest.EmailRequest emailRequest)  {

        userService.sendCodeToEmail(emailRequest.getEmail());
        APIResponse response = APIResponse.of(SuccessCode.INSERT_SUCCESS, "이메일 인증 번호를 전송하였습니다");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Hidden
    @Operation(summary = "인증번호 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/email/verification")
    public ResponseEntity<APIResponse> verificationEmail( @Valid @RequestBody UserRequest.EmailAuth emailAuth){
        String detailMsg =  userService.verifiedCode(emailAuth.getEmail(), emailAuth.getAuthCode());
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, detailMsg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<UserResponse.UserInfo>> signUp(@Valid @RequestBody UserRequest.SignUp signUp){
//        userService.verifiedCode(signUp.getEmail(), signUp.getAuthCode());
        userService.checkDuplicatedEmail(signUp.getEmail());
        UserResponse.UserInfo userInfo = userService.createUser(signUp.getEmail(), signUp.getPassword(),signUp.getCompany());
        APIResponse response = APIResponse.of(SuccessCode.INSERT_SUCCESS, userInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @Operation(summary = "로그인")
    @PostMapping("/signin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> sigIn(@Valid @RequestBody UserRequest.SignIn signIn){
        UserResponse.TokenInfo tokenInfo = tokenService.createToken(signIn);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, tokenInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
