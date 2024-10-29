package Dao;

import java.util.List;
import javax.persistence.EntityManager;

public interface GenericDao<T, ID> {
    T create(T entity);
    T getById(ID id);
    List<T> getAll();
    void update(T entity);
    void delete(ID id);
}
