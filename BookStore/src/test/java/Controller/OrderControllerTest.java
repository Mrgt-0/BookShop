package Controller;

import com.books.BookStore.example.Controller.OrderController;
import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Service.OrderService;
import com.books.BookStore.example.Status.*;
import jakarta.transaction.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderDTO orderDTO;

    @BeforeEach
    public void setUp() {
        Book book = new Book("Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(),9.99, ""); // Предположим, ваш конструктор Book принимает необходимые параметры
        OrderStatus orderStatus = OrderStatus.NEW;
        LocalDate executionDate = LocalDate.now();
        Double orderPrice = 9.99;

        orderDTO = new OrderDTO(book, orderStatus, executionDate, orderPrice);

        orderController = new OrderController();
        orderController.orderService = orderService;
    }

    @Test
    public void testCreateOrder() throws SystemException {
        orderController.createOrder(orderDTO);
        verify(orderService, times(1)).createOrder(orderDTO);
    }

    @Test
    public void testUpdateOrder() throws SystemException {
        int orderId = 1;
        OrderStatus status = OrderStatus.NEW;

        orderController.updateOrder(orderId, status);
        verify(orderService, times(1)).updateStatus(orderId, status);
    }

    @Test
    public void testCancelOrder() throws SystemException {
        int orderId = 1;

        orderController.cancelOrder(orderId);
        verify(orderService, times(1)).cancelOrder(orderId);
    }
}
