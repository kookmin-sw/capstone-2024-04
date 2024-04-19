package com.drm.server.controller.client;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.request.ApplyRequest;
import com.drm.server.controller.dto.request.DashboardRequest;
import com.drm.server.controller.dto.response.DashboardResponse;
import com.drm.server.domain.user.CustomUserDetails;
import com.drm.server.domain.user.User;
import com.drm.server.service.DashboardService;
import com.drm.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard",description = "대시보드 API")
@CrossOrigin(origins = "http://localhost:5173")
public class DashBoardController {
    @Autowired
    UserService userService;
    @Autowired
    DashboardService dashboardService;

    @GetMapping()
    @Operation(summary = "전체 대시보드 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<APIResponse<List<DashboardResponse.DashboardInfo>>> getDashboards(@AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getCustomUserInfo().getUserId();
        List<DashboardResponse.DashboardInfo> dashboards = dashboardService.getDashboardsByUserId(userId);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, dashboards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("{dashboardId}")
    @Operation(summary = "광고 단위별 대시보드 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<APIResponse<List<DashboardResponse.DashboardDataInfo>>> getDashboardsById(@PathVariable Long dashboardId, @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getCustomUserInfo().getUserId();
        DashboardResponse.DashboardDataInfo board = dashboardService.getDashboardWithDataById(userId, dashboardId);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, board);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{dashboardId}/board")
    @Operation(summary = "신청 단위(광고 + 집행 시간) 별 대시보드 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<APIResponse<List<DashboardResponse.RegisteredMediaAppInfo>>> getBoards(@PathVariable Long dashboardId, @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getCustomUserInfo().getUserId();
        List<DashboardResponse.RegisteredMediaAppInfo> boards = dashboardService.getRegisteredBoardsById(userId, dashboardId);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("{dashboardId}/board/{boardId}")
    @Operation(summary = "날짜 별(광고 + 집행기간 + 일(day)) 단위 대시보드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<APIResponse<List<DashboardResponse.DashboardDataInfo>>> getBoardPerDay(@PathVariable Long dashboardId, @PathVariable Long boardId, @Valid @RequestBody DashboardRequest.dataPerDay dayData, @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getCustomUserInfo().getUserId();
        // Define the date format  & Parse the string to LocalDate
        LocalDate localDate = LocalDate.parse(dayData.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DashboardResponse.DashboardDataInfo boards = dashboardService.getDayBoards(userId, dashboardId, localDate);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/compare")
    @Operation(summary = "비교하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public void compareApplication(@RequestBody ApplyRequest.MediaApplicationList mediaApplicationList , @AuthenticationPrincipal CustomUserDetails userDetails ){

    }

    // 특정 Location Id 를 받았을때, 해당 Location 으로 조회된 media_daily_board 데이터들을 합쳐서 반환해준다.
    @PostMapping("location/{locationId}")
    @Operation(summary = "Location(디스플레이) 단위 대시보드 (현재 개발 중)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<APIResponse<DashboardResponse.LocationDataInfo>> getBoardPerLocation(@PathVariable Long locationId, @AuthenticationPrincipal CustomUserDetails userDetails){
        // Long userId = userDetails.getCustomUserInfo().getUserId();
        // 유동인구 데이터는 광고 신청 전에도 노출이 가능한 정보라고 판단하여 user 검증을 우선 제외한다.
        DashboardResponse.LocationDataInfo board = dashboardService.getDashboardPerLocation(locationId);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, board);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
