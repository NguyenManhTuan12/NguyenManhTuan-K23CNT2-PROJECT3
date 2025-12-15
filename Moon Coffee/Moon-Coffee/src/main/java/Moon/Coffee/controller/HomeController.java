package Moon.Coffee.controller;

import Moon.Coffee.entity.Product;
import Moon.Coffee.entity.Category;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // <-- Nhớ Import cái này

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/") // Khi khách vào trang chủ
    public String showHomePage(
            @RequestParam(required = false) String keyword,  // Nhận từ khóa tìm kiếm (có thể null)
            @RequestParam(required = false) Long categoryId, // Nhận ID danh mục (có thể null)
            Model model
    ) {
        // 1. Luôn lấy danh sách danh mục để hiển thị thanh menu (Cho khách chọn)
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        // 2. Xử lý logic tìm kiếm & lọc sản phẩm
        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            // Trường hợp A: Khách đang tìm kiếm theo tên
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else if (categoryId != null) {
            // Trường hợp B: Khách đang bấm vào một danh mục cụ thể
            products = productRepository.findByCategoryId(categoryId);
        } else {
            // Trường hợp C: Mới vào trang chủ, chưa chọn gì -> Hiển thị tất cả
            products = productRepository.findAll();
        }

        // 3. Gửi danh sách sản phẩm đã lọc sang giao diện
        model.addAttribute("products", products);

        // Gửi lại categoryId hiện tại để giao diện biết cái nào đang được chọn (để tô màu đậm lên)
        model.addAttribute("currentCategoryId", categoryId);

        return "home"; // Trả về file home.html
    }
}