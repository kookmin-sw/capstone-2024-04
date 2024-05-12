package com.drm.server.service;

import com.drm.server.domain.dailyDetailBoard.DailyDetailBoard;
import com.drm.server.domain.dailyDetailBoard.DailyDetailBoardRepository;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoardRepository;
import com.drm.server.domain.detectedface.DetectedFace;
import com.drm.server.domain.mediaApplication.MediaApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyDetailBoardService {
    private final DailyDetailBoardRepository dailyDetailBoardRepository;
    public void createDetailBoard(DailyMediaBoard dailyMediaBoard){
        List<DailyDetailBoard> dailyDetailBoards = new ArrayList<>();
        for(int i = 1; i <= 9 ; i++){
            int ageRange = i * 10;
            DailyDetailBoard maleDailyDetailBoard = DailyDetailBoard.toEntity(ageRange, true, dailyMediaBoard);
            dailyDetailBoards.add(maleDailyDetailBoard);
            DailyDetailBoard femaleDailyDetailBoard = DailyDetailBoard.toEntity(ageRange, false, dailyMediaBoard);
            dailyDetailBoards.add(femaleDailyDetailBoard);
        }
        dailyDetailBoardRepository.saveAll(dailyDetailBoards);
    }
    public void updateDatailBoard(DetectedFace detectedFace,DailyMediaBoard dailyMediaBoard) {
//        DailyMediaBoard dailyMediaBoard =  dailyMediaBoardService.findDailyBoardByDateAndApplication(detectedFace.getMediaApplication(),detectedFace.getArriveAt().toLocalDate());
        // 광고의 집행 정보 application 이 Param 으로 들어온다.
        int ageRange = detectedFace.getAge() / 10;
        DailyDetailBoard dailyDetailBoard = findDetailBoardByDateAndApplication(dailyMediaBoard,ageRange,detectedFace.isMale(), detectedFace.getArriveAt().toLocalDate());

        // calculate new Board data
        int dataHour = detectedFace.getArriveAt().getHour() % 24;
//        관심이 있다
        if(detectedFace.getFaceCaptureCnt() > 0){
            dailyDetailBoard.updateAvgStaringTime(detectedFace.getFaceCaptureCnt(), detectedFace.getFps()); //평균시선 시간
            dailyDetailBoard.updateHourlyAvgStaringTime(dataHour, detectedFace.getFaceCaptureCnt(), detectedFace.getFps()); //시간별 평균시선
            dailyDetailBoard.addHourlyInterestedCount(dataHour); // 시간별 관심인구
            dailyDetailBoard.addInterestCount();

        }

        dailyDetailBoard.addHourlyPassedCount(dataHour); //시간별 유동인구
        dailyDetailBoard.addTotalPeopleCount(); // 통유동인구

        dailyDetailBoardRepository.save(dailyDetailBoard);
        log.info(" {} : 광고 상세 데이터 업데이트 ",dailyDetailBoard.getDailyMediaBoard().getMediaApplication().getMediaApplicationId());
    }
    public DailyDetailBoard findDetailBoardByDateAndApplication(DailyMediaBoard dailyMediaBoard,int ageRange, boolean male, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // End of the day (23:59:59.999999999)
        log.info("{},{},{},{},",dailyMediaBoard.getMediaDataId(),ageRange,male,date);

        return dailyDetailBoardRepository.findByDailyMediaBoardAndAgeRangeAndMaleAndCreateDateBetween(dailyMediaBoard, ageRange * 10, male,startOfDay, endOfDay).orElseThrow(()-> new IllegalArgumentException("DailyMediaboard is Null"));
    }
}
