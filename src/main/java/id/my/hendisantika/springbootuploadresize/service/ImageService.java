package id.my.hendisantika.springbootuploadresize.service;

import id.my.hendisantika.springbootuploadresize.config.ImageProperties;
import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

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

    /**
     * Resizes the given image to the configured size, keeping its aspect ratio and original format.
     *
     * @param sourceFile image to resize
     * @return path of the resized image, stored next to the source as {@code <name>_<size>.<ext>}
     * @throws ImageProcessingException when the image cannot be read or written
     */
    public Path resizeImage(Path sourceFile) {
        BufferedImage source = null;
        BufferedImage output = null;
        try {
            source = ImageIO.read(sourceFile.toFile());
            if (source == null) {
                throw new ImageProcessingException("The uploaded file is not a valid image.");
            }

            int imageSize = imageProperties.size();
            String fileName = sourceFile.getFileName().toString();
            String format = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ROOT);
            if ("jpeg".equals(format)) {
                format = "jpg";
            }

            output = Scalr.resize(source, Scalr.Method.QUALITY, imageSize);
            if ("jpg".equals(format) || "bmp".equals(format)) {
                // JPEG & BMP writers do not support an alpha channel
                output = toRgb(output);
            }

            Path target = sourceFile.resolveSibling(
                    FilenameUtils.getBaseName(fileName) + "_" + imageSize + "." + FilenameUtils.getExtension(fileName));
            if (!ImageIO.write(output, format, target.toFile())) {
                throw new ImageProcessingException("No image writer available for format: " + format);
            }
            logger.info("Image has been resized successfully to {}", target);
            return target;
        } catch (IOException e) {
            logger.error("Failed to resize image {}", sourceFile, e);
            throw new ImageProcessingException("Resize failed.", e);
        } finally {
            if (source != null) {
                source.flush();
            }
            if (output != null) {
                output.flush();
            }
        }
    }

    private static BufferedImage toRgb(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_RGB) {
            return image;
        }
        BufferedImage rgb = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgb.createGraphics();
        try {
            graphics.drawImage(image, 0, 0, Color.WHITE, null);
        } finally {
            graphics.dispose();
        }
        return rgb;
    }
}
