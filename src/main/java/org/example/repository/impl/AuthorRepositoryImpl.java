package org.example.repository.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorRepositoryImpl implements AuthorRepository {

    @Getter
    private static final AuthorRepositoryImpl instance = new AuthorRepositoryImpl();

    private static final String SAVE_QUERY = """
            INSERT INTO authors (name, last_name)
            VALUES (?, ?);
            """;

    @Override
    @SneakyThrows
    public Author save(Author author) {
        if (author == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getLastName());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getInt("id"));
            }
            return author;
        }
    }

    @Override
    public List<Author> findAll() {
        return List.of();
    }

    @Override
    public Author findById(Integer id) {
        return null;
    }

    @Override
    public void update(Author entity) {

    }

    @Override
    public void delete(Integer id) {

    }
}
