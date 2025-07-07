package com.bookstore.controller;

import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        model.addAttribute("books", bookService.searchBooks(query));
        model.addAttribute("searchQuery", query);
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "index";
    }

    @GetMapping("/category")
    public String category(@RequestParam("name") String categoryName, Model model) {
        model.addAttribute("books", bookService.getBooksByCategory(categoryName));
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "index";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("bookId") Long bookId, 
                           @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                           RedirectAttributes redirectAttributes) {
        cartService.addToCart(bookId, quantity);
        redirectAttributes.addFlashAttribute("message", "Book added to cart successfully!");
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        model.addAttribute("tax", cartService.getTax());
        model.addAttribute("shipping", cartService.getShipping());
        model.addAttribute("finalTotal", cartService.getFinalTotal());
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("bookId") Long bookId, 
                            @RequestParam("quantity") int quantity,
                            RedirectAttributes redirectAttributes) {
        cartService.updateQuantity(bookId, quantity);
        redirectAttributes.addFlashAttribute("message", "Cart updated successfully!");
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("bookId") Long bookId,
                                RedirectAttributes redirectAttributes) {
        cartService.removeFromCart(bookId);
        redirectAttributes.addFlashAttribute("message", "Item removed from cart!");
        return "redirect:/cart";
    }
}
