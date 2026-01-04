package Moon.Coffee.controller;

import Moon.Coffee.entity.Category;
import Moon.Coffee.entity.Product;
import Moon.Coffee.repository.CategoryRepository;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.service.CartService; // <--- 1. Import CartService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // <--- 2. Import để sắp xếp
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartService cartService; // <--- 3. Tiêm CartService vào đây

    @GetMapping("/") // Trang chủ
    public String showHomePage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            Model model
    ) {
        // A. Lấy danh sách danh mục (Menu)
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        // B. Xử lý tìm kiếm & lọc sản phẩm
        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            // Tìm theo tên
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else if (categoryId != null) {
            // Lọc theo danh mục
            products = productRepository.findByCategoryId(categoryId);
        } else {
            // Mặc định: Hiển thị tất cả, sắp xếp MỚI NHẤT lên đầu (ID giảm dần)
            products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }

        model.addAttribute("products", products);
        model.addAttribute("currentCategoryId", categoryId);

        // C. QUAN TRỌNG: Lấy số lượng giỏ hàng để hiện lên Icon (Badge)
        model.addAttribute("cartCount", cartService.getCount());

        return "user/home";
    }
}