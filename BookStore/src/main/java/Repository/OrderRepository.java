package Repository;

import BookStoreModel.Book;
import BookStoreModel.BookStore;
import BookStoreModel.Order;
import Dao.GenericDaoImpl;
import Status.BookStatus;
import Status.OrderStatus;
import java.sql.*;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.*;
import javax.transaction.Transactional;

@Repository
public class OrderRepository extends GenericDaoImpl<Order, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(){
        super(Order.class);
    }

    @Transactional
    public void save(Order order) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            entityManager.persist(order);
            logger.info("Заказ с ID {} успешно сохранен.", order.getOrderId());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при сохранении заказа: {}, причина: {}", order, e.getMessage());
            throw new RuntimeException("Ошибка при сохранении заказа", e);
        }
    }

    @Transactional
    public void updateOrderStatus(int orderId, OrderStatus status) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Order order = entityManager.find(Order.class, orderId);
            if (order != null) {
                order.setStatus(status);
                entityManager.merge(order);
                logger.info("Статус заказа с ID {} обновлён на {}.", orderId, status);
            } else {
                logger.warn("Заказ с ID {} не найден для обновления статуса.", orderId);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при обновлении статуса заказа с ID {}: {}, причина: {}", orderId, status, e.getMessage());
            throw new RuntimeException("Ошибка при обновлении статуса заказа", e);
        }
    }

    @Transactional
    public Order placeOrder(String title, BookStore bookStore) {
        EntityTransaction transaction = entityManager.getTransaction();
        Order order = null;
        try {
            transaction.begin();

            Book book = bookStore.getBookInventory().get(title);
            if (book != null && book.getStatus() == BookStatus.IN_STOCK) {
                order = new Order(book, OrderStatus.NEW);
                entityManager.persist(order);
                logger.info("Заказ на книгу '{}' успешно размещен.", title);
            } else {
                logger.warn("Книга '{}' отсутствует на складе.", title);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при размещении заказа на книгу '{}': {}", title, e.getMessage(), e);
            throw new RuntimeException("Ошибка при размещении заказа", e);
        }
        return order;
    }

    @Transactional
    public void delete(int orderId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Order order = entityManager.find(Order.class, orderId);
            if (order != null) {
                entityManager.remove(order);
                logger.info("Заказ с ID {} успешно удалён.", orderId);
            } else {
                logger.warn("Заказ с ID {} не найден для удаления.", orderId);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при удалении заказа с ID {}: {}", orderId, e.getMessage());
            throw new RuntimeException("Ошибка при удалении заказа", e);
        }
    }

    @Transactional
    public void fulfillOrderByTitle(String title) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Order order = entityManager.createQuery(
                            "SELECT o FROM Order o WHERE o.book.title = :title AND o.status = :status", Order.class)
                    .setParameter("title", title)
                    .setParameter("status", OrderStatus.NEW)
                    .getSingleResult();

            if (order != null) {
                order.setStatus(OrderStatus.FULFILLED);
                order.setExecutionDate(LocalDate.now());
                entityManager.merge(order);

                logger.info("Заказ для книги '{}' выполнен.", title);
            } else {
                logger.warn("Заказ для книги '{}' не найден.", title);
            }

            transaction.commit();
        } catch (NoResultException e) {
            logger.warn("Заказ для книги '{}' не найден.", title);
            transaction.rollback();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Ошибка при выполнении заказа для книги '{}': {}", title, e.getMessage(), e);
            throw new RuntimeException("Ошибка при выполнении заказа", e);
        }
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
