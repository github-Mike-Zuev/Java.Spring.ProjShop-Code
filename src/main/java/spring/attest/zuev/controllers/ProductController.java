package spring.attest.zuev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.attest.zuev.services.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("categories", productService.getAllCategories());
        return "product/product";
    }
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product",productService.getProductId(id));
        return "product/infoProduct";
    }
    @PostMapping("/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("minPrice") String minPrice, @RequestParam("maxPrice") String maxPrice, @RequestParam(value = "sorting", required = false, defaultValue = "")String sorting, @RequestParam(value = "inCategory", required = false, defaultValue = "")int inCategoryId, Model model){ /** передача строки поиска в нижнем регистре .toLowerCase */
        model.addAttribute("search_result", productService.productSearch(search.toLowerCase(), minPrice, maxPrice, sorting, inCategoryId));
        model.addAttribute("categories", productService.getAllCategories());

        model.addAttribute("value_search", search);
        model.addAttribute("value_minPrice", minPrice);
        model.addAttribute("value_maxPrice", maxPrice);
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }
}
