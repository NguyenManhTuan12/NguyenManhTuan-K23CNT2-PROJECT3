package k23cnt2nguyenmanhtuan_day07.repository;

import k23cnt2nguyenmanhtuan_day07.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
