package ro.unicredit.trxclassifier.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.unicredit.trxclassifier.exceptions.NotFoundException;

import java.util.logging.Logger;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
    private final static String DEFAULT_NOT_FOUND_MSG = "Requested resource not found.";
    private final static String DEFAULT_UNEXPECTED_ERROR_MSG = "Unexpected error.";
    private final static String DEFAULT_UNEXPECTED_CLIENT_ERROR_MSG = "Unexpected client error.";

    // TODO: loggers

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(
                ex, ex.getMessage() == null
                        ? DEFAULT_NOT_FOUND_MSG
                        : ex.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request
        );
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleUnexpectedException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(
                ex,
                DEFAULT_UNEXPECTED_ERROR_MSG,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request
        );
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException  ex, WebRequest request) {
        ex.printStackTrace();
        return handleExceptionInternal(
                ex, ex.getMessage() == null
                        ? DEFAULT_UNEXPECTED_CLIENT_ERROR_MSG
                        : ex.getMessage(), new HttpHeaders(), ex.getStatusCode(), request
        );
    }
}
