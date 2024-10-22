package Repository;

import BookStoreModel.Book;
import DI.Inject;
import Dao.GenericDaoImpl;
import Datebase.DatabaseConnection;
import Status.BookStatus;

import java.sql.*;
import java.time.LocalDate;

public class BookRepository extends GenericDaoImpl<Book, Integer> {
    @Inject
    private Connection connection;
    public BookRepository(Connection connection){
        super(connection);
    }

    public void save(Book book){
        String sql = getCreateSql();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            populateCreateStatement(statement, book);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setId(book, generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception while saving book: ", e);
        }
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

    public Book getByTitle(String title) {
        Book book = null;
        String query = "SELECT * FROM Book WHERE title = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String bookTitle = rs.getString("title");
                String author = rs.getString("author");
                BookStatus status = BookStatus.valueOf(rs.getString("status"));
                LocalDate publish_date = rs.getDate("publish_date").toLocalDate();
                Double price = rs.getDouble("price");
                String description = rs.getString("description");

                book = new Book(bookTitle, author, status, publish_date, price, description);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception: ", e);
        }

        return book;
    }
    @Override
    protected String getCreateSql(){
        return "INSERT INTO Book(title, author, status, publish_date, price, description) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void delete(int bookId) {
        String sql = "DELETE FROM Book WHERE bookId = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception: ", e);
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
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM Book WHERE bookId = ?";
    }

    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int bookId = resultSet.getInt("bookId");
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                BookStatus status = BookStatus.valueOf(resultSet.getString("status"));
                LocalDate publish_date = resultSet.getDate("publish_date").toLocalDate();
                Double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");
                return new Book(bookTitle, author, status, publish_date, price, description);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching book by title: " + title, e);
        }
        return null;
    }
}
