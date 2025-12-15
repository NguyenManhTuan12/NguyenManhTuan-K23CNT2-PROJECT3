package Moon.Coffee.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    // Đường dẫn đến thư mục chứa ảnh trong project của bạn
    // LƯU Ý: System.getProperty("user.dir") lấy đường dẫn gốc của dự án
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/images/products/";

    public String saveImage(MultipartFile file) throws IOException {
        // 1. Kiểm tra nếu không có file được chọn
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 2. Tạo tên file mới độc nhất (để tránh bị trùng tên file cũ)
        // Ví dụ: cafe.jpg -> sẽ thành một chuỗi dài ngoằng_cafe.jpg
        String originalFileName = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // 3. Tạo đường dẫn lưu file
        Path uploadPath = Paths.get(UPLOAD_DIR);

        // Nếu thư mục chưa có thì tạo mới
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 4. Lưu file từ bộ nhớ vào ổ cứng
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // 5. Trả về tên file mới để lưu vào Database
        return newFileName;
    }
}