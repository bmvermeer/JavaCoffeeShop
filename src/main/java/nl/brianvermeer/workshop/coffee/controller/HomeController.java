package nl.brianvermeer.workshop.coffee.controller;

import nl.brianvermeer.workshop.coffee.repository.SearchRepository;
import nl.brianvermeer.workshop.coffee.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final ProductService productService;
    private final SearchRepository searchRepository;

    public HomeController(ProductService productService, SearchRepository searchRepository) {
        this.productService = productService;
        this.searchRepository = searchRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String homePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @PostMapping("/")
    public String searchProducts(Model model, @RequestParam String input) {
        model.addAttribute("products", searchRepository.searchProduct(input));
        return "index";
    }
}
