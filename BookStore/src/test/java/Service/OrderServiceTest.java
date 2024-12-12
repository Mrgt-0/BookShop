package Service;

import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.*;
import com.books.BookStore.example.Model.Order;
import com.books.BookStore.example.Repository.*;
import com.books.BookStore.example.Service.OrderService;
import com.books.BookStore.example.Status.*;
import jakarta.transaction.SystemException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Book book;
    private OrderDTO orderDTO;

    @BeforeEach
    public void setUp() {
        book = new Book("Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(), 9.99, "Description");
        orderDTO = new OrderDTO(book, OrderStatus.NEW, LocalDate.now(), 0.0);
    }

    @Test
    public void testCreateOrder_WhenBookExists() throws SystemException {
        when(bookRepository.getByTitle(book.getTitle())).thenReturn(Optional.of(book));

        orderService.createOrder(orderDTO);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_WhenBookDoesNotExist() {
        when(bookRepository.getByTitle(book.getTitle())).thenReturn(Optional.empty());

        SystemException exception = assertThrows(SystemException.class, () -> {
            orderService.createOrder(orderDTO);
        });

        assertEquals("Книга не найдена.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void testUpdateStatus_WhenOrderExists() throws SystemException {
        Order order = new Order(book, OrderStatus.NEW);
        when(orderRepository.getById(1)).thenReturn(Optional.of(order));

        orderService.updateStatus(1, OrderStatus.FULFILLED);

        verify(orderRepository, times(1)).save(order);
        assertEquals(OrderStatus.FULFILLED, order.getStatus());
    }

    @Test
    public void testUpdateStatus_WhenOrderDoesNotExist() {
        when(orderRepository.getById(1)).thenReturn(Optional.empty());

        SystemException exception = assertThrows(SystemException.class, () -> {
            orderService.updateStatus(1, OrderStatus.FULFILLED);
        });

        assertEquals("Заказ не найден.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void testCancelOrder_WhenOrderExists() throws SystemException {
        Order order = new Order(book, OrderStatus.NEW);
        when(orderRepository.getById(1)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1);

        verify(orderRepository, times(1)).delete(order.getOrderId());
    }

    @Test
    public void testCancelOrder_WhenOrderDoesNotExist() {
        when(orderRepository.getById(1)).thenReturn(Optional.empty());

        SystemException exception = assertThrows(SystemException.class, () -> {
            orderService.cancelOrder(1);
        });

        assertEquals("Заказ не найден.", exception.getMessage());
        verify(orderRepository, never()).delete(anyInt());
    }
}
