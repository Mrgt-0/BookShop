package Controller;

import com.books.BookStore.example.Controller.RequestController;
import com.books.BookStore.example.DTO.RequestDTO;
import com.books.BookStore.example.Model.Book;
import com.books.BookStore.example.Service.RequestService;
import com.books.BookStore.example.Status.BookStatus;
import jakarta.transaction.SystemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

public class RequestControllerTest {
    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private RequestDTO requestDTO;

    @BeforeEach
    public void setUp() {
        Book book = new Book("Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(),9.99, "");
        requestDTO = new RequestDTO(1, book);

        requestController = new RequestController();
        requestController.requestService = requestService;
    }

    @Test
    public void testRequestBook() throws SystemException {
        requestController.requestBook(requestDTO);
        verify(requestService, times(1)).requestBook(requestDTO.getBook().getTitle());
    }

    @Test
    public void testFulfillRequest() throws SystemException {
        int requestId = 1;
        requestController.fulfillRequest(requestId);
        verify(requestService, times(1)).fulfillRequest(requestId);
    }

    @Test
    public void testFulfillPendingRequests() throws SystemException {
        requestController.fulfillPendingRequests(new Book("Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(),9.99, ""));
        verify(requestService, times(1)).fulfillPendingRequests();
    }
}
