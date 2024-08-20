package org.example.repository;

import java.util.List;

public interface Repository<K, E> {

    E save(E entity);
    List<E> findAll();
    E findById(K id);
    void update(E entity);
    void delete(K id);

}
