package Repository;

import BookStoreModel.Order;
import DI.Inject;
import Dao.GenericDaoImpl;
import Status.OrderStatus;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class OrderRepository extends GenericDaoImpl<Order, Integer> {
    @Inject
    private Connection connection;

    @PersistenceContext
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(Connection connection){
        super(connection);
    }

    @Transactional
    public void save(Order order) {
        try {
            entityManager.persist(order);
            logger.info("Заказ с ID {} успешно сохранен.", order.getOrderId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении заказа: {}, причина: {}", order, e.getMessage());
            throw new RuntimeException("Ошибка при сохранении заказа", e);
        }
    }

    @Transactional
    public void updateOrderStatus(int orderId, OrderStatus status){
        try {
            Order order = entityManager.find(Order.class, orderId);
            if (order != null) {
                order.setStatus(status);
                entityManager.merge(order);  // Обновляем заказ
                logger.info("Статус заказа с ID {} обновлен на {}.", orderId, status);
            } else {
                logger.warn("Заказ с ID {} не найден для обновления статуса.", orderId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении статуса заказа с ID {}: {}, причина: {}", orderId, status, e.getMessage());
            throw new RuntimeException("Ошибка при обновлении статуса заказа", e);
        }
    }

    @Transactional
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
