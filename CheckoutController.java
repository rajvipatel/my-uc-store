package com.bookstore.controller;

import com.bookstore.model.Customer;
import com.bookstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cartService.isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("customer", new Customer());
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        model.addAttribute("tax", cartService.getTax());
        model.addAttribute("shipping", cartService.getShipping());
        model.addAttribute("finalTotal", cartService.getFinalTotal());
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        
        return "checkout";
    }

    @PostMapping("/checkout/process")
    public String processCheckout(@Valid @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        
        if (cartService.isEmpty()) {
            return "redirect:/cart";
        }
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("cartItems", cartService.getCartItems());
            model.addAttribute("cartTotal", cartService.getCartTotal());
            model.addAttribute("tax", cartService.getTax());
            model.addAttribute("shipping", cartService.getShipping());
            model.addAttribute("finalTotal", cartService.getFinalTotal());
            model.addAttribute("cartItemCount", cartService.getCartItemCount());
            return "checkout";
        }

        // Simulate payment processing delay
        try {
            Thread.sleep(2000); // 2 second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Generate order number
        String orderNumber = generateOrderNumber();
        
        // Store order details for confirmation page
        redirectAttributes.addFlashAttribute("orderNumber", orderNumber);
        redirectAttributes.addFlashAttribute("customer", customer);
        redirectAttributes.addFlashAttribute("cartItems", cartService.getCartItems());
        redirectAttributes.addFlashAttribute("cartTotal", cartService.getCartTotal());
        redirectAttributes.addFlashAttribute("tax", cartService.getTax());
        redirectAttributes.addFlashAttribute("shipping", cartService.getShipping());
        redirectAttributes.addFlashAttribute("finalTotal", cartService.getFinalTotal());
        redirectAttributes.addFlashAttribute("orderDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")));
        
        // Clear the cart after successful order
        cartService.clearCart();
        
        return "redirect:/checkout/confirmation";
    }

    @GetMapping("/checkout/confirmation")
    public String confirmation(Model model) {
        // If no order data is present, redirect to home
        if (!model.containsAttribute("orderNumber")) {
            return "redirect:/";
        }
        
        model.addAttribute("cartItemCount", cartService.getCartItemCount());
        return "confirmation";
    }

    private String generateOrderNumber() {
        // Generate a random order number (format: ORD-YYYYMMDD-XXXX)
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomNum = new Random().nextInt(9999) + 1;
        return String.format("ORD-%s-%04d", date, randomNum);
    }
}
