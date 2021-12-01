import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class BaseDay {

    protected String readClassPathResource(String resourceName) throws URISyntaxException, IOException {
        final URL resource = getClass().getResource(resourceName);
        Objects.requireNonNull(resource, "Expected to find a classpath resource with name " + resourceName);
        return Files.readString(Paths.get(resource.toURI()), StandardCharsets.UTF_8);
    }
}
