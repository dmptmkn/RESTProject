package org.example.repository.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.repository.PublisherRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorRepositoryImpl implements AuthorRepository {

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();
    private final PublisherRepository publisherRepository = PublisherRepositoryImpl.getInstance();

    @Getter
    private static final AuthorRepositoryImpl instance = new AuthorRepositoryImpl();
    private static final String SAVE_QUERY = """
            INSERT INTO authors (name, last_name)
            VALUES (?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
           SELECT * FROM authors
           """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
           WHERE id = ?
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
    @SneakyThrows
    public List<Author> findAll() {
        List<Author> allAuthors = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                allAuthors.add(buildAuthor(resultSet));
            }
        }

        return allAuthors;
    }

    @Override
    @SneakyThrows
    public Author findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Author author = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                author = buildAuthor(resultSet);
            }
        }

        return author;
    }

    @Override
    public void update(Author entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @SneakyThrows
    private Author buildAuthor(ResultSet resultSet) {
        Integer authorId = resultSet.getInt("id");

        return Author.builder()
                .id(authorId)
                .name(resultSet.getString("name"))
                .lastName(resultSet.getString("last_name"))
                .books(bookRepository.findAllByAuthorId(authorId))
                .publishers(publisherRepository.findAllByAuthorId(authorId))
                .build();
    }
}
