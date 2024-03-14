package capstone.server.manager.controller;

import capstone.server.manager.domain.DetectedData;
import capstone.server.manager.dto.DetectedDataDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/model")
public class ModelController {

    @PostMapping("/send")
    public void sendDetectedData(DetectedDataDto detectedDataDto){
        // video -> Model -> Flask 서버의 Request API 가 향하는 곳
        // 예상 포멧 -> 특정 광고 id + 리스트(광고 N분 N~N+X초 러닝타임 구간, 지나간 사람 수, 관심 표현 사람 수)
        return;
    }

}
