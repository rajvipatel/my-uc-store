package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
public class CartService {

    @Autowired
    private BookService bookService;

    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(Long bookId, int quantity) {
        Optional<Book> bookOpt = bookService.getBookById(bookId);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            
            // Check if book is already in cart
            Optional<CartItem> existingItem = cartItems.stream()
                    .filter(item -> item.getBook().getId().equals(bookId))
                    .findFirst();
            
            if (existingItem.isPresent()) {
                // Update quantity
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                // Add new item
                cartItems.add(new CartItem(book, quantity));
            }
        }
    }

    public void removeFromCart(Long bookId) {
        cartItems.removeIf(item -> item.getBook().getId().equals(bookId));
    }

    public void updateQuantity(Long bookId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(bookId);
            return;
        }
        
        cartItems.stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public int getCartItemCount() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getCartTotal() {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public void clearCart() {
        cartItems.clear();
    }

    public BigDecimal getTax() {
        // 8.5% tax rate
        return getCartTotal().multiply(new BigDecimal("0.085"));
    }

    public BigDecimal getShipping() {
        // Free shipping over $50, otherwise $5.99
        return getCartTotal().compareTo(new BigDecimal("50.00")) >= 0 
                ? BigDecimal.ZERO 
                : new BigDecimal("5.99");
    }

    public BigDecimal getFinalTotal() {
        return getCartTotal().add(getTax()).add(getShipping());
    }
}
