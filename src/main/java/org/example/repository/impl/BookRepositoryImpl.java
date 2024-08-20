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
            SELECT b.id,
                   b.title,
                   b.author_id,
                   b.year_of_publication,
                   b.publisher_id,
                   b.isbn,
                   a.id,
                   a.name,
                   a.last_name,
                   p.id,
                   p.name
            FROM books b
                     JOIN authors a on b.author_id = a.id
                     JOIN publishers p on b.publisher_id = p.id
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE b.id = ?
            """;
    private static final String DELETE_QUERY = """
            DELETE
            FROM books
            WHERE id = ?
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
        if (id == null || id < 1) throw new IllegalArgumentException();
        
//        try (Connection connection = ConnectionManager.getConnection();
//             connection.prepareStatement(DELETE_QUERY)
    }

    @SneakyThrows
    private Book buildBook(ResultSet resultSet) {
        return Book.builder()
                .id(resultSet.getInt("b.id"))
                .title(resultSet.getString("b.title"))
                .author(Author.builder()
                        .id(resultSet.getInt("a.id"))
                        .name(resultSet.getString("a.name"))
                        .lastName(resultSet.getString("a.last_name"))
                        .build())
                .yearOfPublication(resultSet.getInt("b.year_of_publication"))
                .publisher(Publisher.builder()
                        .id(resultSet.getInt("p.id"))
                        .name(resultSet.getString("p.name"))
                        .build())
                .isbn(resultSet.getString("b.isbn"))
                .build();

    }

}
