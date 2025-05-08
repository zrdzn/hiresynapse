package dev.zrdzn.hiresynapse.hiresynapsebackend.handler;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.ApiErrorDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiErrorDto> handleApiError(ApiError error) {
        ApiErrorDto errorResponse = new ApiErrorDto(
            error.getStatus().name(),
            error.getStatus().value(),
            error.getMessage()
        );

        return ResponseEntity.status(error.getStatus()).body(errorResponse);
    }

}
