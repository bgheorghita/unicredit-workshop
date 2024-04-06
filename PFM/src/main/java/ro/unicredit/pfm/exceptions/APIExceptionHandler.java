package ro.unicredit.pfm.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String DEFAULT_NOT_FOUND_MSG = "Requested resource not found.";
    private final static String DEFAULT_UNEXPECTED_ERROR_MSG = "Unexpected error";
    private final static Logger LOGGER = LoggerFactory.getLogger(APIExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage() == null ? DEFAULT_NOT_FOUND_MSG
                : ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, DEFAULT_UNEXPECTED_ERROR_MSG, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
