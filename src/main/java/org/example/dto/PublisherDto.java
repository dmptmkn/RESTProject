package org.example.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PublisherDto {

    String name;
    List<String> authors;
    List<String> books;
}
