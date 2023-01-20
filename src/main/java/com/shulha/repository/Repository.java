package com.shulha.repository;

import java.util.Optional;

public interface Repository<K, V> {
    void save(final V value);

    Optional<V> getById(final K id);

    V[] getAll();

    void remove(final K id);

    void removeAll();
}
