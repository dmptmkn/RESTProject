package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private Integer id;
    private String title;
    private Author author;
    private Integer yearOfPublication;
    private Publisher publisher;
    private String isbn;
}
