package org.example.service.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.mapper.BookMapper;
import org.example.repository.BookRepository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.service.BookService;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookServiceImpl implements BookService {

    @Getter
    private static final BookServiceImpl instance = new BookServiceImpl();

    private final BookRepository repository = BookRepositoryImpl.getInstance();
    private final BookMapper mapper = BookMapper.getInstance();

    @Override
    public List<BookDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::mapFrom)
                .toList();
    }

    @Override
    public BookDto findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Book book = repository.findById(id);
        if (book == null) return null;

        return mapper.mapFrom(book);
    }
}
