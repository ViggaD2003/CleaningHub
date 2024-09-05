package com.fpu.exe.cleaninghub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(StudentNotFoundException.class)
//    public ResponseEntity<ErrorObject> handleStudentNotFoundException(StudentNotFoundException ex, WebRequest request){
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(ClassEntityNotFoundException.class)
//    public ResponseEntity<ErrorObject> handleClassNotFoundException(ClassEntityNotFoundException ex, WebRequest request){
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorObject> handleStudentNotFoundException(NotFoundException ex, WebRequest request){
//        ErrorObject errorObject = new ErrorObject();
//        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
//        errorObject.setMessage(ex.getMessage());
//        errorObject.setTimestamp(new Date());
//
//        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
//    }


}
