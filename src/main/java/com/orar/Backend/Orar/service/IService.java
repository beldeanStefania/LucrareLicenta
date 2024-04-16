package com.orar.Backend.Orar.service;

import java.util.List;

public interface IService<T> {
    T create(T entity);
    T get(Long id);
    List<T> getAll();
    T update(T entity);
    Boolean delete(Long id);

}
