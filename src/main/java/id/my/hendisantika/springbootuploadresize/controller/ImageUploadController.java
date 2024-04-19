package id.my.hendisantika.springbootuploadresize.controller;

import id.my.hendisantika.springbootuploadresize.service.FileUploadService;
import id.my.hendisantika.springbootuploadresize.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
