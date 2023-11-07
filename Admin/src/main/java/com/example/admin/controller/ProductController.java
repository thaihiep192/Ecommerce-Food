package com.example.admin.controller;

import com.example.library.dto.ProductDto;
import com.example.library.model.Category;
import com.example.library.model.Product;
import com.example.library.service.CategoryService;
import com.example.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@Controller
public class ProductController {


    private final ProductService productService;


    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model){
        List<ProductDto> productDtoList = productService.allProduct();
        model.addAttribute("title", "Manage Product");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProducts(@PathVariable("pageNo")int pageNo,
                                 @RequestParam(value = "keyword") String keyword,
                                 Model model
                                ){
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Search Result");
        model.addAttribute("products", products);
        model.addAttribute("size", products.getSize());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "product-result";
    }


    @GetMapping("/add-product")
    public String addProductForm(Model model){
        model.addAttribute("title","Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product")ProductDto productDto,
                              @RequestParam("imageProduct")MultipartFile imageProduct,
                              RedirectAttributes attributes){
        try {
            productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Add successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add!");
        }
        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update-product";
    }


    @PostMapping("/update-product/{id}")
    public String processUpdate(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct")MultipartFile imageProduct,
                                RedirectAttributes attributes
    ){
        try {
            productService.update(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Update successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/products/0";

    }

    @RequestMapping(value = "/enable-product", method = {RequestMethod.PUT , RequestMethod.GET})
    public String enabledProduct(Long id, RedirectAttributes attributes){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:/products/0";
    }

    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:/products/0";
    }
}
