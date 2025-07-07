package com.bookstore.service;

import com.bookstore.model.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    private final List<Book> books = Arrays.asList(
        new Book(1L, "1984", "George Orwell", new BigDecimal("12.99"), 
                "A dystopian social science fiction novel and cautionary tale about the dangers of totalitarianism.", 
                "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=300&h=400&fit=crop", "Fiction"),
        
        new Book(2L, "To Kill a Mockingbird", "Harper Lee", new BigDecimal("14.99"), 
                "A gripping tale of racial injustice and childhood innocence in the American South.", 
                "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=300&h=400&fit=crop", "Fiction"),
        
        new Book(3L, "The Great Gatsby", "F. Scott Fitzgerald", new BigDecimal("13.99"), 
                "A classic American novel about the Jazz Age and the American Dream.", 
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=300&h=400&fit=crop", "Fiction"),
        
        new Book(4L, "Pride and Prejudice", "Jane Austen", new BigDecimal("11.99"), 
                "A romantic novel of manners set in Georgian England.", 
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=300&h=400&fit=crop", "Romance"),
        
        new Book(5L, "The Catcher in the Rye", "J.D. Salinger", new BigDecimal("15.99"), 
                "A controversial coming-of-age story about teenage rebellion and alienation.", 
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=400&fit=crop", "Fiction"),
        
        new Book(6L, "Harry Potter and the Sorcerer's Stone", "J.K. Rowling", new BigDecimal("16.99"), 
                "The magical beginning of the beloved Harry Potter series.", 
                "https://images.unsplash.com/photo-1621351183012-e2f9972dd9bf?w=300&h=400&fit=crop", "Fantasy"),
        
        new Book(7L, "The Alchemist", "Paulo Coelho", new BigDecimal("13.99"), 
                "A philosophical novel about following your dreams and finding your personal legend.", 
                "https://images.unsplash.com/photo-1589998059171-988d887df646?w=300&h=400&fit=crop", "Philosophy"),
        
        new Book(8L, "Clean Code", "Robert C. Martin", new BigDecimal("39.99"), 
                "A handbook of agile software craftsmanship for writing maintainable code.", 
                "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=300&h=400&fit=crop", "Technology"),
        
        new Book(9L, "Sapiens", "Yuval Noah Harari", new BigDecimal("18.99"), 
                "A brief history of humankind from the Stone Age to the present.", 
                "https://images.unsplash.com/photo-1553729459-efe14ef6055d?w=300&h=400&fit=crop", "History"),
        
        new Book(10L, "Atomic Habits", "James Clear", new BigDecimal("17.99"), 
                "An easy and proven way to build good habits and break bad ones.", 
                "https://images.unsplash.com/photo-1606092195730-5d7b9af1efc5?w=300&h=400&fit=crop", "Self-Help")
    );

    public List<Book> getAllBooks() {
        return books;
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    public List<Book> getBooksByCategory(String category) {
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Book> searchBooks(String query) {
        String lowerQuery = query.toLowerCase();
        return books.stream()
                .filter(book -> 
                    book.getTitle().toLowerCase().contains(lowerQuery) ||
                    book.getAuthor().toLowerCase().contains(lowerQuery) ||
                    book.getDescription().toLowerCase().contains(lowerQuery)
                )
                .toList();
    }
}
