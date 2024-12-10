package briot.models.errors;

public class BadRequestException extends RiotApiError {
    public BadRequestException(String message) {
        super(message);
    }
}
