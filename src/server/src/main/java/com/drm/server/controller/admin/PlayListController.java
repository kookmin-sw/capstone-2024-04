package com.drm.server.controller.admin;

import com.drm.server.common.APIResponse;
import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.SuccessCode;
import com.drm.server.controller.dto.response.PlayListResponse;
import com.drm.server.domain.playlist.PlayList;
import com.drm.server.service.PlayListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/v2/playlist")
@Tag(name = "PlayList")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlayListController {
    private final PlayListService playListService;
    @PostMapping
    @Operation(summary = "플레이리스트 수동 등록",description = "오늘날짜에 포함된 신청을 플레리스트로 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "토큰 시간 만료, 형식 오류,로그아웃한 유저 접근,헤더에 값이 없을때",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 없는 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<List<PlayListResponse.TodayList>>> createPlayList(){
        List<PlayList> playLists = playListService.updatePlayList();
        List<PlayListResponse.TodayList> todayList = playLists.stream().map(PlayListResponse.TodayList::new).collect(Collectors.toList());
        APIResponse response = APIResponse.of(SuccessCode.INSERT_SUCCESS, todayList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }



}
