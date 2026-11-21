package id.my.hendisantika.springbootuploadresize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

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
public final class TestImages {

    private TestImages() {
    }

    public static byte[] image(int width, int height, int type, String format) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(new BufferedImage(width, height, type), format, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] png(int width, int height) {
        return image(width, height, BufferedImage.TYPE_INT_ARGB, "png");
    }

    public static byte[] jpg(int width, int height) {
        return image(width, height, BufferedImage.TYPE_INT_RGB, "jpg");
    }
}
