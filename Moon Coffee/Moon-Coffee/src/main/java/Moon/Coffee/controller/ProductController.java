package Moon.Coffee.controller;

import Moon.Coffee.entity.Category;
import Moon.Coffee.entity.Product;
import Moon.Coffee.repository.CategoryRepository;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageService imageService;

    // Chuyển hướng /admin -> /admin/add-product
    @GetMapping("/admin")
    public String redirectToAdminPage() {
        return "redirect:/admin/add-product";
    }

    // Hiển thị trang quản lý
    @GetMapping("/admin/add-product")
    public String showAddProductPage(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "admin/add-product";
    }

    // API Lưu sản phẩm (Có upload ảnh)
    @PostMapping("/api/products/save")
    @ResponseBody
    public ResponseEntity<String> saveProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);

            Category category = categoryRepository.findById(categoryId).orElse(null);
            product.setCategory(category);

            if (imageFile != null && !imageFile.isEmpty()) {
                String newImageName = imageService.saveImage(imageFile);
                product.setImageName(newImageName);
            }

            productRepository.save(product);
            return ResponseEntity.ok("Đã lưu thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    // --- 👇 ĐOẠN CODE MỚI THÊM: API XÓA SẢN PHẨM 👇 ---
    @DeleteMapping("/api/products/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        // Kiểm tra xem sản phẩm có tồn tại không trước khi xóa
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Đã xóa thành công!");
        }
        return ResponseEntity.notFound().build(); // Trả về lỗi nếu không tìm thấy ID
    }
}