package com.drm.server.controller.client;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.ApplyRequest;
import com.drm.server.controller.dto.response.MediaApplicationResponse;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.media.Media;
import com.drm.server.domain.mediaApplication.MediaApplication;
import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.User;
import com.drm.server.service.LocationService;
import com.drm.server.service.MediaApplicationService;
import com.drm.server.service.MediaService;
import com.drm.server.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/media/")
@Tag(name = "Apply")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ApplyController {
    private final MediaService mediaService;
    private final UserService userService;
    private final LocationService locationService;
    private final MediaApplicationService mediaApplicationService;
    @PostMapping("{mediaId}/apply")
    @Operation(summary = "광고 신청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<MediaApplicationResponse.TotalApplicationInfo>> apply (@PathVariable Long mediaId, @RequestBody ApplyRequest.Create request, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser = userService.getUser(userDetails.getUsername());
        Location location = locationService.findById(request.getLocationId());
        Media media = mediaService.findById(mediaId,getUser);

        MediaApplication mediaApplication = mediaApplicationService.createMediaApplication(media, location, request.getStartDate(), request.getEndDate());
        MediaApplicationResponse.TotalApplicationInfo info = new MediaApplicationResponse.TotalApplicationInfo(mediaApplication);

        APIResponse response = APIResponse.of(SuccessCode.INSERT_SUCCESS, info);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("{mediaId}/apply/{applyId}")
    @Operation(summary = "신청 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> unApply (@PathVariable Long mediaId,@PathVariable Long applyId, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser = userService.getUser(userDetails.getUsername());
        mediaApplicationService.deleteMediaApplication(mediaId, applyId,getUser);
        APIResponse response = APIResponse.of(SuccessCode.DELETE_SUCCESS);
        return ResponseEntity.ok(response);

    }
    @Hidden
    @PatchMapping("{mediaId}/apply/{applyId}")
    @Operation(summary = "신청 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public void updateApply (@PathVariable Long mediaId, @PathVariable Long applyId,@AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser = userService.getUser(userDetails.getUsername());

    }
    @GetMapping("{mediaId}/apply")
    @Operation(summary = "신청 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<List<MediaApplicationResponse.MediaApplicationInfo>>> getApplications(@PathVariable Long mediaId, @AuthenticationPrincipal CustomUserDetails userDetails){
        User getUser = userService.getUser(userDetails.getUsername());
        Media getMedia = mediaService.findById(mediaId,getUser);
        List<MediaApplication> mediaApplications = mediaApplicationService.findByMedia(getMedia);
        List<MediaApplicationResponse.MediaApplicationInfo> totalApplicationInfos = mediaApplications.stream().map(MediaApplicationResponse.MediaApplicationInfo::new).collect(Collectors.toList());
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, totalApplicationInfos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

