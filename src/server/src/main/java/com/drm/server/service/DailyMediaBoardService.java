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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyMediaBoardService {

//    private final MediaApplicationService mediaApplicationService;
    private final DailyMediaBoardRepository dailyMediaBoardRepository;
    public void createDailyBoard(MediaApplication mediaApplication, LocalDate date){
        List<Long> hourlyInterestList = new ArrayList<>(Arrays.asList(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L));
        List<Long> hourlyPassedList = new ArrayList<>(Arrays.asList(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L));

        DailyMediaBoard dailyData = DailyMediaBoard.builder().totalPeopleCount(0L)
                .hourlyInterestedCount(hourlyInterestList).hourlyPassedCount(hourlyPassedList)
                .mediaApplication(mediaApplication)
                .maleInterestCnt(0L).femaleInterestCnt(0L).maleCnt(0L)
                .avgStaringTime(0F).avgAge(0F)
                .build();
        dailyMediaBoardRepository.save(dailyData);
        String msg = "CREATE DAILY DATA : " + date;
        log.info(msg);
    }

    public void updateMediaData(MediaApplication application, ModelRequest modelRequest, boolean interestBool) {
        // 광고의 집행 정보 application 이 Param 으로 들어온다.
        LocalDate date = modelRequest.getArriveTime().toLocalDate();
        DailyMediaBoard prevBoard = findDailyBoardByDateAndApplication(application, date);


        // calculate new Board data
        int dataHour = modelRequest.getArriveTime().getHour() % 12;
        validateDailyMediaBoard(prevBoard);

        List<Long> hourlyInterest = prevBoard.getHourlyInterestedCount();
        List<Long> hourlyPassed = prevBoard.getHourlyPassedCount();
        if(interestBool){
            hourlyInterest = prevBoard.getHourlyInterestedCount();
            Long value = hourlyInterest.get(dataHour);
            hourlyInterest.set(dataHour, value + 1);
        }
        hourlyPassed = prevBoard.getHourlyPassedCount();
        Long value = hourlyPassed.get(dataHour);
        hourlyPassed.set(dataHour, value + 1);

        Long maleInterestCnt = prevBoard.getMaleInterestCnt();
        Long femaleInterestCnt = prevBoard.getFemaleInterestCnt();
        if(interestBool){
            if(modelRequest.isMale()){
                maleInterestCnt = prevBoard.getMaleInterestCnt() + 1;
            }
            else {
                femaleInterestCnt = prevBoard.getFemaleInterestCnt() + 1;
            }
        }
        Long maleCnt = prevBoard.getMaleCnt();
        if(modelRequest.isMale()) {maleCnt += 1;}


        Long totalCnt = prevBoard.getTotalPeopleCount();
        float newAvgStaringTime = ((prevBoard.getAvgStaringTime() * totalCnt ) + modelRequest.getInterestFrameCnt()) /  (totalCnt + 1);
        float newAvgAge = ((prevBoard.getAvgAge() * totalCnt) + modelRequest.getAge()) / (totalCnt + 1);
        totalCnt += 1;

        DailyMediaBoard newBoard = DailyMediaBoard.builder()
                .mediaDataId(prevBoard.getMediaDataId()).mediaApplication(prevBoard.getMediaApplication())
                .hourlyInterestedCount(hourlyInterest).hourlyPassedCount(hourlyPassed)
                .maleInterestCnt(maleInterestCnt).femaleInterestCnt(femaleInterestCnt).maleCnt(maleCnt)
                .avgAge(newAvgAge).avgStaringTime(newAvgStaringTime).totalPeopleCount(totalCnt).build();

        dailyMediaBoardRepository.save(newBoard);
        String msg = "Daily Board Update ";
        log.info(msg);
    }
    public void updateDailyBoard(DetectedFace detectedFace) {
        // 광고의 집행 정보 application 이 Param 으로 들어온다.
        DailyMediaBoard prevBoard = findDailyBoardByDateAndApplication(detectedFace.getMediaApplication(), detectedFace.getArriveAt().toLocalDate());

        // calculate new Board data
        int dataHour = detectedFace.getArriveAt().getHour() % 12;
        validateDailyMediaBoard(prevBoard);

        float newAvgStaringTime = ((prevBoard.getAvgStaringTime() * prevBoard.getTotalPeopleCount() ) + detectedFace.getInterestFrameCnt()) /  (totalCnt + 1);
        float newAvgAge = ((prevBoard.getAvgAge() * prevBoard.getTotalPeopleCount()) + detectedFace.getAge()) / (prevBoard.getTotalPeopleCount() + 1);

        List<Long> hourlyInterest = prevBoard.getHourlyInterestedCount();
        List<Long> hourlyPassed = prevBoard.getHourlyPassedCount();

//        시간별 관심 데이터
        prevBoard.addTotalPeopleCount();
        prevBoard.addHourlyPassedCount(dataHour);
        prevBoard.addMaleCnt();

//        관심이 있다면
        if(detectedFace.getFaceCaptureCnt() > 0){
            prevBoard.addHourlyInterestedCount(dataHour);
            if(detectedFace.isMale()){
                prevBoard.addMaleInterestCnt();
            }else {
                prevBoard.addFemaleInterestCnt();
            }
        }

        Long maleInterestCnt = prevBoard.getMaleInterestCnt();
        Long femaleInterestCnt = prevBoard.getFemaleInterestCnt();



        Long totalCnt = prevBoard.getTotalPeopleCount();
        totalCnt += 1;

        DailyMediaBoard newBoard = DailyMediaBoard.builder()
                .mediaDataId(prevBoard.getMediaDataId()).mediaApplication(prevBoard.getMediaApplication())
                .hourlyInterestedCount(hourlyInterest).hourlyPassedCount(hourlyPassed)
                .maleInterestCnt(maleInterestCnt).femaleInterestCnt(femaleInterestCnt).maleCnt(maleCnt)
                .avgAge(newAvgAge).avgStaringTime(newAvgStaringTime).totalPeopleCount(totalCnt).build();

        dailyMediaBoardRepository.save(newBoard);
        String msg = "Daily Board Update ";
        log.info(msg);
    }
    private void validateDailyMediaBoard(DailyMediaBoard prevBoard) {
        if(prevBoard.getHourlyPassedCount() == null){
            throw new IllegalStateException("DAILY BOARD HOURLY PASSED LIST IS NULL");
        }
        if(prevBoard.getHourlyInterestedCount() == null){
            throw new IllegalStateException("DAILY BOARD HOURLY INTERESTED LIST IS NULL");
        }
        if(prevBoard.getHourlyPassedCount().size() != 12){
            throw new IllegalStateException("DAILY BOARD HOURLY PASSED LIST SIZE PROBLEM");
        }
        if(prevBoard.getHourlyInterestedCount().size() != 12){
            throw new IllegalStateException("DAILY BOARD HOURLY INTERESTED LIST SIZE PROBLEM");
        }
    }

    public DailyMediaBoard findDailyBoardByDateAndApplication(MediaApplication application, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // End of the day (23:59:59.999999999)

        return dailyMediaBoardRepository.findByMediaApplicationAndCreateDateBetween(application, startOfDay, endOfDay).orElseThrow(()-> new IllegalArgumentException("DailyMediaboard is Null"));
    }
}
