package Moon.Coffee.controller;

import Moon.Coffee.entity.Product;
import Moon.Coffee.entity.Review;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/add")
    public String submitReview(@RequestParam("productId") Long productId,
                               @RequestParam("reviewerName") String reviewerName,
                               @RequestParam("rating") int rating,
                               @RequestParam("comment") String comment,
                               RedirectAttributes ra) {

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            ra.addFlashAttribute("errorMsg", "Sản phẩm không tồn tại!");
            return "redirect:/home";
        }

        Review review = new Review();
        review.setProduct(product);
        review.setReviewerName(reviewerName);
        review.setRating(rating);
        review.setComment(comment);
        // Sửa setDate thành setCreatedAt cho đúng Entity
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        ra.addFlashAttribute("successMsg", "Cảm ơn bạn đã đánh giá món " + product.getName() + "!");

        // Điều hướng về trang chi tiết sản phẩm
        return "redirect:/product/" + productId;
    }
}