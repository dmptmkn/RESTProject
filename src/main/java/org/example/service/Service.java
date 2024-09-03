package org.example.service;

import java.util.List;

public interface Service<K, T> {

    List<T> findAll();
    T findById(K id);
}
