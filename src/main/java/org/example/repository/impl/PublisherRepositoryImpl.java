package org.example.repository.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.Book;
import org.example.entity.Publisher;
import org.example.repository.BookRepository;
import org.example.repository.PublisherRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherRepositoryImpl implements PublisherRepository {

    private final BookRepository bookRepository = BookRepositoryImpl.getInstance();
    @Getter
    private static final PublisherRepositoryImpl instance = new PublisherRepositoryImpl();

    private static final String SAVE_QUERY = """
            INSERT INTO publishers (name)
            VALUES (?);
            """;
    private static final String FIND_ALL_AUTHORS_QUERY = """
            SELECT * FROM author_publisher
            WHERE publisher_id = ?
            """;
    private static final String FIND_ALL_QUERY = """
            SELECT * FROM publishers
            """;
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + """
            WHERE id = ?
            """;



    @Override
    @SneakyThrows
    public Publisher save(Publisher publisher) {
        if (publisher == null) throw new IllegalArgumentException();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setString(1, publisher.getName());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                publisher.setId(generatedKeys.getInt("id"));
            }

            return publisher;
        }
    }

    @Override
    @SneakyThrows
    public List<Publisher> findAll() {
        List<Publisher> allPublishers = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                allPublishers.add(buildPublisher(resultSet));
            }
        }

        return allPublishers;
    }

    @Override
    @SneakyThrows
    public List<Publisher> findAllByAuthorId(Integer authorId) {
        List<Publisher> allPublishersByAuthorId = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_AUTHORS_QUERY)) {
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> publisherIds = new ArrayList<>();
            while (resultSet.next()) {
                publisherIds.add(resultSet.getInt("publisher_id"));
            }

            for (Integer id : publisherIds) {
                allPublishersByAuthorId.add(findById(id));
            }
        }

        return allPublishersByAuthorId;
    }

    @Override
    @SneakyThrows
    public Publisher findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Publisher publisher = null;
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                publisher = buildPublisher(resultSet);
            }
        }

        return publisher;
    }

    @Override
    public void update(Publisher publisher) {

    }

    @Override
    public void delete(Integer id) {

    }

    private Publisher buildPublisher(ResultSet resultSet) throws SQLException {
        Integer publisherId = resultSet.getInt("id");
        var allBooksByPublisherId = bookRepository.findAllByPublisherId(publisherId);
        var allAuthorsByPublisherId = allBooksByPublisherId.stream()
                .map(Book::getAuthor)
                .distinct()
                .toList();

        return Publisher.builder()
                .id(publisherId)
                .name(resultSet.getString("name"))
                .books(allBooksByPublisherId)
                .authors(allAuthorsByPublisherId)
                .build();
    }
}
