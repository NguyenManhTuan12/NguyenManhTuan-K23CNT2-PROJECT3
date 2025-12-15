package Moon.Coffee.controller;

import Moon.Coffee.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Hiển thị trang giỏ hàng
    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());

        // --- SỬA Ở ĐÂY: Trỏ vào thư mục user ---
        // Vì file html của bạn nằm tại: src/main/resources/templates/user/cart.html
        return "user/cart";
    }

    // 2. Xử lý thêm vào giỏ
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id) {
        cartService.addToCart(id);
        return "redirect:/cart"; // Redirect giữ nguyên vì nó trỏ đến URL /cart ở trên
    }

    // 3. Xóa món
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

    // 4. Cập nhật số lượng (Tăng/Giảm)
    @GetMapping("/cart/update")
    public String updateQuantity(@RequestParam Long id, @RequestParam int quantity) {
        cartService.updateQuantity(id, quantity);
        return "redirect:/cart";
    }
}