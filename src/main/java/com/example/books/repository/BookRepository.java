package com.example.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.books.model.Book;
import com.example.books.model.User;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
}
