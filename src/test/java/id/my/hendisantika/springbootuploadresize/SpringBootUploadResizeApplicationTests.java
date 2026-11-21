package id.my.hendisantika.springbootuploadresize;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "image.folder=${java.io.tmpdir}/spring-boot-upload-resize-test")
class SpringBootUploadResizeApplicationTests {

    @Test
    void contextLoads() {
    }

}
