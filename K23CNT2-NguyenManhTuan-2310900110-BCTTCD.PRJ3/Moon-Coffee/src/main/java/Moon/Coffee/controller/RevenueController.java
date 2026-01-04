package Moon.Coffee.controller;

import Moon.Coffee.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RevenueController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/revenue") // Đường dẫn để xem báo cáo
    public String showRevenuePage(Model model) {
        // 1. Lấy tổng doanh thu
        Double totalMoney = orderRepository.sumTotalRevenue();

        // 2. Lấy tổng số đơn
        Long totalOrders = orderRepository.countTotalOrders();

        // Xử lý nếu chưa có doanh thu (tránh lỗi null)
        if (totalMoney == null) totalMoney = 0.0;

        // 3. Gửi dữ liệu sang HTML
        model.addAttribute("revenue", totalMoney);
        model.addAttribute("ordersCount", totalOrders);

        return "admin/revenue"; // Tên file HTML giao diện
    }
}