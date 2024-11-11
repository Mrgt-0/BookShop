package Dao;

import BookStoreService.BookStoreService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.List;

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
    public T getById(ID id){
        return entityManager.find(getEntityClass(), id);
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
    public void delete(T entity) {
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
