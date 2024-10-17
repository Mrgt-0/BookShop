package Repository;

import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Request;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.BookStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


public class BookRepository extends GenericDaoImpl<Book, Integer> {
    @Inject
    private Connection connection;
    public BookRepository(Connection connection){
        super(connection);
    }

    public void updateBookStatus(int bookId, BookStatus status) {
        String sql = "UPDATE Book SET status = ? WHERE bookId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception: ", e);
        }
    }

    public void removeBook(int bookId) {
        updateBookStatus(bookId, BookStatus.OUT_OF_STOCK);
    }

    @Override
    protected String getCreateSql(){
        return "INSERT INTO Book(title, author, status, publish_date, price, description) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void populateCreateStatement(PreparedStatement statement, Book book) throws SQLException{
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getStatus().name());
        statement.setDate(4, java.sql.Date.valueOf(book.getPublishDate()));
        statement.setDouble(5, book.getPrice());
        statement.setString(6, book.getDescription());
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
        statement.setDate(4, java.sql.Date.valueOf(book.getPublishDate()));
        statement.setDouble(5, book.getPrice());
        statement.setString(6, book.getDescription());
        statement.setInt(7, book.getBookId());
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM Book WHERE bookId = ?";
    }
}
