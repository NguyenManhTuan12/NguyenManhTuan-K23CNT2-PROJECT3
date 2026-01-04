package Moon.Coffee.dto;

import Moon.Coffee.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;
    private Double price;
    private Long productId; // ID dùng để so khớp nhanh trong Service

    /**
     * 1. Logic quan trọng nhất: Tự động đồng bộ ID và Giá khi gán Product.
     * Giúp CartService không cần set từng trường thủ công, tránh lỗi Null.
     */
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            this.productId = product.getId();
            // Nếu item chưa có giá riêng, lấy giá gốc từ sản phẩm
            if (this.price == null) {
                this.price = product.getPrice();
            }
        }
    }

    // 2. Hàm tính thành tiền của riêng món này (Dùng trong HTML: ${item.totalItemPrice})
    public Double getTotalItemPrice() {
        if (price == null) return 0.0;
        return price * quantity;
    }

    // 3. Các hàm Helper để Thymeleaf gọi trực tiếp, tránh lỗi "cannot find symbol"
    public String getName() {
        return (product != null) ? product.getName() : "Sản phẩm không tồn tại";
    }

    public String getImageName() {
        return (product != null) ? product.getImageName() : "default.png";
    }

    // 4. Đảm bảo luôn trả về ID chính xác cho Controller/Service
    public Long getProductId() {
        if (this.productId != null) return this.productId;
        return (product != null) ? product.getId() : null;
    }
}