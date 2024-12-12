package Controller;

import com.books.BookStore.example.Controller.BookStoreController;
import com.books.BookStore.example.DTO.BookDTO;
import com.books.BookStore.example.Service.BookStoreService;
import com.books.BookStore.example.Status.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookStoreControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BookStoreService bookStoreService;

    @InjectMocks
    private BookStoreController bookStoreController;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO bookDTO;
    private List<BookDTO> books;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookStoreController).build();

        bookDTO = new BookDTO(1,"Test Book", "Author", BookStatus.IN_STOCK, LocalDate.now(), 9.99, "");
        books = List.of(bookDTO);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        when(bookStoreService.listBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Book"));

        verify(bookStoreService, times(1)).listBooks();
    }

    @Test
    public void testAddBook() throws Exception {
        mockMvc.perform(post("/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Книга добавлена успешно."));

        verify(bookStoreService, times(1)).addBook(any(BookDTO.class));
    }

    @Test
    public void testRemoveBook() throws Exception {
        int bookId = 1;

        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(content().string("Книга удалена успешно."));

        verify(bookStoreService, times(1)).removeBook(bookId);
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        int bookId = 1;
        OrderStatus status = OrderStatus.NEW;

        mockMvc.perform(patch("/books/" + bookId + "/status")
                        .param("status", status.name()))
                .andExpect(status().isOk())
                .andExpect(content().string("Статус заказа успешно обновлён."));

        verify(bookStoreService, times(1)).updateOrderStatus(bookId, status);
    }
}
