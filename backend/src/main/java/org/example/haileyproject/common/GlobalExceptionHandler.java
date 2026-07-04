package org.example.haileyproject.common;

import org.example.haileyproject.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 요청 값 검증(@Valid) 실패 시
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ApiResponse.error("유효하지 않은 요청입니다.", errors);
    }

    // 2. JSON 데이터 형식이 틀렸을 때 (Unrecognized field 에러 등)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return ApiResponse.error("요청 데이터 형식이 올바르지 않습니다. JSON 필드명을 확인해주세요.");
    }

    // 3. 잘못된 메서드 요청 등 (여기를 수정!)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<String> handleBadRequestExceptions(Exception ex) {
        return ApiResponse.error("유효하지 않은 요청입니다. 에러 타입: " + ex.getClass().getSimpleName());
    }

    // 4. 서비스 로직에서 던진 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ex.printStackTrace();
        Map<String, String> errorData = new HashMap<>();
        errorData.put("error", ex.getMessage());
        return ApiResponse.error("유효하지 않은 요청입니다.", errorData);
    }

    // 5. 서버 알 수 없는 에러
    @ExceptionHandler(ResponseStatusException.class)
    public org.springframework.http.ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        return org.springframework.http.ResponseEntity
                .status(ex.getStatusCode())
                .body(ApiResponse.error(ex.getReason() != null ? ex.getReason() : "요청 처리에 실패했습니다."));
    }

    // 6. 서버 알 수 없는 에러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return ApiResponse.error("서버 에러");
    }
}
