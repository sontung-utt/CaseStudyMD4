package com.codegym.casestudymd4.service;

import java.util.Optional;

public interface IGenerateService<E> {
    Iterable<E> findAll();

    Optional<E> findById(Long id);

    void save(E e);

    void delete(Long id);
}
