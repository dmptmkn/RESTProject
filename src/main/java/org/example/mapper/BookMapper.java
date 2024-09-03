package org.example.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.BookDto;
import org.example.entity.Book;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper implements Mapper<Book, BookDto> {

    @Getter
    private static final BookMapper instance = new BookMapper();

    @Override
    public BookDto mapFrom(Book book) {
        return BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor().getName() + " " + book.getAuthor().getLastName())
                .yearOfPublication(book.getYearOfPublication())
                .publisher(book.getPublisher().getName())
                .isbn(book.getIsbn())
                .build();
    }
}
