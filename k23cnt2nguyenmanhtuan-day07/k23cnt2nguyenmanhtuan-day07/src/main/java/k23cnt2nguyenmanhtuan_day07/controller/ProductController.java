package k23cnt2nguyenmanhtuan_day07.controller;

import k23cnt2nguyenmanhtuan_day07.entity.Product;
import k23cnt2nguyenmanhtuan_day07.service.CategoryService;
import k23cnt2nguyenmanhtuan_day07.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ================= LIST =================
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product/product-list";
    }

    // ============== SHOW ADD FORM ==============
    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/product-form";
    }

    // ============== SAVE PRODUCT ==============
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    // ============== EDIT PRODUCT ==============
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "product/product-form";
    }

    // ============== DELETE PRODUCT ==============
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }

}
