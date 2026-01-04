package Moon.Coffee.controller;

import Moon.Coffee.entity.Order;
import Moon.Coffee.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // 1. Hiển thị danh sách đơn hàng
    @GetMapping("/admin/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("orders", orders);
        return "admin/order-list"; // Đảm bảo bạn sẽ tạo file html này sau
    }

    // 2. Xem chi tiết đơn hàng
    @GetMapping("/admin/orders/detail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        model.addAttribute("order", order);
        return "admin/order-detail"; // Đảm bảo bạn sẽ tạo file html này sau
    }

    // 3. Cập nhật trạng thái
    @PostMapping("/admin/orders/update-status")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("status") String status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders";
    }


}