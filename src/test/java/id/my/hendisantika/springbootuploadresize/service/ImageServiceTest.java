package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.TestImages;
import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class ImageServiceTest {

    @TempDir
    Path tempDir;

    private ImageService service;

    @BeforeEach
    void setUp() {
        service = new ImageService(new ImageProperties(tempDir, 100));
    }

    @Test
    void shouldResizePngAndKeepPngFormat() throws Exception {
        Path source = Files.write(tempDir.resolve("logo.png"), TestImages.png(400, 200));

        Path resized = service.resizeImage(source);

        assertThat(resized.getFileName().toString()).isEqualTo("logo_100.png");
        assertThat(formatOf(resized)).isEqualToIgnoringCase("png");
        BufferedImage image = ImageIO.read(resized.toFile());
        assertThat(image.getWidth()).isEqualTo(100);
        assertThat(image.getHeight()).isEqualTo(50);
    }

    @Test
    void shouldResizeJpgKeepingAspectRatio() throws Exception {
        Path source = Files.write(tempDir.resolve("photo.jpeg"), TestImages.jpg(300, 600));

        Path resized = service.resizeImage(source);

        assertThat(resized.getFileName().toString()).isEqualTo("photo_100.jpeg");
        assertThat(formatOf(resized)).isEqualToIgnoringCase("jpeg");
        BufferedImage image = ImageIO.read(resized.toFile());
        assertThat(image.getWidth()).isEqualTo(50);
        assertThat(image.getHeight()).isEqualTo(100);
    }

    @Test
    void shouldWriteJpgFromImageWithAlphaChannel() throws Exception {
        Path source = Files.write(tempDir.resolve("alpha.jpg"),
                TestImages.png(200, 200)); // PNG content (with alpha) saved as .jpg

        Path resized = service.resizeImage(source);

        assertThat(formatOf(resized)).isEqualToIgnoringCase("jpeg");
    }

    @Test
    void shouldFailForInvalidImage() throws Exception {
        Path source = Files.writeString(tempDir.resolve("fake.png"), "not an image", StandardCharsets.UTF_8);

        assertThatThrownBy(() -> service.resizeImage(source))
                .isInstanceOf(ImageProcessingException.class)
                .hasMessageContaining("not a valid image");
    }

    private static String formatOf(Path file) throws Exception {
        try (ImageInputStream input = ImageIO.createImageInputStream(file.toFile())) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
            assertThat(readers.hasNext()).isTrue();
            return readers.next().getFormatName();
        }
    }
}
