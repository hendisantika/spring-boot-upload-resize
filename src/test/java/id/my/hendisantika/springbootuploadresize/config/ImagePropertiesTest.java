package id.my.hendisantika.springbootuploadresize.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 22/11/26
 * Time: 06.35
 */
class ImagePropertiesTest {

    @Test
    void shouldApplyDefaultsWhenValuesAreMissing() {
        ImageProperties properties = new ImageProperties(null, null);

        assertThat(properties.size()).isEqualTo(400);
        assertThat(properties.folder()).isNotNull();
    }

    @Test
    void shouldKeepConfiguredValues() {
        ImageProperties properties = new ImageProperties(Path.of("/data/images"), 250);

        assertThat(properties.folder()).isEqualTo(Path.of("/data/images"));
        assertThat(properties.size()).isEqualTo(250);
    }

    @Test
    void shouldRejectNonPositiveSize() {
        assertThatIllegalArgumentException().isThrownBy(() -> new ImageProperties(null, 0));
    }
}
