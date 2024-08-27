package org.example.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookDto {

    String title;
    String author;
    Integer yearOfPublication;
    String publisher;
    String isbn;
}
