package Dao;

import java.util.List;

public interface GenericDao<T, ID> {
    T create(T entity);

    @SuppressWarnings("unchecked")
    Class<T> getEntityClass();

    T getById(ID id);
    List<T> getAll();
    T update(T entity);
    void delete(T entity);
}
