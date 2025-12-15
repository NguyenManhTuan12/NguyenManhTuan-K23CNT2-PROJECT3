package Moon.Coffee.service;

import Moon.Coffee.entity.CartItem;
import Moon.Coffee.entity.Product;
import Moon.Coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // QUAN TRỌNG: Giúp giỏ hàng lưu riêng cho từng khách hàng
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    private List<CartItem> items = new ArrayList<>();

    // 1. Thêm sản phẩm vào giỏ
    public void addToCart(Long productId) {
        // Kiểm tra xem món này đã có trong giỏ chưa
        for (CartItem item : items) {
            // Lưu ý: Sử dụng .getId() để khớp với file CartItem.java
            if (item.getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1); // Có rồi thì tăng số lượng
                return;
            }
        }

        // Chưa có thì tìm trong Database và thêm mới
        Product p = productRepository.findById(productId).orElse(null);
        if (p != null) {
            items.add(new CartItem(p.getId(), p.getName(), p.getPrice(), 1, p.getImageName()));
        }
    }

    // 2. Lấy danh sách để hiển thị ra màn hình
    public List<CartItem> getItems() {
        return items;
    }

    // 3. Xóa sản phẩm khỏi giỏ
    public void removeFromCart(Long productId) {
        items.removeIf(item -> item.getId().equals(productId));
    }

    // 4. Cập nhật số lượng (Tăng/Giảm)
    public void updateQuantity(Long productId, int quantity) {
        for (CartItem item : items) {
            if (item.getId().equals(productId)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    removeFromCart(productId); // Nếu giảm về 0 thì xóa luôn
                }
                return;
            }
        }
    }

    // 5. Tính tổng tiền
    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    // 6. Xóa sạch giỏ hàng (Dùng khi Thanh toán thành công)
    public void clearCart() {
        items.clear();
    }
}