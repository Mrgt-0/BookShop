package Dao;

import BookStoreController.BookStoreController;
import DI.Inject;
import com.mysql.cj.conf.PropertyKey.logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {

    protected Connection connection;

    @Inject
    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(BookStoreController.class);
    public GenericDaoImpl(Connection connection){
        this.connection=connection;
    }
  
    @Override
    public T create(T entity){
        String sql = getCreateSql();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            populateCreateStatement(statement, entity);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ID id = (ID) generatedKeys.getObject(1);
                        setId(entity, id);
                    }
                }
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create entity", e);
        }
    }
  
    @Override
    public T getById(ID id){
        String sql = getSelectByIdSql();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setIdParameter(statement, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get entity by ID", e);
        }
        return null;
    }
  
    @Override
    public List<T> getAll() {
        String sql = getSelectAllSql();
        List<T> entities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {

            throw new RuntimeException("Failed to get all entities", e);
        }
        return entities;
    }

    @Override
    public void update(T entity) {
        String sql = getUpdateSql();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            populateUpdateStatement(statement, entity);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update entity", e);
        }
    }
  
    @Override
    public void delete(ID id) {
        String sql = getDeleteSql();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setIdParameter(statement, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete entity", e);
        }
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
