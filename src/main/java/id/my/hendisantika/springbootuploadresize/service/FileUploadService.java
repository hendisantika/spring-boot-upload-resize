package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 4/19/24
 * Time: 08:00
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FileUploadService {

    static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp");

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    private final ImageProperties imageProperties;

    public FileUploadService(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    /**
     * Validates and stores the uploaded image in the configured folder using a unique, sanitized file name.
     *
     * @param imageFile uploaded file
     * @return path of the stored file
     * @throws ImageProcessingException when the file is empty, not a supported image or cannot be stored
     */
    public Path upload(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new ImageProcessingException("Please choose file to upload.");
        }

        String originalName = FilenameUtils.getName(StringUtils.cleanPath(
                imageFile.getOriginalFilename() == null ? "" : imageFile.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(originalName).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ImageProcessingException("Unsupported file type. Allowed types: " + ALLOWED_EXTENSIONS + ".");
        }

        try {
            byte[] content = imageFile.getBytes();
            if (ImageIO.read(new ByteArrayInputStream(content)) == null) {
                throw new ImageProcessingException("The uploaded file is not a valid image.");
            }

            Path folder = imageProperties.folder().toAbsolutePath().normalize();
            Files.createDirectories(folder);

            String baseName = FilenameUtils.getBaseName(originalName).replaceAll("[^A-Za-z0-9._-]", "_");
            String storedName = baseName + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
            Path target = folder.resolve(storedName).normalize();
            if (!target.startsWith(folder)) {
                throw new ImageProcessingException("Invalid file name.");
            }

            Files.write(target, content);
            logger.info("Image has been uploaded successfully to {}", target);
            return target;
        } catch (IOException e) {
            logger.error("Failed to store uploaded image", e);
            throw new ImageProcessingException("Upload failed.", e);
        }
    }
}
