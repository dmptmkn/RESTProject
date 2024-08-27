package org.example.repository;

import org.example.entity.Book;

import java.util.List;

public interface BookRepository extends Repository<Integer, Book> {

    List<Book> findAllByAuthorId(Integer authorId);
    List<Book> findAllByPublisherId(Integer publisherId);

}
