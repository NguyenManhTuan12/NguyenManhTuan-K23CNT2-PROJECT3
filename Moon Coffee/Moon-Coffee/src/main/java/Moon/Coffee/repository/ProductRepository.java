package Moon.Coffee.repository;

import Moon.Coffee.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Tìm sản phẩm theo ID danh mục (Dùng cho thanh Menu lọc danh mục)
    List<Product> findByCategoryId(Long categoryId);

    // 2. Tìm theo tên (Dùng cho thanh Tìm kiếm)
    // - Containing: Tìm từ khóa nằm trong tên (ví dụ gõ "trà" ra "trà đào", "trà sữa")
    // - IgnoreCase: Không phân biệt chữ hoa chữ thường
    List<Product> findByNameContainingIgnoreCase(String keyword);
}