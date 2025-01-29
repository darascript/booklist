package com.example.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.books.model.Book;
import com.example.books.model.User;
import com.example.books.repository.BookRepository;
import com.example.books.repository.UserRepository;

import java.util.List;
import java.util.Optional;
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Book> getBooksForUser(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return bookRepository.findByUser(user);
    }

    public void addBook(String title, String author, String description, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setUser(user);
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Method to update an existing book
    public void updateBook(Long bookId, String title, String author, String description, String username) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getUser().equals(user)) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setDescription(description);
            bookRepository.save(book);
        } else {
            throw new RuntimeException("Unauthorized to update this book");
        }
    }
}

