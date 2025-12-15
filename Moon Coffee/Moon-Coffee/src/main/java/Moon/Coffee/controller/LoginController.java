package Moon.Coffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        // SỬA TỪ "login" THÀNH "user/login"
        // Để khớp với vị trí file: templates/user/login.html
        return "user/login";
    }
}