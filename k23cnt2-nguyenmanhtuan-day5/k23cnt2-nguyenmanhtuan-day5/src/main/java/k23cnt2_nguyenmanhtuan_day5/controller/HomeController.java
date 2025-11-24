package k23cnt2_nguyenmanhtuan_day5.controller;

import k23cnt2_nguyenmanhtuan_day5.entity.Info;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    // 🏠 Trang chủ
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("title", "K23CNT2 - Trang chủ");
        return "home";
    }

    // ℹ️ Trang giới thiệu
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "K23CNT2 - Giới thiệu");
        return "about";
    }

    // 📞 Trang liên hệ
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "K23CNT2 - Liên hệ");
        return "contact";
    }

    // 👤 Trang profile
    @GetMapping("/profile")
    public String profile(Model model) {
        List<Info> profile = new ArrayList<>();
        profile.add(new Info(
                "K23CNT2 - Nguyen Manh Tuan",
                "NMT",
                "nguyenmanhtuan@devmaster.edu.vn",
                "https://devmaster.edu.vn"
        ));

        model.addAttribute("DevmasterProfile", profile);
        model.addAttribute("title", "K23CNT2 - Profile");
        return "profile";
    }
}
