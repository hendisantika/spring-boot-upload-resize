package id.my.hendisantika.springbootuploadresize.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Link : s.id/hendisantika
 * Date: 22/11/26
 * Time: 06.20
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        logger.warn("Upload rejected, file too large: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "File is too large. Please choose a smaller image.");
        return "redirect:/";
    }

    @ExceptionHandler(MultipartException.class)
    public String handleMultipart(MultipartException e, RedirectAttributes redirectAttributes) {
        logger.warn("Invalid multipart request: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid upload request. Please try again.");
        return "redirect:/";
    }
}
