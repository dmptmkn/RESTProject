package org.example.repository.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Publisher;
import org.example.repository.BookRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookRepositoryImpl implements BookRepository {

    @Getter
    private static final BookRepositoryImpl instance = new BookRepositoryImpl();

    private static final String SAVE_QUERY = """
            INSERT INTO books (title, author_id, year_of_publication, publisher_id, isbn)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM books b
                     JOIN authors a ON author_id = a.id
                     JOIN publishers p ON publisher_id = p.id
            """;
    private static final String FIND_ALL_BY_AUTHOR_ID_QUERY = FIND_ALL_QUERY + """
            WHERE b.author_id = ?
            """;
    private static final String FIND_ALL_BY_PUBLISHER_ID_QUERY = FIND_ALL_QUERY + """
            WHERE b.publisher_id = ?
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE b.id = ?
            """;

    @Override
    @SneakyThrows
    public Book save(Book book) {
        if (book == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getAuthor().getId());
            preparedStatement.setInt(3, book.getYearOfPublication());
            preparedStatement.setInt(4, book.getPublisher().getId());
            preparedStatement.setString(5, book.getIsbn());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt("id"));
            }

            return book;
        }
    }

    @Override
    @SneakyThrows
    public List<Book> findAll() {
        List<Book> allBooks = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                allBooks.add(buildBook(resultSet));
            }
        }

        return allBooks;
    }

    @Override
    @SneakyThrows
    public List<Book> findAllByAuthorId(Integer authorId) {
        return findAllById(authorId, FIND_ALL_BY_AUTHOR_ID_QUERY);
    }

    @Override
    @SneakyThrows
    public List<Book> findAllByPublisherId(Integer publisherId) {
        return findAllById(publisherId, FIND_ALL_BY_PUBLISHER_ID_QUERY);
    }

    @Override
    @SneakyThrows
    public Book findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Book book = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }
        }

        return book;
    }

    @Override
    public void update(Book entity) {
    }

    @Override
    public void delete(Integer id) {
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        return Book.builder()
                .id(resultSet.getInt(1))
                .title(resultSet.getString(2))
                .author(Author.builder()
                        .id(resultSet.getInt(3))
                        .name(resultSet.getString(8))
                        .lastName(resultSet.getString(9))
                        .build())
                .yearOfPublication(resultSet.getInt(4))
                .publisher(Publisher.builder()
                        .id(resultSet.getInt(5))
                        .name(resultSet.getString(11))
                        .build())
                .isbn(resultSet.getString(6))
                .build();
    }

    private List<Book> findAllById(Integer id, String query) throws SQLException {
        if (id == null || id < 1) throw new IllegalArgumentException();

        List<Book> allBooksById = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allBooksById.add(buildBook(resultSet));
            }
        }

        return allBooksById;
    }
}
