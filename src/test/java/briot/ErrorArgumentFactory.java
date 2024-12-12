package briot;

import briot.models.errors.BadRequestException;
import briot.models.errors.InternalServerErrorException;
import briot.models.errors.NotFoundException;
import briot.models.errors.RequestRedirectedException;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class ErrorArgumentFactory {
    public static Stream<Arguments> errorFactory() {
        return Stream.of(
                Arguments.of(BadRequestException.class),
                Arguments.of(NotFoundException.class),
                Arguments.of(RequestRedirectedException.class),
                Arguments.of(InternalServerErrorException.class)
        );
    }
}
