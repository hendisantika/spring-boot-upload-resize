package id.my.hendisantika.springbootuploadresize.health;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

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
class ImageFolderHealthIndicatorTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldBeUpAndCreateFolderWhenMissing() {
        Path folder = tempDir.resolve("images");
        ImageFolderHealthIndicator indicator = new ImageFolderHealthIndicator(new ImageProperties(folder, 100));

        Health health = indicator.health(true);

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsKeys("path", "usableSpaceBytes");
        assertThat(folder).isDirectory();
    }

    @Test
    void shouldBeDownWhenFolderPathIsAFile() throws Exception {
        Path file = Files.writeString(tempDir.resolve("not-a-folder"), "x");
        ImageFolderHealthIndicator indicator = new ImageFolderHealthIndicator(new ImageProperties(file, 100));

        Health health = indicator.health(true);

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }
}
