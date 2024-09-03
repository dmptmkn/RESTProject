package org.example.repository;

import org.example.entity.Publisher;

import java.util.List;

public interface PublisherRepository extends Repository<Integer, Publisher> {

    List<Publisher> findAllByAuthorId(Integer authorId);

}
