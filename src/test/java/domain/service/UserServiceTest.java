package domain.service;

import domain.model.User;
import domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        String name = "Max Mustermann";
        String email = "max@test.de";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        User result = userService.registerUser(name, email);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "bereits@belegt.de";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("Egal", email);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}