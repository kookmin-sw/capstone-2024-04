package com.drm.server.common;

import com.drm.server.common.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "에러 상태 코드", allowableValues = {"400", "401", "403","404","500"})
    private int status;                 // 에러 상태 코드
    @Pattern(regexp = "G(0[0-9]|1[0-6])", message = "Error code should be in the format of G001 to G016")
    private String divisionCode;        // 에러 구분 코드
    @Schema(description = "에러 메시지",example = "handle Validation Exception")
    private String resultMsg;           // 에러 메시지
    @Schema(description = "상세 메시지 (다양한 형태의 오류를 포함할 수 있음)")
    private Object errors;    // 상세 에러 메시지
    @Schema(description = "간단한 에러 이유(상세 메시지의 값이 있는 경우 빈 값이 올수있음)")
    private String reason;              // 에러 이유

    /**
     * ErrorResponse 생성자-1
     * @param code ErrorCode
     */
    @Builder
    protected ErrorResponse(final ErrorCode code) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.divisionCode = code.getDivisionCode();
        this.reason = "";
        this.errors = "";
    }

    /**
     * ErrorResponse 생성자-2
     *
     * @param code   ErrorCode
     * @param reason String
     */
    @Builder
    protected ErrorResponse(final ErrorCode code, final String reason) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.divisionCode = code.getDivisionCode();
        this.reason = reason;
        this.errors = "";
    }

    /**
     * ErrorResponse 생성자-3
     *
     * @param code   ErrorCode
     * @param errors List<FieldError>
     */
    @Builder
    protected ErrorResponse(final ErrorCode code, final Object errors) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.divisionCode = code.getDivisionCode();
        this.reason = "";
    }


    /**
     * Global Exception 전송 타입-1
     *
     * @param code          ErrorCode
     * @param bindingResult BindingResult
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    /**
     * Global Exception 전송 타입-2
     *
     * @param code ErrorCode
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    /**
     * Global Exception 전송 타입-3
     *
     * @param code   ErrorCode
     * @param reason String
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }
    public static ErrorResponse of(final ErrorCode code, final Object errors) {
        return new ErrorResponse(code, errors);
    }
    @Getter
    @Setter
    @Schema(description = "request 형식에 맞지 않음")
    public static class ErrorResponseWithFieldError{
        @Schema(description = "에러 상태 코드", defaultValue = "500", allowableValues = {"400", "401", "403","404","500"})
        private int status;                 // 에러 상태 코드
        @Pattern(regexp = "G(0[0-9]|1[0-6])", message = "Error code should be in the format of G001 to G016")
        private String divisionCode;        // 에러 구분 코드
        @Schema(description = "에러 메시지")
        private String resultMsg;           // 에러 메시지
        private List<FieldError> errors;    // 상세 에러 메시지
        private String reason;
    }


    /**
     * 에러를 e.getBindingResult() 형태로 전달 받는 경우 해당 내용을 상세 내용으로 변경하는 기능을 수행한다.
     */
    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;

        }
    }
    public ErrorResponse invalidFields(LinkedList<LinkedHashMap<String,String>> errors){
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,errors);
    }
}
