package hellocloud.pricer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "The server is in an unexpected state")
public class PricerRuntimeException extends RuntimeException {
    public PricerRuntimeException(String message) {
        super(message);
    }

    public PricerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
