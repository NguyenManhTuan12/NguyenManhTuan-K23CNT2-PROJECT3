package Moon.Coffee.repository;

import Moon.Coffee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 1. Tính tổng doanh thu (Chỉ tính đơn Completed)
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'Completed'")
    Double sumTotalRevenue();

    // 2. Đếm tổng số đơn
    @Query("SELECT COUNT(o) FROM Order o")
    Long countTotalOrders();
}