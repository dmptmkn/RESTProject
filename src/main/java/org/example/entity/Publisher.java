package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"books", "authors"})
public class Publisher {

    private Integer id;
    private String name;
    private List<Book> books;
    private List<Author> authors;
}
