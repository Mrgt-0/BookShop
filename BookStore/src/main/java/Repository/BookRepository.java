package Repository;

import BookStoreModel.Book;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.BookStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.sql.*;
import java.time.LocalDate;

@Repository
public class BookRepository extends GenericDaoImpl<Book, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);

    public BookRepository(){
        super(Book.class);
    }

    @Transactional
    public void save(Book book){
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.persist(book);
            logger.info("Книга с ID {} сохранена.", book.getBookId());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при добавлении книги: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении книги", e);
        }
    }

    @Transactional
    public void updateBookStatus(int bookId, BookStatus status) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Book book = entityManager.find(Book.class, bookId);
            if (book != null) {
                book.setStatus(status);
                entityManager.merge(book);
                logger.info("Статус книги с ID {} обновлён на {}.", bookId, status.name());
            } else {
                logger.warn("Книга с ID {} не найдена для обновления статуса.", bookId);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при обновлении статуса книги с ID {}: {}", bookId, e.getMessage());
            throw new RuntimeException("Ошибка при обновлении статуса книги", e);
        }
    }

    public Book getByTitle(String title) {
        Book book = null;
        try {
            book = entityManager.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class)
                    .setParameter("title", title)
                    .getSingleResult();
            logger.info("Книга с названием '{}' найдена.", title);
        } catch (NoResultException e) {
            logger.warn("Книга с названием '{}' не найдена.", title);
        } catch (Exception e) {
            logger.error("Ошибка запроса книги по названию '{}': {}", title, e.getMessage());
            throw new RuntimeException("Ошибка при запросе книги по названию", e);
        }

        return book;
    }

    @Override
    protected Class<Book> getEntityClass() {
        return Book.class;
    }

    @Override
    protected String getCreateSql(){
        return "INSERT INTO Book(title, author, status, publish_date, price, description) VALUES (?, ?, ?, ?, ?)";
    }

    @Transactional
    public void delete(int bookId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Book book = entityManager.find(Book.class, bookId);
            if (book != null) {
                entityManager.remove(book);
                logger.info("Книга с ID {} успешно удалена.", bookId);
            } else {
                logger.warn("Книга с ID {} не найдена для удаления.", bookId);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при удалении книги с ID {}: {}", bookId, e.getMessage());
            throw new RuntimeException("Ошибка при удалении книги", e);
        }
    }

    @Override
    protected void populateCreateStatement(PreparedStatement statement, Book book) throws SQLException{
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getStatus().name());
        statement.setDate(4, Date.valueOf(book.getPublishDate()));
        statement.setDouble(5, book.getPrice());
        statement.setString(6, book.getDescription());
        logger.info("Заполняем запрос на создание книги: {}", book);
    }

    @Override
    protected void setId(Book book, Integer id){
        book.setBookId(id);
    }

    @Override
    protected String getSelectByIdSql(){
        return "SELECT * FROM Book WHERE bookId = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement statement, Integer id) throws SQLException{
        statement.setInt(1, id);
    }

    @Override
    protected Book mapResultSetToEntity(ResultSet resultSet) throws SQLException{
        return new Book(resultSet.getString("title"),
                resultSet.getString("author"),
                BookStatus.valueOf(resultSet.getString("status")),
                resultSet.getDate("publish_date").toLocalDate(),
                resultSet.getDouble("price"),
                resultSet.getString("description")

        );
    }

    @Override
    protected String getSelectAllSql(){
        return "SELECT * FROM Book";
    }

    @Override
    protected String getUpdateSql(){
        return "UPDATE Book SET title = ?, author = ?, status = ?, publish_date = ?, price = ?, description = ? WHERE bookId = ?";
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, Book book) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getStatus().name());
        statement.setDate(4, Date.valueOf(book.getPublishDate()));
        statement.setDouble(5, book.getPrice());
        statement.setString(6, book.getDescription());
        statement.setInt(7, book.getBookId());
        logger.info("Заполняем запрос на обновление информации о книге: {}", book);
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM Book WHERE bookId = ?";
    }

    public Book getBookByTitle(String title) {
        try {
            return entityManager.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class)
                    .setParameter("title", title)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Книга с названием '{}' не найдена.", title);
            return null;
        } catch (Exception e) {
            logger.error("Ошибка при получении книги с названием '{}': {}", title, e.getMessage());
            throw new RuntimeException("Ошибка при получении книги по названию", e);
        }
    }
}
