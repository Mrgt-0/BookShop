package com.books.BookStore.example.Repository;

import com.books.BookStore.example.Dao.GenericDaoImpl;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Model.User;
import com.books.BookStore.example.Status.UserRole;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends GenericDaoImpl<User, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(RequestRepository.class);

    public UserRepository() { super(User.class); }

    public User getByLogin(String login){
        try{
            User user = entityManager.createQuery("SELECT u FROM User b WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
            logger.info("Книга с названием '{}' найдена.", login);
            return user;
        } catch (NoResultException e) {
            logger.warn("Пользователь с названием '{}' не найдена.", login);
            return null;
        }
    }
    @Override
    protected String getCreateSql() {
        return "INSERT INTO User(userId) VALUES (?)";
    }

    @Override
    protected void delete(int userId) {
        User user=entityManager.find(User.class, userId);
        if(user!=null){
            entityManager.remove(user);
            logger.info("Пользователь с ID {} успешно удален.", userId);
        }else
            logger.warn("Не удалось удалить пользователя: пользователь с ID {} не найден.", userId);
    }

    @Override
    protected void populateCreateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setInt(1, user.getUserId());
        statement.setString(3, user.getLogin());
        statement.setString(4, user.getPassword());
        statement.setString(2, user.getRole());
        logger.info("Заполняем запрос на создание пользователя: {}", user);
    }

    @Override
    protected void setId(User user, Integer userId) { user.setUserId(userId); }

    @Override
    protected String getSelectByIdSql() { return "SELECT * FROM User WHERE userId = ?"; }

    @Override
    protected void setIdParameter(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("login"),
                resultSet.getString("password"),
                resultSet.getString("role")
                );
    }

    @Override
    protected String getSelectAllSql() {
        return "SELECT * FROM User";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE User SET role = ?, login = ?, password = ?";
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getRole());
        statement.setString(2, user.getLogin());
        statement.setString(3, user.getPassword());
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM User WHERE userId = ?";
    }
}
