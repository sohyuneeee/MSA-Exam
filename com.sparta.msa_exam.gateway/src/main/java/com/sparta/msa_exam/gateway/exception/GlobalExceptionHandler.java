//package com.sparta.msa_exam.gateway.exception;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.support.NotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import static org.springframework.boot.web.servlet.server.Encoding.DEFAULT_CHARSET;
//import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;
//import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException e) {
//        logError(e);
//        ErrorCode errorCode = e.getErrorCode();
//        return createResponseEntity(errorCode);
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<CustomErrorResponse> handleNotFoundException(NotFoundException e) {
//        logError(e);
//        return createResponseEntity(ErrorCode.COMMON_SERVICE_UNAVAILABLE);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CustomErrorResponse> handleException(Exception e) {
//        logError(e);
//        if (e.getCause() instanceof CustomException customException) {
//            return createResponseEntity(customException.getErrorCode());
//        }
//        return createResponseEntity(INTERNAL_SERVER_ERROR, ErrorCode.COMMON_SERVER_ERROR.name(), e.getMessage());
//    }
//
//    private ResponseEntity<CustomErrorResponse> createResponseEntity(ErrorCode errorCode) {
//        return createResponseEntity(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
//    }
//
//    private ResponseEntity<CustomErrorResponse> createResponseEntity(HttpStatus httpStatus, String code, String message) {
//        CustomErrorResponse customErrorResponse = CustomErrorResponse.builder()
//                .code(code)
//                .message(message)
//                .build();
//        return ResponseEntity
//                .status(httpStatus)
//                .header(CONTENT_ENCODING, DEFAULT_CHARSET.name())
//                .contentType(APPLICATION_JSON)
//                .body(customErrorResponse);
//    }
//
//    private void logError(Exception e) {
//        log.error(e.getClass().getSimpleName(), e);
//    }
//
//}
