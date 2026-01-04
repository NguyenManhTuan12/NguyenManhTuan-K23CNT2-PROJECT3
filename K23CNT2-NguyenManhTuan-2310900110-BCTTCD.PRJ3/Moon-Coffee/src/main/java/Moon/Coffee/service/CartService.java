package Moon.Coffee.service;

import Moon.Coffee.dto.CartItem;
import Moon.Coffee.entity.Product;
import Moon.Coffee.repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
@Data
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * 1. Thêm món vào giỏ
     * Logic: Kiểm tra trùng ID -> Nếu trùng thì tăng số lượng -> Nếu chưa có thì thêm mới.
     * Đã đồng bộ với CartItem: Sử dụng newItem.setProduct(product) để tự động gán ID và Giá.
     */
    public void addToCart(Long productId) {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        for (CartItem item : cartItems) {
            // Sử dụng hàm getProductId() an toàn từ DTO
            if (item.getProductId() != null && item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        // Nếu chưa có, tìm trong database và thêm mới
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            CartItem newItem = new CartItem();
            newItem.setProduct(product); // Quan trọng: Hàm này tự động điền productId và price
            newItem.setQuantity(1);
            cartItems.add(newItem);
        }
    }

    // 2. Lấy tổng số lượng sản phẩm (để hiển thị lên icon giỏ hàng)
    public int getCount() {
        if (cartItems.isEmpty()) return 0;
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // 3. Tính tổng tiền hàng (Dùng cho CheckoutController)
    public double getTotalPrice() {
        return cartItems.stream()
                // Lọc bỏ các item lỗi không có giá
                .filter(item -> item.getPrice() != null)
                // Tính tổng: Giá * Số lượng
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    // 4. Cập nhật số lượng (Dùng khi bấm nút +/- trong giỏ)
    public void updateQuantity(Long productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProductId() != null && item.getProductId().equals(productId)) {
                // Đảm bảo số lượng không nhỏ hơn 1
                int newQuantity = Math.max(1, quantity);
                item.setQuantity(newQuantity);
                return;
            }
        }
    }

    // 5. Xóa sản phẩm khỏi giỏ
    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProductId() != null && item.getProductId().equals(productId));
    }

    // 6. Xóa sạch giỏ hàng (Gọi sau khi đặt hàng thành công)
    public void clearCart() {
        cartItems.clear();
    }

    // Getter tường minh cho cartItems (Phòng trường hợp Lombok lỗi trên một số máy)
    public List<CartItem> getCartItems() {
        return cartItems;
    }
}