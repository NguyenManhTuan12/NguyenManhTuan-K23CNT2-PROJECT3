package Moon.Coffee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối đến thư mục chứa ảnh trong máy tính của bạn
        Path uploadDir = Paths.get("./src/main/resources/static/images/products/");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // Cấu hình: Khi ai đó gọi link /images/products/** // -> Thì chọc thẳng vào thư mục trên ổ cứng để lấy ảnh
        registry.addResourceHandler("/images/products/**")
                .addResourceLocations("file:/" + uploadPath + "/");
    }
}