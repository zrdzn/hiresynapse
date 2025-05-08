package dev.zrdzn.hiresynapse.hiresynapsebackend.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError extends RuntimeException {

    private final HttpStatus status;

    public ApiError(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
