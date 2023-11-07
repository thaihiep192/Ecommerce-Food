package com.example.customer.controller;

import com.example.library.dto.CategoryDto;
import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.service.CategoryService;
import com.example.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@RequiredArgsConstructor
@Controller
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    @GetMapping("/menu")
    public String menu(Model model){
        model.addAttribute("title", "Menu");
        model.addAttribute("page", "Products");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products = productService.products();
        model.addAttribute("products",products);
        model.addAttribute("categories", categories);
        return "index";
    }
    @GetMapping("/product-detail")
    public String details( Long id, Model model){
        ProductDto product = productService.getById(id);
        List<ProductDto> products = productService.findAllByCategory(product.getCategory().getName());
        model.addAttribute("productDetail", product);
        model.addAttribute("products", products);
        model.addAttribute("title","Product Detail");
        model.addAttribute("page", "Product Detail");
        return "product-detail";
    }
    @GetMapping("/shop-detail")
    public String  shopDetail(Model model){
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews",listView);
        model.addAttribute("title","Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return  "shop-detail";
    }
    @GetMapping("/high-price")
    public String filterHighPrice(Model model){
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categoryDtoList);
        List<ProductDto> products = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title","Shop Detail");
        model.addAttribute("page","Shop Detail");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", products);
        return "shop-detail";
    }
    @GetMapping("/lower-price")
    public String filterLowPrice(Model model){
        List<CategoryDto> categoryDtoList = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categoryDtoList);
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title","Shop Detail");
        model.addAttribute("page","Shop Detail");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", products);
        return "shop-detail";
    }
    @GetMapping("/find-products")
    public String productsInCategory( Long id, Model model){
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", productDtos.get(0).getCategory().getName());
        model.addAttribute("products", productDtos);
        return "products";
    }
    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword , Model model){
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title","Search Product");
        model.addAttribute("page", "Result Search");
        model.addAttribute("products", productDtos);
        return "products";
    }


}
