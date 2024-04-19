package id.my.hendisantika.springbootuploadresize.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${image.folder}")
    private String imageFolder;

    private final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
}
