package k23cnt2nguyenmanhtuan_day07.service;

import k23cnt2nguyenmanhtuan_day07.entity.Category;
import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category saveCategory(Category category);

    void deleteCategory(Long id);
}
