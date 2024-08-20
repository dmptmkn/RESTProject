package org.example.repository.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.example.entity.Publisher;
import org.example.repository.PublisherRepository;
import org.example.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherRepositoryImpl implements PublisherRepository {

    @Getter
    private static final PublisherRepositoryImpl instance = new PublisherRepositoryImpl();

    private static final String SAVE_QUERY = """
            INSERT INTO publishers (name)
            VALUES (?);
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
    public List<Publisher> findAll() {
        return List.of();
    }

    @Override
    public Publisher findById(Integer id) {
        return null;
    }

    @Override
    public void update(Publisher publisher) {

    }

    @Override
    public void delete(Integer id) {

    }
}
