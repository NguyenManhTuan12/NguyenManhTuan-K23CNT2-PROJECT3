package k23cnt2nguyenmanhtuan_day07.service;

import k23cnt2nguyenmanhtuan_day07.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAll();              // list tất cả
    Product getById(Long id);            // lấy theo id
    void save(Product product);          // thêm & sửa
    void delete(Long id);                // xóa
}
