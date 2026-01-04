package Moon.Coffee.controller;

import Moon.Coffee.entity.Coupon;
import Moon.Coffee.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {

    @Autowired
    private CouponRepository couponRepository;

    @GetMapping
    public String list(Model model) {
        List<Coupon> coupons = couponRepository.findAll();
        model.addAttribute("coupons", coupons);
        if (!model.containsAttribute("newCoupon")) {
            model.addAttribute("newCoupon", new Coupon());
        }
        return "admin/coupon-management";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("newCoupon") Coupon coupon, RedirectAttributes ra) {
        try {
            // Chuẩn hóa mã viết hoa toàn bộ
            coupon.setCode(coupon.getCode().toUpperCase().trim());
            couponRepository.save(coupon);
            ra.addFlashAttribute("successMsg", "Đã lưu mã giảm giá thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: Mã bị trùng hoặc dữ liệu không hợp lệ!");
        }
        return "redirect:/admin/coupons";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        couponRepository.deleteById(id);
        ra.addFlashAttribute("successMsg", "Đã xóa mã giảm giá!");
        return "redirect:/admin/coupons";
    }
}