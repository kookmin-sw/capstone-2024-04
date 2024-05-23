package com.drm.server.controller.client;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.FileDto;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.UserRequest;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.User;
import com.drm.server.service.FileService;
import com.drm.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "user",description ="user 정보 관련")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final FileService fileService;
    private final UserService userService;
    @Operation(summary = "프로필 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping(path = "profile",consumes =  {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<APIResponse<UserResponse.UserInfo>> updateProfile(@RequestPart(value = "file",required = false)MultipartFile multipartFile, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User getUser =userService.getUser(userDetails.getUsername());
        FileDto fileDto = fileService.uploadFile("drmprofile",multipartFile);
        UserResponse.UserInfo userInfo= userService.updateProfile(getUser, fileDto);
        APIResponse response = APIResponse.of(SuccessCode.UPDATE_SUCCESS, userInfo);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "비밀번호 확인", description = "사용자화면에서 비밀번호 확인을 위한 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping(path = "verify-password")
    public ResponseEntity<APIResponse> verfiyPassword(@RequestBody UserRequest.Password password, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser =userService.getUser(userDetails.getUsername());
        userService.verifyPassword(getUser,password.getPassword());
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, "비밀번호 인증 성공");
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경전 기존 비밀번호 재검증을 위해 기존 비밀번호도 같이 넣어주세요(최종 변경)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping(path = "password")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody UserRequest.NewPassword password, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser =userService.getUser(userDetails.getUsername());
        userService.updatePassword(getUser,password.getPassword(),password.getUpdatePassword());
        APIResponse response = APIResponse.of(SuccessCode.UPDATE_SUCCESS, "비밀번호 변경 성공");
        return ResponseEntity.ok(response);
    }
//    @Operation(summary = "탈퇴", description = "비밀번호 변경전 기존 비밀번호 재검증을 위해 기존 비밀번호도 같이 넣어주세요(최종 변경)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "성공"),
//            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//    })
//    @PostMapping(path = "withdrwal")
//    public ResponseEntity<APIResponse> withdrawal(@AuthenticationPrincipal CustomUserDetails userDetails){
//        User getUser =userService.getUser(userDetails.getUsername());
//        userService.withdrawal(getUser);
//
//    }

}
