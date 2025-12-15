package Moon.Coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF để các form hoạt động dễ dàng

                .authorizeHttpRequests((requests) -> requests
                        // 👇👇👇 CẬP NHẬT QUAN TRỌNG Ở ĐÂY 👇👇👇
                        // Cho phép khách truy cập: Trang chủ, Giỏ hàng, Thanh toán, và Tài nguyên tĩnh
                        .requestMatchers("/", "/home", "/login", "/cart/**", "/checkout", "/order-success", "/css/**", "/images/**", "/js/**").permitAll()

                        // Các trang quản lý (Admin) và API lưu dữ liệu thì BẮT BUỘC đăng nhập
                        .requestMatchers("/admin/**", "/api/**").authenticated()

                        .anyRequest().permitAll()
                )

                .formLogin((form) -> form
                        .loginPage("/login") // Trang login tùy chỉnh
                        .loginProcessingUrl("/login") // Link xử lý đăng nhập
                        .defaultSuccessUrl("/admin/add-product", true) // Đăng nhập thành công thì vào Admin
                        .failureUrl("/login?error=true") // Thất bại thì báo lỗi
                        .permitAll()
                )

                .logout((logout) -> logout
                        .logoutSuccessUrl("/login?logout=true") // Đăng xuất xong về lại trang login
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Tạo tài khoản Admin mặc định trong bộ nhớ
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123456")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}