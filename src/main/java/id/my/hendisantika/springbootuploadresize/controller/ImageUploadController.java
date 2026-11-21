package id.my.hendisantika.springbootuploadresize.controller;

import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import id.my.hendisantika.springbootuploadresize.service.FileUploadService;
import id.my.hendisantika.springbootuploadresize.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-upload-resize
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 4/19/24
 * Time: 08:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ImageUploadController {

    private final FileUploadService fileUploadService;

    private final ImageService imageService;

    public ImageUploadController(FileUploadService fileUploadService, ImageService imageService) {
        this.fileUploadService = fileUploadService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public String uploadImage() {
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam(value = "image", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            Path original = fileUploadService.upload(imageFile);
            Path resized = imageService.resizeImage(original);
            redirectAttributes.addFlashAttribute("successMessage",
                    "File uploaded successfully and resized to " + resized.getFileName() + ".");
        } catch (ImageProcessingException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}
