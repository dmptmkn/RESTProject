package org.example.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AuthorDto {

    String name;
    String lastName;
    List<String> books;
    List<String> publishers;
}
