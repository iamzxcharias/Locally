package domain.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.port.UserRepository;
import domain.model.User;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // --- ARRANGE ---
        // Wir simulieren: Die E-Mail ist noch NICHT vergeben
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        // --- ACT ---
        User user = new User("TestUser", "test@example.com");
        userService.registerUser(user);

        // --- ASSERT ---
        // Wurde gespeichert?
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        // --- ARRANGE ---
        String existingEmail = "busy@example.com";

        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        // --- ACT & ASSERT ---
        User user = new User("Bad Guy", existingEmail);

        // Hier prüfen wir: "Wirft dieser Codeblock eine IllegalArgumentException?"
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(user);
        });

        // Zusatz: Wir können auch prüfen, dass save() NIEMALS aufgerufen wurde
        verify(userRepository, never()).save(any());
    }
}