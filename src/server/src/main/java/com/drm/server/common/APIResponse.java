package com.drm.server.common;

import com.drm.server.common.enums.SuccessCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "공동 응답 형식")
public class APIResponse<T> {
    @Schema(description = "성공 상태 코드",defaultValue = "200",allowableValues = {"200","201"})
    private int status;                 // 성공 상태 코드
    @Schema(description = "코드 값",defaultValue = "200",allowableValues = {"200","201"})
    private String code;        // 구분 코드
    @Schema(description = "코드 메시지",allowableValues = {"SELECT SUCCESS","DELETE SUCCESS","INSERT SUCCESS","UPDATE SUCCESS"})
    private String msg;           // 성공 메시지
    @Schema(description = "상세 메시지")
    private String detailMsg;

    private T data;

    @Builder
    public APIResponse(final SuccessCode code) {
        this.msg = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.detailMsg = "";
        this.data = (T)new HashMap<String,String>();
    }
    @Builder
    public APIResponse(final SuccessCode code, final T data) {
        this.msg = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.data = data;
        this.detailMsg = "";
    }
    @Builder
        public APIResponse(final SuccessCode code, String detailMsg) {
            this.msg = code.getMessage();
            this.status = code.getStatus();
            this.code = code.getCode();
            this.detailMsg = detailMsg;
            this.data = (T) "";
        }



    public static APIResponse of(final SuccessCode code) {
        return new APIResponse(code);
    }
    public static APIResponse of(final SuccessCode code, Object data){
        return new APIResponse(code, data);
    }
    public static APIResponse of(final SuccessCode code, String detailMsg){
        return new APIResponse(code, detailMsg);
    }


}
