package Moon.Coffee.repository;

import Moon.Coffee.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // Hiện tại để trống, JpaRepository đã hỗ trợ sẵn các hàm lưu, xóa, sửa rồi.
}