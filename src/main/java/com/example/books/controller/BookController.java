package com.example.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

import com.example.books.model.Book;
import com.example.books.service.BookService;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;


    @GetMapping("/books")
    public String viewBooks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Book> books = bookService.getBooksForUser(userDetails.getUsername());
        model.addAttribute("books", books);
        return "book-list";
    }

    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book"; 
    }
    @PostMapping("/books/add")
    public String createBook(@RequestParam String title, @RequestParam String author,  @RequestParam String description, @AuthenticationPrincipal UserDetails userDetails) {
        bookService.addBook(title, author, description, userDetails.getUsername());
        return "redirect:/books";
    }

    @PostMapping("books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String viewBookById(@PathVariable Long id, Model model) {
        return bookService.findBookById(id)
            .map(book -> {
                model.addAttribute("book", book);
                return "book-detail"; 
            })
            .orElse("error"); 
    }
    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable Long id, Model model) {
        Optional<Book> bookOptional = bookService.findBookById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            return "book-details";
        } else {
            return "error";  // Redirect to an error page if the book is not found
        }
    }

    // Endpoint to show the edit book form
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Optional<Book> bookOptional = bookService.findBookById(id);
        if (bookOptional.isPresent()) {
            model.addAttribute("book", bookOptional.get());
            return "edit-book";
        } else {
            return "error";  // Redirect to an error page if the book is not found
        }
    }

    // Endpoint to update the book details
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam String author, 
                             @RequestParam String description, @AuthenticationPrincipal UserDetails userDetails) {
        bookService.updateBook(id, title, author, description, userDetails.getUsername());
        return "redirect:/books";
    }
}

