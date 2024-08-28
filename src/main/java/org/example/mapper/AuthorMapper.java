package org.example.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Publisher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorMapper implements Mapper<Author, AuthorDto> {

    @Getter
    private static final AuthorMapper instance = new AuthorMapper();

    @Override
    public AuthorDto mapFrom(Author author) {
        if (author == null) return null;

        return AuthorDto.builder()
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
