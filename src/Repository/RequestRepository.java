package Repository;

import BookStoreModel.Book;
import BookStoreModel.Request;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class RequestRepository {
    private final Connection connection;

    public RequestRepository() {
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

    public void createRequest(Request request) {
        String sql = "INSERT INTO Requests (book_id, count) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, request.getBook().getBookId());
            statement.setInt(2, request.getRequestCount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public List<Request> getAllRequests() {
        String sql = "SELECT * FROM Requests";
        List<Request> requests = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                Book book = new BookRepository().getBookById(bookId);
                Request request = new Request(book);
                request.setRequestId(resultSet.getInt("id"));
                request.setRequestCount(resultSet.getInt("count"));
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all requests", e);
        }

        return requests;
    }

    public void deleteRequest(int requestId) {
        String sql = "DELETE FROM Requests WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
