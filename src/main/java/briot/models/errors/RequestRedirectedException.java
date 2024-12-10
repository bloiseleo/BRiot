package briot.models.errors;

public class RequestRedirectedException extends RiotApiError {
    public RequestRedirectedException(String message) {
        super(message);
    }
}
