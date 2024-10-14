package com.fpu.exe.cleaninghub.utils.wapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpu.exe.cleaninghub.common.Status;
import org.springframework.http.HttpStatus;

public class API {
    public static class Response<T> {
        @JsonProperty("status")
        private int status;

        @JsonProperty("code")
        private int code;

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private T data;

        @JsonProperty("error")
        private String error;


        public Response(int status, int code, String message, T data, String error) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.data = data;
            this.error = error;
        }

        public static <T> Response<T> success(T data) {
            return new Response<>(Status.SUCCESS.value(), HttpStatus.OK.value(), "Success", data,null);
        }

        public static <T> Response<T> error( HttpStatus code, String message,String error) {
            return new Response<>( Status.ERROR.value(),code.value(), message, null,error);
        }
    }
}
