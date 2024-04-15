package com.drm.server.service;

import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoardRepository;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.mediaApplication.MediaApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.ion.NullValueException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyMediaBoardService {

//    private final MediaApplicationService mediaApplicationService;
    private final DailyMediaBoardRepository dailyMediaBoardRepository;
    public void createDailyBoard(MediaApplication mediaApplication, LocalDate date) {
        DailyMediaBoard initBoard = DailyMediaBoard.toEntity(mediaApplication);

        dailyMediaBoardRepository.save(initBoard);
        log.info("CREATE DAILY DATA : " + date);
    }
    public void updateDailyBoard(DetectedFace detectedFace) {
        // 광고의 집행 정보 application 이 Param 으로 들어온다.
        DailyMediaBoard dailyMediaBoard = findDailyBoardByDateAndApplication(detectedFace.getMediaApplication(), detectedFace.getArriveAt().toLocalDate());

        // calculate new Board data
        int dataHour = detectedFace.getArriveAt().getHour() % 24;
        validateDailyMediaBoard(dailyMediaBoard);
//        평균 값 계산 (Total 값 넣기전)
        dailyMediaBoard.updateAvgAge(detectedFace.getAge());
//        관심이 있다
        if(detectedFace.getFaceCaptureCnt() > 0){
            dailyMediaBoard.updateAvgStaringTime(detectedFace.getFaceCaptureCnt());
            dailyMediaBoard.addHourlyInterestedCount(dataHour);

            if(detectedFace.isMale()){
                dailyMediaBoard.addMaleInterestCnt();
            }else dailyMediaBoard.addFemaleInterestCnt();

        }

        if (detectedFace.isMale()) dailyMediaBoard.addMaleCnt();
        dailyMediaBoard.addHourlyPassedCount(dataHour);
        dailyMediaBoard.addTotalPeopleCount();

        dailyMediaBoardRepository.save(dailyMediaBoard);
        log.info("{} dailyboard 수정",dailyMediaBoard.getModifiedDate());
    }
    private void validateDailyMediaBoard(DailyMediaBoard prevBoard) {
        if(prevBoard.getHourlyPassedCount() == null){
            throw new IllegalStateException("DAILY BOARD HOURLY PASSED LIST IS NULL");
        }
        if(prevBoard.getHourlyInterestedCount() == null){
            throw new IllegalStateException("DAILY BOARD HOURLY INTERESTED LIST IS NULL");
        }
        if(prevBoard.getHourlyPassedCount().size() != 24){
            throw new IllegalStateException("DAILY BOARD HOURLY PASSED LIST SIZE PROBLEM");
        }
        if(prevBoard.getHourlyInterestedCount().size() != 24){
            throw new IllegalStateException("DAILY BOARD HOURLY INTERESTED LIST SIZE PROBLEM");
        }
    }

    public DailyMediaBoard findDailyBoardByDateAndApplication(MediaApplication application, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // End of the day (23:59:59.999999999)

        return dailyMediaBoardRepository.findByMediaApplicationAndCreateDateBetween(application, startOfDay, endOfDay).orElseThrow(()-> new IllegalArgumentException("DailyMediaboard is Null"));
    }
}
