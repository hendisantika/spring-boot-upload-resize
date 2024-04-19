package id.my.hendisantika.springbootuploadresize.controller;

import id.my.hendisantika.springbootuploadresize.service.FileUploadService;
import id.my.hendisantika.springbootuploadresize.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;

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

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ImageService imageService;

    @GetMapping("")
    public String uploadImage() {
        return "uploadImage";
    }

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("image") MultipartFile imageFile, RedirectAttributes redirectAttributes) {
        if (imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please choose file to upload.");
            return "redirect:/";
        }

        File file = fileUploadService.upload(imageFile);
        if (file == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload failed.");
            return "redirect:/";

        }
        boolean resizeResult = imageService.resizeImage(file);
        if (!resizeResult) {
            redirectAttributes.addFlashAttribute("errorMessage", "Resize failed.");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("successMessage", "File upload successfully.");
        return "redirect:/";
    }
}
