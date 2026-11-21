package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.TestImages;
import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
class FileUploadServiceTest {

    @TempDir
    Path tempDir;

    private Path uploadFolder;

    private FileUploadService service;

    @BeforeEach
    void setUp() {
        uploadFolder = tempDir.resolve("uploads");
        service = new FileUploadService(new ImageProperties(uploadFolder, 100));
    }

    @Test
    void shouldStoreValidImageWithUniqueNameAndCreateFolder() throws Exception {
        byte[] content = TestImages.png(50, 50);
        MockMultipartFile file = new MockMultipartFile("image", "cat.png", "image/png", content);

        Path first = service.upload(file);
        Path second = service.upload(file);

        assertThat(first).exists().hasParent(uploadFolder.toAbsolutePath().normalize());
        assertThat(first.getFileName().toString()).startsWith("cat_").endsWith(".png");
        assertThat(first).isNotEqualTo(second);
        assertThat(Files.readAllBytes(first)).isEqualTo(content);
    }

    @Test
    void shouldKeepPathTraversalFileNameInsideUploadFolder() {
        MockMultipartFile file = new MockMultipartFile("image", "../../evil.jpg", "image/jpeg", TestImages.jpg(20, 20));

        Path stored = service.upload(file);

        assertThat(stored.getParent()).isEqualTo(uploadFolder.toAbsolutePath().normalize());
        assertThat(stored.getFileName().toString()).startsWith("evil_");
    }

    @Test
    void shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("image", "empty.png", "image/png", new byte[0]);

        assertThatThrownBy(() -> service.upload(file))
                .isInstanceOf(ImageProcessingException.class)
                .hasMessageContaining("choose file");
    }

    @Test
    void shouldRejectNullFile() {
        assertThatThrownBy(() -> service.upload(null)).isInstanceOf(ImageProcessingException.class);
    }

    @Test
    void shouldRejectUnsupportedExtension() {
        MockMultipartFile file = new MockMultipartFile("image", "script.exe", "application/octet-stream",
                TestImages.png(10, 10));

        assertThatThrownBy(() -> service.upload(file))
                .isInstanceOf(ImageProcessingException.class)
                .hasMessageContaining("Unsupported file type");
    }

    @Test
    void shouldRejectFileThatIsNotARealImage() {
        MockMultipartFile file = new MockMultipartFile("image", "fake.png", "image/png",
                "not an image".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> service.upload(file))
                .isInstanceOf(ImageProcessingException.class)
                .hasMessageContaining("not a valid image");
        assertThat(uploadFolder).doesNotExist();
    }
}
