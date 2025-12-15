package Moon.Coffee.controller;

import Moon.Coffee.entity.CartItem;
import Moon.Coffee.entity.Order;
import Moon.Coffee.entity.OrderDetail;
import Moon.Coffee.entity.Product;
import Moon.Coffee.repository.OrderRepository;
import Moon.Coffee.repository.ProductRepository;
import Moon.Coffee.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    // 1. Mở trang Checkout (Điền thông tin)
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {
        // Nếu giỏ hàng trống thì đá về trang giỏ hàng
        if (cartService.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("totalPrice", cartService.getTotalPrice());

        // Trả về file: templates/user/checkout.html
        return "user/checkout";
    }

    // 2. Xử lý nút "Xác nhận thanh toán"
    @PostMapping("/checkout")
    public String placeOrder(
            @RequestParam("customerName") String name,
            @RequestParam("customerPhone") String phone,
            @RequestParam("customerAddress") String address
    ) {
        // A. Tạo đơn hàng tổng
        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setCustomerAddress(address);
        order.setTotalPrice(cartService.getTotalPrice());
        order.setStatus("Chờ xác nhận");

        // B. Chuyển đổi từng món trong giỏ thành OrderDetail để lưu
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartService.getItems()) {
            Product product = productRepository.findById(item.getId()).orElse(null);
            if (product != null) {
                OrderDetail detail = new OrderDetail(order, product, item.getQuantity(), item.getPrice());
                details.add(detail);
            }
        }
        order.setOrderDetails(details);

        // C. Lưu vào Database (Lúc này order sẽ được cấp ID tự động)
        orderRepository.save(order);

        // D. Xóa sạch giỏ hàng sau khi mua xong
        cartService.clearCart();

        // E. Chuyển hướng đến trang thành công KÈM THEO MÃ ĐƠN HÀNG
        // Ví dụ: /order-success?orderId=5
        return "redirect:/order-success?orderId=" + order.getId();
    }

    // 3. Trang thông báo thành công (Hiển thị QR Code)
    @GetMapping("/order-success")
    public String showSuccessPage(@RequestParam(name = "orderId", required = false) Long orderId, Model model) {

        // Nếu có mã đơn hàng, lấy thông tin từ Database gửi sang view
        if (orderId != null) {
            Order order = orderRepository.findById(orderId).orElse(null);
            model.addAttribute("order", order);
        }

        // Trả về file: templates/user/order-success.html
        return "user/order-success";
    }
}