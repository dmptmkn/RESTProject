package org.example.service.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.example.mapper.AuthorMapper;
import org.example.repository.AuthorRepository;
import org.example.repository.impl.AuthorRepositoryImpl;
import org.example.service.AuthorService;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorServiceImpl implements AuthorService {

    @Getter
    private static final AuthorServiceImpl instance = new AuthorServiceImpl();

    private AuthorRepository repository = AuthorRepositoryImpl.getInstance();
    private final AuthorMapper mapper = AuthorMapper.getInstance();

    @Override
    public List<AuthorDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::mapFrom)
                .toList();
    }

    @Override
    public AuthorDto findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Author author = repository.findById(id);
        if (author == null) return null;

        return mapper.mapFrom(author);
    }
}
