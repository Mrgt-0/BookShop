package Repository;

import BookStoreModel.Order;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.OrderStatus;

import java.sql.*;

public class OrderRepository extends GenericDaoImpl<Order, Integer> {
    @Inject
    private Connection connection;
    public OrderRepository(Connection connection){
        super(connection);
    }

    public void save(Order order) {
        String sql = getCreateSql();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            populateCreateStatement(statement, order);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setId(order, generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception while saving order: ", e);
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus status){
        String sql = "UPDATE order_date SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception: ", e);
        }
    }

    public void delete(int orderId){
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    protected String getCreateSql(){
        return "INSERT INTO Order_book(status, order_date, order_price) VALUES()";
    }

    @Override
    protected void populateCreateStatement(PreparedStatement statement, Order order) throws SQLException{
        statement.setString(1, order.getStatus().name());
        statement.setDate(2, Date.valueOf(order.getExecutionDate()));
        statement.setDouble(3, order.getOrderPrice());
    }

    @Override
    protected void setId(Order order, Integer id){
        order.setOrderId(id);
    }

    @Override
    protected String getSelectByIdSql(){
        return "SELECT * FROM Order_book WHERE orderId = ?";
    }

    @Override
    protected void setIdParameter(PreparedStatement statement, Integer id) throws SQLException{
        statement.setInt(1, id);
    }
    @Override
    protected Order mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Order(OrderStatus.valueOf(resultSet.getString("status")),
                resultSet.getDate("order_date").toLocalDate(),
                resultSet.getDouble("order_price"));
    }

    @Override
    protected String getSelectAllSql(){
        return "SELECT * FROM order_date";
    }

    @Override
    protected String getUpdateSql(){
        return "UPDATE order_date SET status = ?, order_date = ?, order_price = ?";
    }

    @Override
    protected void populateUpdateStatement(PreparedStatement statement, Order order) throws SQLException{
        statement.setString(1, order.getStatus().name());
        statement.setDate(2, Date.valueOf(order.getExecutionDate()));
        statement.setDouble(3, order.getOrderPrice());
        statement.setInt(4, order.getOrderId());
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM order_date WHERE orderId = ?";
    }
}
