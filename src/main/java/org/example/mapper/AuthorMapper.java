package org.example.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.AuthorDTO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Publisher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorMapper implements Mapper<Author, AuthorDTO> {

    @Getter
    private static final AuthorMapper instance = new AuthorMapper();

    @Override
    public AuthorDTO mapFrom(Author author) {
        if (author == null) throw new IllegalArgumentException();
        return AuthorDTO.builder()
                .name(author.getName())
                .lastName(author.getLastName())
                .books(author.getBooks().stream()
                        .map(Book::getTitle)
                        .toList())
                .publishers(author.getPublishers().stream()
                        .map(Publisher::getName)
                        .toList())
                .build();
    }
}
