package briot.models.errors;

public class InternalServerErrorException extends RiotApiError {
    public InternalServerErrorException(String message) {
        super(message);
    }
}
