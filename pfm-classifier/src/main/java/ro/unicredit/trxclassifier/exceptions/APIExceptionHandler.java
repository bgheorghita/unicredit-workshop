package ro.unicredit.trxclassifier.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER_API_EXCEPTION = LoggerFactory.getLogger(APIExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        LOGGER_API_EXCEPTION.warn(ex.getMessage());
        return handleExceptionInternal(
                ex, ex.getMessage() == null
                        ? DefaultExceptionMessages.NOT_FOUND
                        : ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request
        );
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, WebRequest request) {
        LOGGER_API_EXCEPTION.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                DefaultExceptionMessages.UNEXPECTED_ERROR,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request
        );
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException  ex, WebRequest request) {
        LOGGER_API_EXCEPTION.warn(ex.getMessage());
        return handleExceptionInternal(
                ex, ex.getMessage() == null
                        ? DefaultExceptionMessages.UNEXPECTED_CLIENT_ERROR
                        : ex.getMessage(), new HttpHeaders(), ex.getStatusCode(), request
        );
    }
}
