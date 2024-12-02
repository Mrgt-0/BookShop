package com.books.BookStore.example.Dao;

import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Service.BookStoreService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {
    @PersistenceContext
    protected EntityManager entityManager;
    private Class<T> entiryClass;
    private static final Logger logger = LogManager.getLogger(BookStoreService.class);

    public GenericDaoImpl(Class<T> entiryClass){
        this.entiryClass=entiryClass;
    }
    @Override
    public T create(T entity){
        entityManager.persist(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getEntityClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public Optional<T> getById(ID id){
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    @Override
    public List<T> getAll() {
        TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClass().getSimpleName() + " e", getEntityClass());
        return query.getResultList();
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Optional<Book> entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    protected abstract String getCreateSql();

    protected abstract void delete(int bookId);

    protected abstract void populateCreateStatement(PreparedStatement statement, T entity) throws SQLException;
    protected abstract void setId(T entity, ID id);

    protected abstract String getSelectByIdSql();
    protected abstract void setIdParameter(PreparedStatement statement, ID id) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getSelectAllSql();

    protected abstract String getUpdateSql();
    protected abstract void populateUpdateStatement(PreparedStatement statement, T entity) throws SQLException;

    protected abstract String getDeleteSql();
}
