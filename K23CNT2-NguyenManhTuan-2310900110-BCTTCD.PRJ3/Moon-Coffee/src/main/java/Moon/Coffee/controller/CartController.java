package Moon.Coffee.controller;

import Moon.Coffee.entity.Coupon;
import Moon.Coffee.repository.CouponRepository;
import Moon.Coffee.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CouponRepository couponRepository;

    // 1. Hiển thị giỏ hàng
    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        model.addAttribute("cartItems", cartService.getCartItems());

        double subTotal = cartService.getTotalPrice();

        // Lấy giá trị giảm giá từ Session
        Double discount = (Double) session.getAttribute("discountValue");
        if (discount == null) discount = 0.0;

        // ĐẢM BẢO: Nếu tổng tiền hàng nhỏ hơn mức giảm giá (do xóa bớt đồ), chỉ giảm tối đa bằng subTotal
        double finalPrice = Math.max(0, subTotal - discount);

        model.addAttribute("subtotal", subTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("finalPrice", finalPrice);
        return "user/cart";
    }

    // 2. Thêm sản phẩm
    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Long id, RedirectAttributes ra) {
        cartService.addToCart(id);
        ra.addFlashAttribute("successMsg", "Đã thêm vào giỏ hàng!");
        return "redirect:/cart";
    }

    // 3. Áp dụng mã giảm giá
    @PostMapping("/cart/apply-coupon")
    public String applyCoupon(@RequestParam("couponCode") String code, HttpSession session, RedirectAttributes ra) {
        Optional<Coupon> couponOpt = couponRepository.findByCode(code.toUpperCase().trim());

        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            // Kiểm tra: Trạng thái Active, Ngày hết hạn, và Lượt dùng tối đa
            if (Boolean.TRUE.equals(coupon.getActive()) &&
                    (coupon.getExpiryDate() == null || coupon.getExpiryDate().after(new Date())) &&
                    coupon.getUsedCount() < coupon.getUsageLimit()) {

                session.setAttribute("discountValue", (double) coupon.getDiscountValue());
                session.setAttribute("appliedCouponCode", coupon.getCode());
                ra.addFlashAttribute("successMsg", "Áp dụng mã '" + coupon.getCode() + "' thành công!");
            } else {
                ra.addFlashAttribute("errorMsg", "Mã đã hết hạn hoặc đã dùng hết lượt!");
                session.removeAttribute("discountValue");
                session.removeAttribute("appliedCouponCode");
            }
        } else {
            ra.addFlashAttribute("errorMsg", "Mã '" + code + "' không tồn tại!");
            session.removeAttribute("discountValue");
            session.removeAttribute("appliedCouponCode");
        }
        return "redirect:/cart";
    }

    // 4. Hủy mã giảm giá (MỚI - để fix nút Hủy mã)
    @GetMapping("/cart/remove-coupon")
    public String removeCoupon(HttpSession session, RedirectAttributes ra) {
        session.removeAttribute("discountValue");
        session.removeAttribute("appliedCouponCode");
        ra.addFlashAttribute("successMsg", "Đã hủy mã giảm giá.");
        return "redirect:/cart";
    }

    // 5. Cập nhật số lượng
    @GetMapping("/cart/update")
    public String updateQuantity(@RequestParam("id") Long id, @RequestParam("quantity") int quantity) {
        cartService.updateQuantity(id, quantity);
        return "redirect:/cart";
    }

    // 6. Xóa sản phẩm
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(id);

        // Nếu xóa hết hàng thì xóa sạch mã giảm giá trong session cho sạch
        if (cartService.getCartItems().isEmpty()) {
            session.removeAttribute("discountValue");
            session.removeAttribute("appliedCouponCode");
        }
        return "redirect:/cart";
    }
}