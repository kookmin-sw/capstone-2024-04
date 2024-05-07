package com.drm.server.controller;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.response.PlayListResponse;
import com.drm.server.domain.location.Location;
import com.drm.server.domain.playlist.PlayList;
import com.drm.server.service.LocationService;
import com.drm.server.service.PlayListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "BroadCasting",description = "해당 api는 토큰이 필요 없습니다")
@RequestMapping("/none/api")
@RequiredArgsConstructor
public class BroadCastingController {
    private final PlayListService playListService;
    @GetMapping("/{locationId}")
    @Operation(summary = "해당 장소의 오늘 플레이리스트",description = "오늘 플레리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<List<PlayListResponse.TodayList>>> getTodayListByLocationId(@PathVariable Long locationId){
        List<PlayList> playLists = playListService.todayList(locationId);
        List<PlayListResponse.TodayList> todayList = playLists.stream().map(PlayListResponse.TodayList::new).collect(Collectors.toList());
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, todayList);
        return ResponseEntity.ok(response);
    }

}
