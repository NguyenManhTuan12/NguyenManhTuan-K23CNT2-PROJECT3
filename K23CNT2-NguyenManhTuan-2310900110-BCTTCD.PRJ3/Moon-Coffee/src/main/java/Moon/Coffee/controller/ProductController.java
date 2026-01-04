package Moon.Coffee.controller;

import Moon.Coffee.entity.Product;
import Moon.Coffee.entity.Review;
import Moon.Coffee.repository.CategoryRepository;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.repository.ReviewRepository;
import Moon.Coffee.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ImageService imageService;

    // ==========================================
    // 🟢 PHẦN 1: QUẢN TRỊ VIÊN (ADMIN)
    // ==========================================

    @GetMapping("/admin")
    public String redirectToAdminPage() {
        return "redirect:/admin/add-product";
    }

    @GetMapping("/admin/add-product")
    public String showAddProductPage(Model model) {
        // Lấy danh sách sản phẩm và danh mục để quản lý
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/add-product";
    }

    @GetMapping("/admin/product/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/admin/add-product";

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/admin/products/save")
    public String saveEditedProduct(
            @ModelAttribute("product") Product productForm,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes ra
    ) {
        try {
            if (productForm.getId() != null) {
                Product existingProduct = productRepository.findById(productForm.getId()).orElse(null);
                if (existingProduct != null) {
                    existingProduct.setName(productForm.getName());
                    existingProduct.setPrice(productForm.getPrice());
                    existingProduct.setCategory(productForm.getCategory());

                    // Xử lý lưu ảnh mới nếu có tải lên
                    if (imageFile != null && !imageFile.isEmpty()) {
                        String newImageName = imageService.saveImage(imageFile);
                        existingProduct.setImageName(newImageName);
                    }
                    productRepository.save(existingProduct);
                    ra.addFlashAttribute("successMsg", "Cập nhật sản phẩm thành công!");
                }
            }
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/add-product";
    }

    @DeleteMapping("/api/products/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return ResponseEntity.ok("Đã xóa thành công!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm.");
        } catch (Exception e) {
            // Ngăn xóa nếu sản phẩm đã được khách hàng đánh giá hoặc mua
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Lỗi: Sản phẩm đã có đánh giá hoặc đơn hàng liên quan!");
        }
    }

    // ==========================================
    // 🔵 PHẦN 2: KHÁCH HÀNG & ĐÁNH GIÁ (REVIEW)
    // ==========================================

    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/";

        // Lấy danh sách đánh giá mới nhất (CreatedAt)
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(id);

        // Tính toán chỉ số đánh giá trung bình
        Double avgRating = reviewRepository.getAverageRatingByProductId(id);
        Long totalReviews = reviewRepository.countByProductId(id);

        // Truyền dữ liệu ra Thymeleaf (Đảm bảo giá trị mặc định cho số sao)
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating != null ? avgRating : 0.0);
        model.addAttribute("totalReviews", totalReviews != null ? totalReviews : 0);

        return "user/product-detail";
    }

    @PostMapping("/product/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @RequestParam("reviewerName") String name,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               RedirectAttributes ra) {

        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            Review review = new Review();
            review.setReviewerName(name);
            review.setRating(rating);
            review.setComment(comment);
            review.setProduct(product);

            // Đồng bộ thời gian tạo đánh giá với hệ thống
            review.setCreatedAt(LocalDateTime.now());

            reviewRepository.save(review);
            ra.addFlashAttribute("successMsg", "Cảm ơn bạn đã đánh giá món " + product.getName() + "!");
        }
        // Quay lại đúng trang sản phẩm vừa đánh giá
        return "redirect:/product/" + id;
    }
}