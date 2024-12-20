package briot.helpers;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.Optional;

public abstract class FileUtils {
    private static String getExtension(String contentType) {
        if (!contentType.equals("application/x-tar")) {
            return null;
        }
        return ".tgz";
    }
    public static File createTempFileFromResponse(HttpResponse response, String name) {
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        try {
            return File.createTempFile(name, getExtension(contentTypeOptional.orElse("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
