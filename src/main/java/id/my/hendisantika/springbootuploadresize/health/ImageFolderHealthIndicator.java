package id.my.hendisantika.springbootuploadresize.health;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import org.springframework.boot.health.contributor.AbstractHealthIndicator;
import org.springframework.boot.health.contributor.Health;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 22/11/26
 * Time: 06.40
 */
@Component("imageFolder")
public class ImageFolderHealthIndicator extends AbstractHealthIndicator {

    private final ImageProperties imageProperties;

    public ImageFolderHealthIndicator(ImageProperties imageProperties) {
        super("Image folder health check failed");
        this.imageProperties = imageProperties;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Path folder = imageProperties.folder().toAbsolutePath().normalize();
        Files.createDirectories(folder);

        builder.withDetail("path", folder.toString());
        if (!Files.isDirectory(folder) || !Files.isWritable(folder)) {
            builder.down().withDetail("error", "Image folder is not a writable directory");
            return;
        }
        builder.up().withDetail("usableSpaceBytes", Files.getFileStore(folder).getUsableSpace());
    }
}
