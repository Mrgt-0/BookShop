package Service;

import com.books.BookStore.example.DTO.*;
import com.books.BookStore.example.Model.*;
import com.books.BookStore.example.Repository.*;
import com.books.BookStore.example.Service.*;
import com.books.BookStore.example.Status.*;
import jakarta.transaction.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookStoreServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private BookStoreService bookStoreService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    public void setUp() {
        book = new Book("Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(), 9.99, "Description");
        bookDTO = new BookDTO(1, "Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(), 9.99, "Description");
    }

    @Test
    public void testListBooks() throws SystemException {
        when(bookRepository.getAll()).thenReturn(Arrays.asList(book));
        List<BookDTO> books = bookStoreService.listBooks();
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getTitle());
    }

    @Test
    public void testAddBook() throws SystemException {
        bookStoreService.addBook(bookDTO);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testRemoveBook() throws SystemException {
        when(bookRepository.getById(1)).thenReturn(Optional.ofNullable(book));
        bookStoreService.removeBook(1);
        verify(bookRepository, times(1)).delete(Optional.ofNullable(book));
    }

    @Test
    public void testRemoveBook_NotFound() throws SystemException {
        when(bookRepository.getById(1)).thenReturn(null);
        bookStoreService.removeBook(1);
        verify(bookRepository, never()).delete(any());
    }

    @Test
    public void testUpdateOrderStatus() throws SystemException {
        int orderId = 1;
        OrderStatus status = OrderStatus.FULFILLED;
        bookStoreService.updateOrderStatus(orderId, status);
        verify(orderRepository, times(1)).updateOrderStatus(orderId, status);
    }

    @Test
    public void testCancelOrder() throws SystemException {
        BookStore bookStore = mock(BookStore.class);
        when(bookStore.getOrders()).thenReturn(Arrays.asList(new Order(book, OrderStatus.NEW)));
        bookStoreService = new BookStoreService(bookStore);
        bookStoreService.cancelOrder("Test Book");
        verify(orderService, times(1)).cancelOrder(1);
        verify(orderRepository, times(1)).delete(1);
    }

    @Test
    public void testPlaceOrder_ThrowsSystemException_WhenBookNotFound() {
        OrderDTO orderDTO = new OrderDTO(new Book("Nonexistent Book", "", BookStatus.IN_STOCK, LocalDate.now(), 0.0, ""), OrderStatus.NEW, LocalDate.now(), 0.0);
        SystemException thrown = assertThrows(SystemException.class, () -> {
            bookStoreService.placeOrder(orderDTO);
        });
        assertEquals("Книга 'Nonexistent Book' не найдена.", thrown.getMessage());
    }
}
