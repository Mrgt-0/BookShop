package Service;

import com.books.BookStore.example.DTO.OrderDTO;
import com.books.BookStore.example.Model.*;
import com.books.BookStore.example.Repository.RequestRepository;
import com.books.BookStore.example.Service.*;
import com.books.BookStore.example.Status.BookStatus;
import jakarta.transaction.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    private BookStore bookStore;

    @Mock
    private OrderService orderService;

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestService requestService;

    private Book book;
    private String bookTitle;

    @BeforeEach
    public void setUp() {
        book = new Book("Test Book", "Author", BookStatus.OUT_OF_STOCK, LocalDate.now(), 9.99, "Description");
        bookTitle = "Test Book";
    }

    @Test
    public void testRequestBook_WhenBookIsOutOfStock() throws SystemException {
        when(bookStore.getBookInventory()).thenReturn(Map.of(bookTitle, book));

        requestService.requestBook(bookTitle);

        verify(requestRepository, times(1)).create(any(Request.class));
        verify(orderService, never()).createOrder(any(OrderDTO.class));
    }

    @Test
    public void testRequestBook_WhenBookIsInStock() throws SystemException {
        book.setStatus(BookStatus.IN_STOCK);
        when(bookStore.getBookInventory()).thenReturn(Map.of(bookTitle, book));

        requestService.requestBook(bookTitle);

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
        verify(requestRepository, never()).create(any(Request.class));
    }

    @Test
    public void testRequestBook_WhenBookDoesNotExist() throws SystemException {
        when(bookStore.getBookInventory()).thenReturn(Map.of());

        requestService.requestBook(bookTitle);

        verify(requestRepository, never()).create(any(Request.class));
        verify(orderService, never()).createOrder(any(OrderDTO.class));
    }

    @Test
    public void testFulfillRequest_WhenRequestExistsAndBookIsInStock() throws SystemException {
        Request request = new Request(book);
        when(requestRepository.getById(1)).thenReturn(Optional.of(request));
        book.setStatus(BookStatus.IN_STOCK);

        requestService.fulfillRequest(1);

        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
        verify(requestRepository, times(1)).delete(1);
    }

    @Test
    public void testFulfillRequest_WhenRequestDoesNotExist() throws SystemException {
        when(requestRepository.getById(1)).thenReturn(Optional.empty());

        assertThrows(SystemException.class, () -> {
            requestService.fulfillRequest(1);
        });

        verify(orderService, never()).createOrder(any(OrderDTO.class));
        verify(requestRepository, never()).delete(anyInt());
    }

    @Test
    public void testFulfillRequest_WhenBookIsNotInStock() throws SystemException {
        Request request = new Request(book);
        when(requestRepository.getById(1)).thenReturn(Optional.of(request));
        book.setStatus(BookStatus.OUT_OF_STOCK);

        requestService.fulfillRequest(1);

        verify(orderService, never()).createOrder(any(OrderDTO.class));
        verify(requestRepository, never()).delete(anyInt());
    }

    @Test
    public void testFulfillPendingRequests() throws SystemException {
        List<Request> requests = List.of(new Request(book));
        when(requestRepository.getAll()).thenReturn(requests);
        book.setStatus(BookStatus.IN_STOCK);

        requestService.fulfillPendingRequests();

        verify(requestRepository, times(1)).getAll();
        verify(orderService, times(1)).createOrder(any(OrderDTO.class));
    }
}
