package Repository;

import BookStoreModel.Order;
import Status.OrderStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final Connection connection;

    public OrderRepository() {
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

    public void createOrder(Order order) {
        String sql = "INSERT INTO Orders (book_id, status, execution_date, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, order.getBook().getBookId());
            statement.setString(2, order.getStatus().name());
            statement.setDate(3, Date.valueOf(order.getExecutionDate()));
            statement.setInt(4, order.getOrderPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        String sql = "SELECT * FROM Orders WHERE status = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            new BookRepository().getBookById(resultSet.getInt("book_id")),
                            OrderStatus.valueOf(resultSet.getString("status"))
                    );
                    order.setOrderId(resultSet.getInt("id"));
                    order.setExecutionDate(resultSet.getDate("execution_date").toLocalDate());
                    order.setOrderPrice(resultSet.getInt("price"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return orders;
    }

    public void cancelOrder(int orderId) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}
