package com.drm.server.messageq;

import com.drm.server.common.KoreaLocalDateTime;
import com.drm.server.controller.dto.response.PlayListLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KSqlDBHandler {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
//    private final String ksqlDbUrl = "http://0.0.0.0:8088";
    private final String ksqlDbUrl = "http://43.203.218.109:8088";

    public List<PlayListLog> queryKsqlDb(String query) throws IOException {
        String url = ksqlDbUrl + "/query";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");

        String body = "{\"ksql\": \"" + query + "\", \"streamsProperties\": {\"ksql.streams.auto.offset.reset\": \"earliest\"}}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return parseResponseJson(response.getBody());
    }

    private List<PlayListLog> parseResponseJson(String jsonResponse) throws IOException {
        List<PlayListLog> playlistLogs = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonResponse);

        for (JsonNode node : root) {
            if (node.has("row")) {
                JsonNode columns = node.get("row").get("columns");

                // Extracting values from columns
                String startTime = columns.get(0).asText();
                String endTime = columns.get(1).asText();
                Long playlistId = columns.get(2).asLong();
                Long locationId = columns.get(3).asLong();

                // Creating PlaylistLog object
                PlayListLog playlistLog = new PlayListLog(playlistId, startTime, endTime, locationId);
                playlistLogs.add(playlistLog);
            }
        }

        return playlistLogs;
    }



    public String getFilteredData(Long locationId, String arriveAt, String leaveAt)  {

        String ksqlQuery = "SELECT * FROM playlist_stream " +
                "WHERE location_id =" +locationId +
                " AND NOT (start_time <= '" + arriveAt + "' AND end_time <= '" + arriveAt + "') " +
                "AND NOT (start_time >= '" + leaveAt + "' AND end_time >= '" + leaveAt + "');";
        return ksqlQuery;
    }

}
