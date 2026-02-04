package com.study.my_spring_study_diary.global.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"success", "data", "error"})
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ErrorInfo error;

    private ApiResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ErrorInfo getError() {
        return error;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.data = data;
        response.error = null;
        return response;
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.data = null;
        response.error = new ErrorInfo(code, message);
        return response;
    }

    public static class ErrorInfo {
        private String code;
        private String message;

        public ErrorInfo() {
        }

        public ErrorInfo(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}