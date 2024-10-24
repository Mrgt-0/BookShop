package Repository;

import BookStoreModel.Order;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.OrderStatus;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderRepository extends GenericDaoImpl<Order, Integer> {
    @Inject
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(Connection connection){
        super(connection);
    }

    public void save(Order order) {
        String sql = getCreateSql();
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            populateCreateStatement(statement, order);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setId(order, generatedKeys.getInt(1));
                        logger.info("Заказ с ID {} успешно сохранен.", order.getOrderId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при сохранении заказа: {}, SQL exception: {}", order, e.getMessage());
            throw new RuntimeException("SQL exception while saving order: ", e);
        }
    }

    public void updateOrderStatus(int orderId, OrderStatus status){
        String sql = "UPDATE order_date SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, orderId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                logger.info("Статус заказа с ID {} обновлен на {}.", orderId, status);
            } else {
                logger.warn("Заказ с ID {} не найден для обновления статуса.", orderId);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении статуса заказа с ID {}: {}, SQL exception: {}", orderId, status, e.getMessage());
            throw new RuntimeException("SQL exception: ", e);
        }
    }

    public void delete(int orderId){
        logger.info("Удаляем заказ с ID {} (статус будет изменен на CANCELLED).", orderId);
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
        logger.info("Заполняем запрос на создание заказа: {}", order);
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
        logger.info("Заполняем запрос на обновление информации о заказе: {}", order);
    }

    @Override
    protected String getDeleteSql(){
        return "DELETE FROM order_date WHERE orderId = ?";
    }
}
