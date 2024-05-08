package com.drm.server.messageq;

import com.drm.server.domain.playlist.PlayList;
import com.drm.server.domain.playlist.PlayListRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class MultipleMediaConsumer {
    private final PlayListRepository playListRepository;
    @KafkaListener(topics = "drm-advt-topic")
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message: ->" + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        Integer index = (Integer) map.get("playlist_index");
        PlayList nextMedia = playListRepository.findById(Long.valueOf(index)).orElseThrow(() -> new IllegalArgumentException("없는 플레이"));

        LocalDate currentDate = LocalDate.now();
        PlayList presentMedia = playListRepository.findByLocationAndCreateDateAndPosting(nextMedia.getLocation(), currentDate.atStartOfDay()).orElse(nextMedia);

        presentMedia.unBroadCasting();
        nextMedia.brodCasting();
        playListRepository.save(presentMedia);
        playListRepository.save(nextMedia);

    }
}