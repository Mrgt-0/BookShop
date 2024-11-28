package com.books.BookStore.example.Dao;

import com.books.BookStore.example.Model.Book;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
    T create(T entity);

    @SuppressWarnings("unchecked")
    Class<T> getEntityClass();

    Optional<T> getById(ID id);
    List<T> getAll();
    T update(T entity);
    void delete(Optional<Book> entity);
}
