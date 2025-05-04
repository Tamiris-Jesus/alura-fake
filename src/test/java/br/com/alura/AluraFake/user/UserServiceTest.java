package br.com.alura.AluraFake.user;

import br.com.alura.AluraFake.domain.user.User;
import br.com.alura.AluraFake.domain.user.UserRepository;
import br.com.alura.AluraFake.domain.user.UserService;
import br.com.alura.AluraFake.infra.security.TokenService;
import br.com.alura.AluraFake.domain.user.dto.AuthenticationRequestDTO;
import br.com.alura.AluraFake.domain.user.dto.AuthenticationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private TokenService tokenService;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        tokenService = mock(TokenService.class);
        authenticationManager = mock(AuthenticationManager.class);
        userService = new UserService(userRepository, tokenService);
    }

    @Test
    void shouldAuthenticateAndReturnToken() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("test@example.com", "password");
        User mockUser = mock(User.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(mockUser)).thenReturn("fake-jwt-token");

        AuthenticationResponseDTO response = userService.authenticate(request, authenticationManager);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
    }

    @Test
    void shouldLoadUserByUsernameIfExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        var loadedUser = userService.loadUserByUsername("test@example.com");
        assertNotNull(loadedUser);
        assertEquals("test@example.com", loadedUser.getUsername());
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("notfound@example.com");
        });
    }
}
