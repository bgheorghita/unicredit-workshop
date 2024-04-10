package ro.unicredit.trxclassifier.exceptions;

public enum DefaultExceptionMessages {
    NOT_FOUND("Requested resource not found."),
    UNEXPECTED_ERROR("Unexpected error."),
    UNEXPECTED_CLIENT_ERROR("Unexpected client error."),
    UNABLE_TO_CLASSIFY_BY_DESCRIPTION("Unable to classify be description.");

    private final String message;

    DefaultExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
