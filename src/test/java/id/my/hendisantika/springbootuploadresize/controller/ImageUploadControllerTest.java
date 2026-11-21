package id.my.hendisantika.springbootuploadresize.controller;

import id.my.hendisantika.springbootuploadresize.exception.ImageProcessingException;
import id.my.hendisantika.springbootuploadresize.service.FileUploadService;
import id.my.hendisantika.springbootuploadresize.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
@WebMvcTest(ImageUploadController.class)
class ImageUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileUploadService fileUploadService;

    @MockitoBean
    private ImageService imageService;

    private final MockMultipartFile image =
            new MockMultipartFile("image", "cat.png", "image/png", new byte[]{1, 2, 3});

    @Test
    void shouldShowUploadPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("uploadImage"));
    }

    @Test
    void shouldUploadAndResizeImage() throws Exception {
        Path original = Path.of("/images/cat_1234.png");
        given(fileUploadService.upload(any())).willReturn(original);
        given(imageService.resizeImage(original)).willReturn(Path.of("/images/cat_1234_400.png"));

        mockMvc.perform(multipart("/uploadImage").file(image))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("successMessage",
                        "File uploaded successfully and resized to cat_1234_400.png."));
    }

    @Test
    void shouldShowErrorWhenUploadFails() throws Exception {
        given(fileUploadService.upload(any()))
                .willThrow(new ImageProcessingException("The uploaded file is not a valid image."));

        mockMvc.perform(multipart("/uploadImage").file(image))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("errorMessage", "The uploaded file is not a valid image."));
        verify(imageService, never()).resizeImage(any());
    }

    @Test
    void shouldShowErrorWhenResizeFails() throws Exception {
        Path original = Path.of("/images/cat_1234.png");
        given(fileUploadService.upload(any())).willReturn(original);
        given(imageService.resizeImage(original)).willThrow(new ImageProcessingException("Resize failed."));

        mockMvc.perform(multipart("/uploadImage").file(image))
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("errorMessage", "Resize failed."));
    }
}
