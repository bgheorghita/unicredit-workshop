package ro.unicredit.pfm.exceptions;

public enum DefaultExceptionMessages {
    NOT_FOUND("Requested resource not found."),
    UNEXPECTED_ERROR("Unexpected error.");

    private final String message;

    DefaultExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
