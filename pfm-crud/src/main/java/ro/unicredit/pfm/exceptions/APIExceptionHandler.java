package ro.unicredit.pfm.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ro.unicredit.pfm.exceptions.constants.DefaultExceptionMessages.RESOURCE_NOT_FOUND;
import static ro.unicredit.pfm.exceptions.constants.DefaultExceptionMessages.UNEXPECTED_ERROR;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER_API_EXCEPTION = LoggerFactory.getLogger(APIExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        LOGGER_API_EXCEPTION.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage() == null ? RESOURCE_NOT_FOUND.getMessage()
                : ex.getMessage(), new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, WebRequest request) {
        LOGGER_API_EXCEPTION.warn(ex.getMessage());
        return handleExceptionInternal(ex, UNEXPECTED_ERROR.getMessage(), new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }
}
