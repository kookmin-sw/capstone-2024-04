package com.drm.server.service;

import com.drm.server.controller.dto.request.ModelRequest;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoard;
import com.drm.server.domain.dailyMediaBoard.DailyMediaBoardRepository;
import com.drm.server.domain.mediaApplication.MediaApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.ion.NullValueException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyMediaBoardService {

//    private final MediaApplicationService mediaApplicationService;
    private final DailyMediaBoardRepository dailyMediaBoardRepository;
    public void createDailyData(MediaApplication mediaApplication, LocalDate date){
        DailyMediaBoard dailyData = DailyMediaBoard.builder().mediaApplication(mediaApplication).date(date).build();
        dailyMediaBoardRepository.save(dailyData);
        String msg = "CREATE DAILY DATA : " + date;
        log.info(msg);
    }
    public void updateMediaData(Long mediaId, ModelRequest modelRequest, boolean interestBool) {
        LocalDate date = modelRequest.getArriveTime().toLocalDate();
        Optional<DailyMediaBoard> dailyBoard = dailyMediaBoardRepository.findByMediaIdAndDate(mediaId, date);
        if(dailyBoard.isEmpty()){
            throw new NullValueException("DAILY BOARD NOT EXISTS : " + date);
        }

        // calculate new Board data
        DailyMediaBoard prevBoard = dailyBoard.get();
        int dataHour = modelRequest.getArriveTime().getHour();

        List<Long> hourlyInterest = new ArrayList<>();
        List<Long> hourlyPassed = new ArrayList<>();
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
}
