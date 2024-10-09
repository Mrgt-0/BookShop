package Repository;

import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Request;
import Status.BookStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


public class BookRepository {
    private final Connection connection;

    public BookRepository() {
        this.connection = getConnection();
    }

    private Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String username = "your_username";
        String password = "your_password";

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection", e);
        }
    }

    public void createBook(Book book) {
        String sql = "INSERT INTO books (title, author, status, publish_date, price, description) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getStatus().name());
            statement.setDate(4, java.sql.Date.valueOf(book.getPublishDate()));
            statement.setDouble(5, book.getPrice());
            statement.setString(6, book.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add book", e);
        }
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM Book WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Book book = new Book(
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            BookStatus.valueOf(resultSet.getString("status")),
                            resultSet.getDate("publish_date").toLocalDate(),
                            resultSet.getDouble("price"),
                            resultSet.getString("description")
                    );
                    book.setBookId(resultSet.getInt("id"));
                    return book;
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }

    public void updateBookStatus(int bookId, BookStatus status) {
        String sql = "UPDATE Book SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void removeBook(int bookId) {
        updateBookStatus(bookId, BookStatus.OUT_OF_STOCK);
    }

    public void fulfillPendingRequests(BookStore bookStore) {
        RequestRepository requestRepository = new RequestRepository();
        List<Request> requests = requestRepository.getAllRequests();
        for (Request request : requests) {
            Book book = getBookById(request.getBook().getBookId());
            if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
                // Обновите логику выполнения запросов без учета количества книг
                updateBookStatus(book.getBookId(), BookStatus.OUT_OF_STOCK);
                requestRepository.deleteRequest(request.getRequestId());
            }
        }
    }
}
