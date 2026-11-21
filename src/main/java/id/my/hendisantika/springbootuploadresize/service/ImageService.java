package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 4/19/24
 * Time: 07:59
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageProperties imageProperties;

    public ImageService(ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
    }

    public boolean resizeImage(File sourceFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(sourceFile);
            int imageSize = imageProperties.size();
            BufferedImage outputImage = Scalr.resize(bufferedImage, imageSize);
            String newFileName = FilenameUtils.getBaseName(sourceFile.getName())
                    + "_" + imageSize + "."
                    + FilenameUtils.getExtension(sourceFile.getName());
            Path path = imageProperties.folder().resolve(newFileName);
            File newImageFile = path.toFile();
            ImageIO.write(outputImage, "jpg", newImageFile);
            outputImage.flush();
            logger.info("Image has been resized successfully ...");
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
