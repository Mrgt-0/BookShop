package Service;

import com.books.BookStore.example.Model.User;
import com.books.BookStore.example.Repository.UserRepository;
import com.books.BookStore.example.Service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "password", "ROLE_USER");
    }

    @Test
    public void testLoadUserByUsername_WhenUserExists() {
        when(userRepository.getByLogin("testUser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        verify(userRepository, times(1)).getByLogin("testUser");
    }

    @Test
    public void testLoadUserByUsername_WhenUserDoesNotExist() {
        when(userRepository.getByLogin("testUser")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("testUser");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).getByLogin("testUser");
    }
}
