package org.example.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.AuthorDTO;
import org.example.entity.Author;
import org.example.mapper.AuthorMapper;
import org.example.repository.AuthorRepository;
import org.example.repository.impl.AuthorRepositoryImpl;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorService {

    @Getter
    private static final AuthorService instance = new AuthorService();

    private final AuthorRepository authorRepository = AuthorRepositoryImpl.getInstance();
    private final AuthorMapper authorMapper = AuthorMapper.getInstance();

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::mapFrom)
                .toList();
    }

    public AuthorDTO findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Author author = authorRepository.findById(id);
        if (author == null) return null;

        return authorMapper.mapFrom(author);
    }

}
