package id.my.hendisantika.springbootuploadresize.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 22/11/26
 * Time: 06.20
 */
@ConfigurationProperties(prefix = "image")
public record ImageProperties(Path folder, Integer size) {

    private static final int DEFAULT_SIZE = 400;

    public ImageProperties {
        if (folder == null) {
            folder = Path.of(System.getProperty("java.io.tmpdir"), "spring-boot-upload-resize");
        }
        if (size == null) {
            size = DEFAULT_SIZE;
        }
        if (size <= 0) {
            throw new IllegalArgumentException("image.size must be greater than 0 but was " + size);
        }
    }
}
