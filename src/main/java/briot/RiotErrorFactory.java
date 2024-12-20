package briot;

import briot.models.errors.*;

import java.net.http.HttpResponse;

public class RiotErrorFactory {
    public RiotErrorFactory() {}
    private BadRequestException createBadRequestException(String message, int statusCode) {
        if (statusCode == 404) {
            return new NotFoundException(message);
        }
        return new BadRequestException(message);
    }
    private InternalServerErrorException createInternalServerErrorException(String message) {
        return new InternalServerErrorException(message);
    }
    private RequestRedirectedException createRequestRedirectedException(String message) {
        return new RequestRedirectedException(message);
    }
    public RiotApiError create(HttpResponse<String> response) {
        if (response.statusCode() >= 300 && response.statusCode() < 400) {
            return this.createRequestRedirectedException(response.body());
        }
        if (response.statusCode() >= 400 && response.statusCode() < 500) {
            return this.createBadRequestException(response.body(), response.statusCode());
        }
        return this.createInternalServerErrorException(response.body());
    }
}
