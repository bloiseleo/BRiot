package briot.models.errors;

public abstract class RiotApiError extends RuntimeException{
    public RiotApiError(String message) {
        super(message);
    }
}
