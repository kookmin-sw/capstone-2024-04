package com.drm.server.domain.detectedface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Converter
public class DetectedDataConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> list) {
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
    public List<Integer> convertToEntityAttribute(String dbData) {
        return null;
    }
}
