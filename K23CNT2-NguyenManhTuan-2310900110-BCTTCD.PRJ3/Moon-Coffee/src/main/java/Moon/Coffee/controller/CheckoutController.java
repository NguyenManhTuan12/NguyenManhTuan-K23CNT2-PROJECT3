package Moon.Coffee.controller;

import Moon.Coffee.dto.CartItem;
import Moon.Coffee.entity.Order;
import Moon.Coffee.entity.OrderDetail;
import Moon.Coffee.entity.Product;
import Moon.Coffee.entity.Coupon;
import Moon.Coffee.repository.OrderDetailRepository;
import Moon.Coffee.repository.OrderRepository;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.repository.CouponRepository;
import Moon.Coffee.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional; // 1. Import quan trọng cho Transactional
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    // --- 1. Hiển thị trang thanh toán ---
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        // 2. Fix Generic: Thêm <CartItem> để code tường minh, tránh cảnh báo vàng
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        double subTotal = cartService.getTotalPrice();
        Double discount = (Double) session.getAttribute("discountValue");
        if (discount == null) discount = 0.0;

        double finalPrice = Math.max(0, subTotal - discount);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("totalPrice", finalPrice);

        return "user/checkout";
    }

    // --- 2. Xử lý đặt hàng ---
    @PostMapping("/checkout")
    @Transactional(rollbackFor = Exception.class) // 3. @Transactional: Lỗi là rollback ngay, không tạo đơn rác
    public String processCheckout(@RequestParam("customerName") String name,
                                  @RequestParam("customerPhone") String phone,
                                  @RequestParam("customerAddress") String address,
                                  @RequestParam(value = "note", required = false) String note,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  HttpSession session) {

        // 4. Kiểm tra giỏ hàng rỗng: Chặn trường hợp F5 lại trang sau khi đã mua xong
        if (cartService.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        double subTotal = cartService.getTotalPrice();
        Double discountValue = (Double) session.getAttribute("discountValue");
        if (discountValue == null) discountValue = 0.0;

        double finalPrice = Math.max(0, subTotal - discountValue);

        // A. Lưu Order
        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setCustomerAddress(address);
        order.setNote(note);
        order.setPaymentMethod(paymentMethod);
        order.setTotalPrice(finalPrice);
        order.setOrderDate(new Date());
        order.setStatus("Pending");

        Order savedOrder = orderRepository.save(order);

        // B. Lưu OrderDetail (Dùng Generic List<CartItem>)
        List<CartItem> cartItems = cartService.getCartItems();
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            // Dùng item.getProductId() an toàn từ DTO
            Product product = productRepository.findById(item.getProductId()).orElse(null);

            if (product != null) {
                detail.setProduct(product);
                detail.setQuantity(item.getQuantity());
                detail.setPrice(item.getPrice()); // Lưu giá tại thời điểm mua
                orderDetailRepository.save(detail);
            }
        }

        // C. Cập nhật Coupon (Dùng Generic Optional<Coupon>)
        String couponCode = (String) session.getAttribute("appliedCouponCode");
        if (couponCode != null) {
            Optional<Coupon> couponOpt = couponRepository.findByCode(couponCode);
            if (couponOpt.isPresent()) {
                Coupon coupon = couponOpt.get();
                // Chốt chặn cuối cùng: Chỉ tăng nếu chưa vượt quá giới hạn
                if (coupon.getUsedCount() < coupon.getUsageLimit()) {
                    coupon.setUsedCount(coupon.getUsedCount() + 1);
                    couponRepository.save(coupon);
                }
            }
        }

        // D. Dọn dẹp
        cartService.clearCart();
        session.removeAttribute("discountValue");
        session.removeAttribute("appliedCouponCode");

        // E. Điều hướng
        if ("MOMO".equals(paymentMethod)) {
            return "redirect:/payment/momo?orderId=" + savedOrder.getId();
        } else if ("TRANSFER".equals(paymentMethod)) {
            return "redirect:/order-success?id=" + savedOrder.getId() + "&type=bank";
        } else {
            return "redirect:/order-success?id=" + savedOrder.getId() + "&type=cod";
        }
    }

    // --- 3. Trang thanh toán MoMo ---
    @GetMapping("/payment/momo")
    public String showMomoPayment(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return "redirect:/";

        model.addAttribute("order", order);
        return "user/momo-payment";
    }

    // --- 4. Trang thông báo thành công ---
    @GetMapping("/order-success")
    public String showSuccessPage(@RequestParam(value = "id", required = false) Long id,
                                  @RequestParam(value = "type", required = false) String type,
                                  Model model) {
        if (id != null) {
            Order order = orderRepository.findById(id).orElse(null);
            model.addAttribute("order", order);
            model.addAttribute("paymentType", type);
        }
        return "user/order-success";
    }
}