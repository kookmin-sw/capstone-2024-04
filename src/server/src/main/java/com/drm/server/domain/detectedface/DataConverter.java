package com.drm.server.domain.detectedface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Converter
public class DataConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> list) {
        if(CollectionUtils.isEmpty(list)) {
            return new String();
        }

        StringBuffer strData = new StringBuffer("[]");
        for(int i=0; i<list.size(); i++){
            String str;
            if(i!= (list.size()-1)){
                str = list.get(i) + ",";
            }
            else{
                str = list.get(i) + "";
            }
            strData.insert(strData.length()-1, str);
        }
        return new String(strData);
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList();
        }

        // 문자열을 파싱하여 리스트에 추가
        List<Long> list = new ArrayList<>();
        String[] tokens = dbData.substring(1, dbData.length() - 1).split(",");
        for (String token : tokens) {
            list.add(Long.parseLong(token.trim()));
        }
        return list;
    }
}
