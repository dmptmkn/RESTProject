package org.example.mapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.PublisherDto;
import org.example.entity.Book;
import org.example.entity.Publisher;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherMapper implements Mapper<Publisher, PublisherDto> {

    @Getter
    private static final PublisherMapper instance = new PublisherMapper();

    @Override
    public PublisherDto mapFrom(Publisher publisher) {
        return PublisherDto.builder()
                .name(publisher.getName())
                .books(publisher.getBooks().stream()
                        .map(Book::getTitle)
                        .toList())
                .authors(publisher.getAuthors().stream()
                        .map(author -> author.getName() + " " + author.getLastName())
                        .toList())
                .build();
    }
}
