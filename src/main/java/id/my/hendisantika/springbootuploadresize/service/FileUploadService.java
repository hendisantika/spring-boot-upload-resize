package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    private final ImageProperties imageProperties;

    public FileUploadService(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    public File upload(MultipartFile imageFile) {
        try {
            Path path = imageProperties.folder().resolve(imageFile.getOriginalFilename());
            Files.write(path, imageFile.getBytes());
            logger.info("Image has been uploaded successfully ...");
            return path.toFile();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
