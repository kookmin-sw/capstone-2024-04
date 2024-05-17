package com.drm.server.controller.dto.response;

import java.util.List;

// DashBoard 계산에 주로 사용되는 연산들
// Template -> Long / Float 타입에 따라 세부 operator 구현 요구됨.
public class DashboardCalculator {
    public static <T extends Number> List<T> sumHourListDataPerHour(List<T> updateList, List<T> inputList){
        if(updateList.size() != inputList.size()) throw new IllegalStateException("HOUR PASSED DATA LIST SIZE IS DIFFERENT");
        for(int i=0; i<inputList.size(); i++){
            updateList.set(i, sum(inputList.get(i), updateList.get(i)));
        }
        return updateList;
    }

    public static <T extends Number> List<T> divideHourListData(List<T> updateList, Long cnt){
        if(cnt == 0) throw new IllegalArgumentException("DIVIDE CNT IS ZERO");
        for(int i=0; i<updateList.size(); i++){
            updateList.set(i, divide(updateList.get(i), cnt));
        }
        return updateList;
    }

    private static <T extends Number> T sum(T a, T b) {
        if (a instanceof Long) {
            return (T) Long.valueOf(a.longValue() + b.longValue());
        } else if (a instanceof Float) {
            return (T) Float.valueOf(a.floatValue() + b.floatValue());
        }
        throw new IllegalArgumentException("Unsupported number type.");
    }

    private static <T extends Number> T divide(T a, long divisor) {
        if (a instanceof Long) {
            return (T) Long.valueOf(a.longValue() / divisor);
        } else if (a instanceof Float) {
            return (T) Float.valueOf(a.floatValue() / divisor);
        }
        throw new IllegalArgumentException("Unsupported number type.");
    }

}
