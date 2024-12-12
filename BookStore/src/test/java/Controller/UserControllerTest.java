package Controller;

import com.books.BookStore.example.Controller.UserController;
import com.books.BookStore.example.JWT.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;
    @BeforeEach
    public void setUp() {
        userController = new UserController();
        userController.authenticationManager = authenticationManager;
        userController.jwtUtil = jwtUtil;
    }

    @Test
    public void testLogin() {
        String username = "testUser";
        String password = "password";
        String token = "jwt-token";

        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(jwtUtil.generateToken(username)).thenReturn(token);

        String result = userController.login(username, password);
        assertEquals(token, result);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
